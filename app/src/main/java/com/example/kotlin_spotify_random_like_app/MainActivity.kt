package com.example.kotlin_spotify_random_like_app

import AddTracksRequest
import Offset
import PlayRequest
import PlaylistRequest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_spotify_random_like_app.databinding.ActivityMainBinding
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var spotifyAuth: SpotifyConnection
    private lateinit var spotifyApi: SpotifyApi

    private val clientId = "1e6d0591bbb64af286b323ff7d26ce0f"
    private val redirectUri = "http://com.example.kotlin_spotify_random_like_app/callback"
    private val REQUEST_CODE = 1337
    companion object {
         var accessToken: String? = null // accessToken değişkenini tanımladık
    }
    private var playlistId : String? = null
   // private lateinit var spotifyService: SpotifyService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        spotifyAuth = SpotifyConnection(this)
        spotifyApi = SpotifyApi // SpotifyApi nesnesini başlatın

        binding.button.setOnClickListener{
            val builder : AuthorizationRequest.Builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri);
            builder.setScopes(arrayOf("streaming","user-modify-playback-state","user-read-private", "playlist-read", "playlist-read-private","playlist-modify-private","playlist-modify-public","user-read-email","user-read-recently-played","user-read-currently-playing"))
            val request: AuthorizationRequest = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
        }


        binding.button6.setOnClickListener {
           // val playlistManager = SpotifyPlayback(this,spotifyApi, accessToken.toString())
           // playlistManager.addTrackToPlaylist()

        }

        binding.button5.setOnClickListener {
            /// Yeni Play List Oluşturma
            val createPlayList = CreatePlayList(this, spotifyApi, accessToken.toString())
            createPlayList.create()
        }
        binding.button4.setOnClickListener {
            //Random Music Oluşturma
            val intent = Intent(this, SpotifySwipeMusic::class.java)
            startActivity(intent)
            //play()
        }
        binding.button2.setOnClickListener {
            val token = "Bearer ${accessToken.toString()}"
            Log.e("basari",accessToken.toString())
            CoroutineScope(Dispatchers.IO).launch{
                try {
                    AuthorizationResponse.Type.CODE
                    spotifyApi.service.pause(token)
                }
                catch (e: Exception) {
                    Log.e("deneme", "Error: ${e.message}")
                }
            }
        }

        binding.button3.setOnClickListener {
            val token = "Bearer ${accessToken.toString()}"
                CoroutineScope(Dispatchers.IO).launch{
                    try {
                        spotifyApi.service.next(token)
                    }
                    catch (e: Exception) {
                        Log.e("deneme", "Error: ${e.message}")
                    }
                }
        }

    }


    override fun onStart() {
        super.onStart()
        spotifyAuth?.connectionStart()
    }

    override fun onStop() {
        super.onStop()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    accessToken = response.accessToken
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("hata","hata")
                }
                else -> {
                }
            }
        }
    }

}