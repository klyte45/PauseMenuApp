package com.halkyproject.pausemenu.service

import android.app.Service
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.halkyproject.pausemenu.activities.player.LoggPlayer.Song
import com.halkyproject.pausemenu.util.LoopMediaPlayer
import java.util.*


class LoggPlayerService : Service() {

    companion object {
        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_REWIND = "action_rewind"
        const val ACTION_FAST_FORWARD = "action_fast_foward"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREVIOUS = "action_previous"
        const val ACTION_STOP = "action_stop"
        const val UID_NOTIF = 574987
    }


    private var currSong: Song? = null
    private var currPlayer: LoopMediaPlayer? = null
    private var startPlayTime: Long = 0
    private var pauseTime: Long = -1
    private lateinit var mSession: MediaSession

    val songs: ArrayList<Song> = ArrayList()
    private var songPosn: Int = 0

    override fun onCreate() {
        super.onCreate()
        songPosn = 0
        mSession = MediaSession(this, "LoggPlayerService")
        mSession.setCallback(MediaSessionCallback())
    }

    private val musicBind = MusicBinder()

    override fun onBind(intent: Intent): IBinder? {
        return musicBind
    }

    override fun onUnbind(intent: Intent): Boolean {
        return false
    }

    fun getCurrentTime(): Long {
        return Date().time - startPlayTime
    }

    fun release() {
        currPlayer?.release()
    }

    fun playSong() {
        release()
        //get song
        currSong = songs[songPosn]
        try {
            currPlayer = LoopMediaPlayer.create(applicationContext, Uri.fromFile(currSong!!.file), currSong!!.loopFrame)
            startPlayTime = Date().time

            mSession.setMetadata(MediaMetadata.Builder()
                    .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, null)
                    .putString(MediaMetadata.METADATA_KEY_ARTIST, currSong!!.artist)
                    .putString(MediaMetadata.METADATA_KEY_ALBUM, currSong!!.album)
                    .putString(MediaMetadata.METADATA_KEY_TITLE, currSong!!.title)
                    .build())
        } catch (e: Exception) {
            Log.e("MUSIC SERVICE", "Error setting data source", e)
            throw e
        }

    }

    fun setSong(songIndex: Int) {
        songPosn = songIndex
    }

    inner class MusicBinder : Binder() {
        internal val service: LoggPlayerService
            get() = this@LoggPlayerService
    }

    private inner class MediaSessionCallback : MediaSession.Callback() {
        override fun onPlay() {
            if (!songs.isEmpty()) {
                handlePlayRequest()
            }
        }

        override fun onSkipToQueueItem(queueId: Long) {
            if (!songs.isEmpty()) {
                // set the current index on queue from the music Id:
                songPosn = (queueId % songs.size).toInt()
                // play the music
                playSong()
            }
        }

        override fun onSeekTo(position: Long) {
        }

        override fun onPause() {
            handlePauseRequest()
        }

        override fun onStop() {
            handleStopRequest()
        }

        override fun onSkipToNext() {
            next()
        }

        override fun onSkipToPrevious() {
            prev()
        }
    }

    fun prev() {
        songPosn--
        songPosn += songs.size
        songPosn %= songs.size
        playSong()
    }

    fun next() {
        songPosn++
        songPosn %= songs.size
        playSong()
    }

    private fun handleStopRequest() {
        currPlayer?.release()
    }

    private fun handlePlayRequest() {
        if (currPlayer != null) {
            currPlayer!!.play()
            startPlayTime += Date().time - pauseTime
            pauseTime = -1
        } else playSong()
    }

    private fun handlePauseRequest() {
        currPlayer!!.pause()
        pauseTime = Date().time
    }

}
