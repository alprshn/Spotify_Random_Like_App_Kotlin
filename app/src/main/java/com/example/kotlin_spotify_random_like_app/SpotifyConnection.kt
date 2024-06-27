package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track


class SpotifyConnection(private val context: Context) {
     private val clientId = "1e6d0591bbb64af286b323ff7d26ce0f"
     private val redirectUri = "http://com.example.kotlin_spotify_random_like_app/callback"
    private val REQUEST_CODE = 1337

     var spotifyAppRemote: SpotifyAppRemote? = null
    fun connectionStart(){
        Log.e("MainActivity", "Oldu bu iis")

        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.e("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                //connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("Baglandi", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }
    fun play(trackUri: String) {
        spotifyAppRemote?.let {
            it.playerApi.play(trackUri)
            Log.e("SpotifyConnection", "Playing track: $trackUri")
        }
    }
    private fun connected() {
        spotifyAppRemote?.let {
            // Play a playlist

            val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
            it.playerApi.play(playlistURI)
            it.playerApi.pause()
            Log.e("Baglandi", "deneme")
            // Subscribe to PlayerState

        }

    }

}