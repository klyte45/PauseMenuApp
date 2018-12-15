package com.halkyproject.pausemenu.singletons

//import org.apache.tika.metadata.Metadata
//import org.apache.tika.parser.ParseContext
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.IBinder
import android.widget.ListView
import com.halkyproject.pausemenu.activities.player.LoggPlayer
import com.halkyproject.pausemenu.adapter.SongAdapter
import com.halkyproject.pausemenu.service.LoggPlayerService
import org.gagravarr.ogg.audio.OggAudioStatistics
import org.gagravarr.vorbis.VorbisFile
import java.io.File


object MusicServiceSingleton {
    private const val LOOPSTART_TAG = "loopstart"

    private var musicSrv: LoggPlayerService? = null
    private var playIntent: Intent? = null
    private var musicBound = false

    private fun getMusicConnection(songView: ListView, player: LoggPlayer): ServiceConnection {
        return object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                val binder = service as LoggPlayerService.MusicBinder
                //get service
                musicSrv = binder.service
                val songAdt = SongAdapter(player, musicSrv!!.songs)
                songView.adapter = songAdt

                //pass list
                musicBound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                musicBound = false
            }
        }
    }

    fun bindPlayer(songView: ListView, player: LoggPlayer) {
        playIntent = Intent(player.applicationContext, LoggPlayerService::class.java)
        player.applicationContext.bindService(playIntent, getMusicConnection(songView, player), Context.BIND_AUTO_CREATE)
        player.applicationContext.startService(playIntent)
    }

    fun setSong(idx: Int) {
        musicSrv!!.setSong(idx)
        musicSrv!!.playSong()
    }

    fun finalize() {
        musicSrv?.release()
    }

    fun updateSongList(fileList: Collection<File>, ctx: Context) {
        musicSrv!!.songs.removeAll { true }
        fileList.forEach { it ->
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(ctx.applicationContext, Uri.fromFile(it))

            val file = VorbisFile(it)
            val stats = OggAudioStatistics(file, file)
            stats.calculate()


            var loopStart = -1L
            if (file.comment.getComments(LOOPSTART_TAG)?.getOrNull(0) != null) {
                try {
                    loopStart = file.comment.getComments(LOOPSTART_TAG)[0].trim().toLong() * 1000 / file.info.sampleRate
                    file.skipToGranule(file.comment.getComments(LOOPSTART_TAG)[0].trim().toLong())
                } catch (e: Exception) {
                }
            }


            musicSrv!!.songs.add(LoggPlayer.Song(it,
                    mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: it.name,
                    mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR) ?: "???",
                    mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: "???",
                    loopStart.toInt(),
                    (stats.durationSeconds * 1000).toInt()
            ))
        }
        musicSrv!!.songs.sortBy { it.title }
    }

    fun next() {
        musicSrv?.next()
    }
}