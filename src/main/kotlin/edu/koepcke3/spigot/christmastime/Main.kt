package edu.koepcke3.spigot.christmastime

import com.xxmicloxx.NoteBlockAPI.model.FadeType
import com.xxmicloxx.NoteBlockAPI.model.Playlist
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode
import com.xxmicloxx.NoteBlockAPI.model.Song
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder
import edu.koepcke3.spigot.Tick
import edu.koepcke3.spigot.christmastime.events.christmashead.*
import edu.koepcke3.spigot.christmastime.items.ItemEventHandler
import edu.koepcke3.spigot.christmastime.weather.MusicHandler
import edu.koepcke3.spigot.christmastime.weather.WeatherCommandHandler
import edu.koepcke3.spigot.christmastime.weather.WeatherEventHandler
import edu.koepcke3.spigot.christmastime.weather.WeatherSim
import org.bukkit.*
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

//egg do random shit -- wizzerdd
public class Main : JavaPlugin() {
    private var WEATHER_TASK = -1

    lateinit var currentWeather: Pair<WeatherSim.Weather, BukkitTask>

    lateinit var christmasHead: ChristmasHead

    public override fun onEnable() {
        super.onEnable()
        println("playing!")

        MusicHandler.init(this.dataFolder)
        //^^ do the above so that it doesnt crash on start up

        val weather = WeatherSim.getRandomWeather()
        currentWeather = Pair(weather, weather.handleWeather().runTaskTimer(this, 0, 5))

        christmasHead = ChristmasHead(this)

        val commands = setOf(
                WeatherCommandHandler(this),
                ChristmasHeadCommandHandler(this)
        )

        commands.forEach { cmdClass ->
            Bukkit.broadcastMessage("${cmdClass.javaClass}")
            cmdClass.getCommandNames().forEach { cmd ->
                Bukkit.broadcastMessage("cmd $cmd")
                this.getCommand(cmd)?.setExecutor(cmdClass)
            }
        }

        val eventListeners: Set<Listener> = setOf(
                ItemEventHandler(this),
                WeatherEventHandler(this),
                ChristmasHeadMobEventHandler(this),
                ChristmasHeadBlockEventHandler(this)

        )
        eventListeners.forEach{ this.server.pluginManager.registerEvents(it, this) }

        WEATHER_TASK = Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(this, { getNextWeather() }, 0L, Tick.minToTick(5))

    }

    fun setWeather(weather: WeatherSim.Weather) {
        if(!currentWeather.second.isCancelled) {
            currentWeather.second.cancel()
        }
        currentWeather = Pair(weather, weather.handleWeather().runTaskTimer(this, 0, 5))
    }
    fun getNextWeather() {
        val weather = WeatherSim.getRandomWeather()
        setWeather(weather)
    }
    fun toggleWeatherCycle() {
        setWeather(WeatherSim.Weather.NONE)
        if(currentWeather.second.isCancelled) {
            WEATHER_TASK = Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(this, {getNextWeather()}, 0L, Tick.minToTick(5))
        }
        else {
            currentWeather.second.cancel()
            Bukkit.getServer().scheduler.cancelTask(WEATHER_TASK)
        }
    }

    public override fun onDisable() {
        super.onDisable()
    }
}