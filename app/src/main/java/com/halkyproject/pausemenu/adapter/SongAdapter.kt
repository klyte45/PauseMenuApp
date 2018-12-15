package com.halkyproject.pausemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.activities.player.LoggPlayer.Song


class SongAdapter(c: Context, theSongs: ArrayList<Song>) : BaseAdapter() {
    private var songs: ArrayList<Song> = theSongs
    private var songInf: LayoutInflater = LayoutInflater.from(c)

    override fun getCount(): Int {
        return songs.size
    }

    override fun getItem(arg0: Int): Any? {
        return null
    }

    override fun getItemId(arg0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        //map to song layout
        val songLay =
                if (convertView == null || convertView !is LinearLayout) {
                    songInf.inflate(R.layout.layout_loggplayer_song, parent, false) as LinearLayout
                } else {
                    convertView
                }
        //get title and artist views
        val songView = songLay.findViewById(R.id.song_title) as TextView
        val artistView = songLay.findViewById(R.id.song_artist) as TextView
        val timeView = songLay.findViewById(R.id.m_duration) as TextView
        //get song using position
        val currSong = songs[position]
        //get title and artist strings
        songView.text = currSong.title
        artistView.text = currSong.artist

        if (currSong.loopFrame < 0) {
            timeView.text = String.format("%02d:%02d,%01d", currSong.songLength / 60000, (currSong.songLength % 60000) / 1000, (currSong.songLength % 1000) / 100)
        } else {
            val loopSize = currSong.songLength - currSong.loopFrame
            timeView.text = String.format("%02d:%02d,%01d + %02d:%02d,%01d",
                    currSong.loopFrame / 60000, (currSong.loopFrame % 60000) / 1000, (currSong.loopFrame % 1000) / 100,
                    loopSize / 60000, (loopSize % 60000) / 1000, (loopSize % 1000) / 100
            )
        }

        //set position as tag
        songLay.tag = position
        return songLay
    }
}