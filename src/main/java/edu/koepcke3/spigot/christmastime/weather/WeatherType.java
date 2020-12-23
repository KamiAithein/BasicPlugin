package edu.koepcke3.spigot.christmastime.weather;

import org.bukkit.scheduler.BukkitRunnable;

public interface WeatherType {
    /**
     * Handles weather by creating a task on a timer that can be cancelled
     */
    BukkitRunnable handleWeather();
}