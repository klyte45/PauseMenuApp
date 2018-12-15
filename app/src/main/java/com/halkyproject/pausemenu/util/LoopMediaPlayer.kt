package com.halkyproject.pausemenu.util

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import com.halkyproject.pausemenu.singletons.MusicServiceSingleton


class LoopMediaPlayer private constructor(context: Context, val uri: Uri, private val loopStart: Int) {

    private var mContext: Context? = null
    private var mCounter = 1

    private var mCurrentPlayer: MediaPlayer? = null
    private var mNextPlayer: MediaPlayer? = null

    private val onCompletionListener = OnCompletionListener { mediaPlayer ->
        mediaPlayer.release()
        if (loopStart >= 0) {
            mCurrentPlayer = mNextPlayer
            createNextMediaPlayer()
        } else {
            MusicServiceSingleton.next()
        }
    }

    init {
        mContext = context

        mCurrentPlayer = MediaPlayer.create(mContext, uri)
        mCurrentPlayer!!.setOnPreparedListener { mCurrentPlayer!!.start() }
        if (loopStart >= 0) {
            createNextMediaPlayer()
        }
    }

    private fun createNextMediaPlayer() {
        mNextPlayer = MediaPlayer.create(mContext, uri)
        mCurrentPlayer!!.setNextMediaPlayer(mNextPlayer)
        mCurrentPlayer!!.setOnCompletionListener(onCompletionListener)
        mNextPlayer!!.seekTo(loopStart + 150)
    }

    fun release() {
        mCurrentPlayer?.release()
        mNextPlayer?.release()
    }

    fun pause() {
        mCurrentPlayer!!.stop()
    }

    fun play() {
        mCurrentPlayer!!.start()
    }

    companion object {

//        val TAG = LoopMediaPlayer::class.java.simpleName

        fun create(context: Context, uri: Uri, loopStart: Int): LoopMediaPlayer {
            return LoopMediaPlayer(context, uri, loopStart)
        }
    }
}