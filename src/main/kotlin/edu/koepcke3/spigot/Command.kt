package edu.koepcke3.spigot

import edu.koepcke3.spigot.christmastime.Main
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Supplier


/**
 * Basic Command structure
 */
abstract class Command(protected open val plugin: Main) : CommandExecutor {

    interface CommandParser {
        fun parseArgs(plugin: Main, sender: CommandSender, args: List<String>): Pair<Boolean, String>
    }
    /**
     * All commands are implemented by an enum that extends this interface
     */
    protected abstract fun getCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): () -> Boolean
    abstract fun getCommandNames(): Set<String>

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(command.name in getCommandNames()) {
            return getCommand(sender, command, label, args).invoke()
        }

        return false
    }
}