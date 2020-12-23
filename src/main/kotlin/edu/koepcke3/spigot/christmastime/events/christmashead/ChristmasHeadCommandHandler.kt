package edu.koepcke3.spigot.christmastime.events.christmashead

import edu.koepcke3.spigot.CustomSkull
import edu.koepcke3.spigot.christmastime.Main
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.NumberFormatException


class ChristmasHeadCommandHandler(override val plugin: Main) : edu.koepcke3.spigot.Command(plugin) {
    private val commands: Set<String> = setOf("christmashead")

    override fun getCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): () -> Boolean {
        if(args.isEmpty() || args[0] !in Commands.values().map{cmd -> cmd.name} ) {
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
        TRADE {
            override fun parseArgs(plugin: Main, sender: CommandSender, args: List<String>): Pair<Boolean, String> {
                if(sender !is Player) {
                    return Pair(false, "Only entities can trade heads!")
                }
                val toTrade = sender.inventory.itemInMainHand
                val lore = toTrade.itemMeta?.lore

                val validTrade = lore?.contains("CHRISTMASHEAD") ?: false
                if(!validTrade) {
                    return Pair(false, "Please hold the correct christmashead you want to trade!")
                }
                if(lore!!.contains("TRADED")) {
                    return Pair(false, "Cannot trade traded item!!")
                }
                if(args.isEmpty()) {
                    return Pair(false, "Please enter in the correct base64 encoded url!")
                }
                sender.inventory.removeItem(toTrade)
                sender.updateInventory()
                val tradeFor = CustomSkull.createSkull(args[0])
                val tradeForMeta = tradeFor.itemMeta
                tradeFor.amount = toTrade.amount

                tradeForMeta?.lore = listOf("CHRISTMASHEAD", "TRADED")
                tradeFor.itemMeta = tradeForMeta

                sender.inventory.setItemInMainHand(tradeFor)
                sender.updateInventory()
                return Pair(true, "traded ${toTrade.amount}x christmas heads")

            }
        },
        GENERATE {
            override fun parseArgs(plugin: Main, sender: CommandSender, args: List<String>): Pair<Boolean, String> {
                if(sender !is Player) {
                    return Pair(false, "Only entities can trade heads!")
                }
                if(!sender.isOp) {
                    return Pair(false, "Must be op!")
                }
                if(args.isEmpty()) {
                    return Pair(false, "specify the skull to create!")
                }
                val toAdd = CustomSkull.createSkull(args[0])
                if(args.size > 1) {
                    try {
                        val count = Integer.parseInt(args[1])
                        toAdd.amount = count
                    }
                    catch(error: NumberFormatException) {
                        sender.sendMessage("invalid number format exception!")
                    }
                }
                val addTo = if(args.size < 3) {
                    sender
                } else {
                    plugin.server.getPlayer(args[2]) ?: return Pair(false, "could not find player ${args[2]}")
                }
                if(args.size >= 4) {
                    val loreList = args[3].split("\\n")
                    val toAddMeta = toAdd.itemMeta
                    toAddMeta?.lore = loreList

                    toAdd.itemMeta = toAddMeta
                }
                addTo.inventory.addItem(toAdd)
                addTo.updateInventory()
                return Pair(true, "added item")
            }
        },
        GIVE {
            override fun parseArgs(plugin: Main, sender: CommandSender, args: List<String>): Pair<Boolean, String> {
                if(!sender.isOp) {
                    return Pair(false, "must be op!")
                }
                if(args.isEmpty()) {
                    return Pair(false, "must indicate player")
                }
                val giveTo = plugin.server.getPlayer(args[0]) ?: return Pair(false, "could not find player!")
                if(args.size < 2) {
                    return Pair(false, "must indicate item!")
                }
                if(args[1] == "CHRISTMASTIME:ICEBOW") {
                    val bow = ItemStack(Material.BOW, 1)
                    bow.addUnsafeEnchantment(Enchantment.LOYALTY, 1)
                    val meta = bow.itemMeta
                    meta?.setDisplayName(ChatColor.BLUE.toString() + "Frosty's Revenge")
                    meta?.lore = listOf("Once a jolly Snowman,", "reduced to a puddle by global warming.", "His hatred seethes out of this bow", "","CHRISTMASTIME:ICEBOW")
                    bow.itemMeta = meta

                    giveTo.inventory.addItem(bow)
                    return Pair(true, "gave bow")
                }
                return Pair(false, "couldnt find item")
            }
        }


    }

}