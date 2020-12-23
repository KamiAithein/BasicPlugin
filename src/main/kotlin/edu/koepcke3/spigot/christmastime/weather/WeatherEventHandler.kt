package edu.koepcke3.spigot.christmastime.weather

import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent
import com.xxmicloxx.NoteBlockAPI.model.FadeType
import com.xxmicloxx.NoteBlockAPI.model.Playlist
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder
import edu.koepcke3.spigot.christmastime.Main
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

public class WeatherEventHandler(private val plugin: Main) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        MusicHandler.addPlayerToRadio(event.player)
    }

    @EventHandler
    fun onSongEnd(event: SongEndEvent) {
        MusicHandler.stopMusic()
    }

    @EventHandler
    fun onSnowEvent(event: SnowEvent) {
        println("playing!")

        MusicHandler.reloadSongFiles(plugin.dataFolder)
        MusicHandler.startMusic()

        Bukkit.getOnlinePlayers().forEach{MusicHandler.addPlayerToRadio(it)}
        Bukkit.broadcastMessage("The current song is: ${MusicHandler.getCurrentSong().path}, dm @沒有Mayo#1804 if you hate it to have it removed")
    }

    @EventHandler
    fun onWeatherEvent(event: WeatherEvent) {
        if(event !is SnowEvent) {
            println("not playing!")
            MusicHandler.stopMusic()
        }
    }
}