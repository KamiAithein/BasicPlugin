package edu.koepcke3.spigot.christmastime.weather;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WeatherEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String message;

    public WeatherEvent(String example) {
        message = example;
    }

    public String getMessage() {
        return message;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
