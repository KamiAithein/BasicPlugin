package edu.koepcke3.spigot.christmastime.events.christmashead

import edu.koepcke3.spigot.christmastime.Main
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.CreatureSpawnEvent

class ChristmasHeadBlockEventHandler(private val plugin: Main) : Listener {

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val item = event.itemInHand
        val meta = item.itemMeta
        if(meta!!.hasLore() && meta.lore!!.contains("CHRISTMASHEAD") && !meta.lore!!.contains("TRADED")) {
            event.player.sendMessage("Cannot place mob dropped head, trade it in to place!")
            event.isCancelled = true
        }
    }
}