package edu.koepcke3.spigot.christmastime.weather

import edu.koepcke3.spigot.christmastime.Main
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WeatherCommandHandler(override val plugin: Main) : edu.koepcke3.spigot.Command(plugin) {
    private val commands: Set<String> = setOf("christmasweather")

    override fun getCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): () -> Boolean {
        if(args.isEmpty() || args[0] !in Commands.values().map{cmd -> cmd.name} ) {
            sender.sendMessage("invalid command name! command name: ${args[0]}")
            return { false }
        }
        return {
            val argsList = args.toList()
            val cmdName = argsList[0]
            val res = Commands.valueOf(cmdName).parseArgs(plugin, sender, args.drop(1))
            sender.sendMessage(res.second)
            res.first
        }
    }

    override fun getCommandNames(): Set<String> {
        return commands //default upper
    }

    enum class Commands : CommandParser {
        SET_WEATHER {
            override fun parseArgs(plugin: Main, sender: CommandSender, args: List<String>): Pair<Boolean, String> {
                if(!sender.isOp) {
                    return Pair(false, "this command requires op permissions!")
                }
                val requiredArgs = mapOf(
                        Pair("weatherToSet", 0)
                )
                if(args.size != requiredArgs.size) {
                    return Pair(false, "expected arguments size of ${requiredArgs.size} but got ${args.size} args instead")
                }
                val weatherToSet = args[requiredArgs["weatherToSet"] ?: error("checked arg not found!!")]
                return try {
                    val weather = WeatherSim.Weather.valueOf(weatherToSet)
                    plugin.setWeather(weather)
                    Pair(true, "set weather to $weather")
                } catch(e: IllegalArgumentException) {
                    Pair(false, "invalid weather!")
                }
            }
        },
        TOGGLE_WEATHER {
            override fun parseArgs(plugin: Main, sender: CommandSender, args: List<String>): Pair<Boolean, String> {
                if(!sender.isOp) {
                    return Pair(false, "this command requires op permissions!")
                }
                plugin.toggleWeatherCycle()
                return Pair(true, "weather cycle toggled")
            }
        },
        ADD_MUSICBLACKLIST {
            override fun parseArgs(plugin: Main, sender: CommandSender, args: List<String>): Pair<Boolean, String> {
                if(!sender.isOp && sender.name != args[0]) {
                    return Pair(false, "must be op to add another player to blacklist!")
                }
                val player: Player = Bukkit.getPlayer(args[0]) ?: return Pair(false, "could not find player ${args[0]}!")
                MusicHandler.blacklistPlayer(player)
                return Pair(true, "added ${args[0]} to blacklist")
            }
        },
        REMOVE_MUSICBLACKLIST {
            override fun parseArgs(plugin: Main, sender: CommandSender, args: List<String>): Pair<Boolean, String> {
                if(!sender.isOp && sender.name != args[0]) {
                    return Pair(false, "must be op to remove another player to blacklist!")
                }
                val player: Player = Bukkit.getPlayer(args[0]) ?: return Pair(false, "could not find player ${args[0]}!")
                MusicHandler.unblacklistPlayer(player)

                return Pair(true, "removed ${args[0]} to blacklist")
            }
        },
    }

}