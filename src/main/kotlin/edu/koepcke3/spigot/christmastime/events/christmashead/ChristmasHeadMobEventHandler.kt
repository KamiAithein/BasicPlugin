package edu.koepcke3.spigot.christmastime.events.christmashead

import edu.koepcke3.spigot.CustomSkull
import edu.koepcke3.spigot.christmastime.Main
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

class ChristmasHeadMobEventHandler(private val plugin: Main) : Listener {

    @EventHandler
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        if (event.entityType in plugin.christmasHead.CREATURE_HEADS) {
            val ent = event.entity
            ent.equipment?.helmet = plugin.christmasHead.CREATURE_HEADS[event.entityType]?.itemStack
        }
    }
}