package edu.koepcke3.spigot.christmastime.weather

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

object WeatherSim {


    enum class Weather : WeatherType {
        NONE {
            override fun callEvent() {
                Bukkit.getServer().pluginManager.callEvent(WeatherEvent("no weather"))
            }

            override fun handleWeather(): BukkitRunnable {
                callEvent()
                return object: BukkitRunnable() {
                    override fun run() { } //nothing
                }
            }

        },
        SNOW {
            override fun callEvent() {
                Bukkit.getServer().pluginManager.callEvent(SnowEvent("snow"))
            }

            override fun handleWeather(): BukkitRunnable {
                println("handle snow!")
                callEvent()
                return object: BukkitRunnable() {
                    override fun run() {
                        Bukkit.getOnlinePlayers().forEach{ player ->
                            SnowHandler.snowOn(player)
                        }
                    }

                }
            }
        },
        SNOW_STORM {
            override fun callEvent() {
                Bukkit.getServer().pluginManager.callEvent(WeatherEvent("snow storm"))
            }

            override fun handleWeather(): BukkitRunnable {
                callEvent()
                return object: BukkitRunnable() {
                    override fun run() {
                        Bukkit.getOnlinePlayers().forEach{ player ->
                            SnowHandler.snowOn(player, 1000, 0.15, Vector(-0.2, -0.5, -Math.random()*0.2), isSnowStorm = true)
                        }
                    }
                }
            }
        }
        ;
        abstract fun callEvent()
    }


    val P_WEATHER: Map<Weather, Double> = mutableMapOf(
            Pair(Weather.NONE, 0.6),
            Pair(Weather.SNOW, 0.3),
            Pair(Weather.SNOW_STORM, 0.1)
    )

    //https://stackoverflow.com/a/4437287
    public fun getRandomWeather(): Weather {
        var r = Math.random()

        if(P_WEATHER.isEmpty()) {
            throw IllegalArgumentException("P_WEATHER IS EMPTY!")
        }
        lateinit var weather: Weather
        for((k,v) in P_WEATHER) {
            weather = k
            r -= v
            if(r < 0) {
                return weather
            }
        }
        return weather
    }

}


