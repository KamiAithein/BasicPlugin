package edu.koepcke3.spigot.christmastime.weather;

import org.bukkit.event.HandlerList;

public class SnowEvent extends WeatherEvent {
    private static final HandlerList handlers = new HandlerList();
    private String message;

    public SnowEvent(String example) {
        super(example);
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
