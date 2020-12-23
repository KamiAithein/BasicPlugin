package edu.koepcke3.spigot.christmastime.weather

import com.xxmicloxx.NoteBlockAPI.model.FadeType
import com.xxmicloxx.NoteBlockAPI.model.Playlist
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode
import com.xxmicloxx.NoteBlockAPI.model.Song
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder
import org.bukkit.entity.Player
import java.io.File

object MusicHandler {
    lateinit var playlist: Playlist
    lateinit var rsp: RadioSongPlayer

    val radioBlackList: MutableSet<Player> = mutableSetOf()

    private var init = false

    fun init(dataFolder: File) {
        //only one song at a time
        if(!dataFolder.exists()){
            dataFolder.mkdir()
        }
        playlist = Playlist(getSongsDir(dataFolder).random())
        rsp = RadioSongPlayer(playlist)
        rsp.isRandom = true
        rsp.repeatMode = RepeatMode.NO
        rsp.volume = 10
        rsp.fadeIn.fadeDuration = 3
        rsp.fadeIn.type = FadeType.LINEAR
        rsp.fadeOut.fadeDuration = 3
        rsp.fadeOut.type = FadeType.LINEAR
        rsp.autoDestroy = true
        rsp.isPlaying = false
        init = true
    }
    fun getSongsDir(dataFolder: File): List<Song> {
        val songs: MutableList<Song> = mutableListOf()
        if(!dataFolder.exists()){
            dataFolder.mkdir()
        }
        val files = dataFolder.listFiles()
        files?.forEach { file ->
            if(file != null) {
                songs.add(NBSDecoder.parse(file))
            }
        }
        return songs.toList()
    }
    fun addPlayerToRadio(player: Player): Boolean {
        if(player !in radioBlackList) {
            rsp.addPlayer(player)
            return true
        }
        return false
    }
    fun blacklistPlayer(player: Player) {
        radioBlackList.add(player)
        rsp.removePlayer(player)
    }
    fun unblacklistPlayer(player: Player) {
        radioBlackList.remove(player)
        addPlayerToRadio(player)
    }
    fun getBlacklist(): Set<Player> {
        return radioBlackList
    }
    fun isPlaying(): Boolean {
        return rsp.isPlaying
    }
    fun stopMusic() {
        rsp.isPlaying = false
    }
    fun startMusic() {
        rsp.isPlaying = true
    }
    fun reloadSongFiles(dataFolder: File) {
        stopMusic()
        rsp.destroy()
        init(dataFolder)
    }
    fun getCurrentSong(): Song {
        return rsp.song
    }
}