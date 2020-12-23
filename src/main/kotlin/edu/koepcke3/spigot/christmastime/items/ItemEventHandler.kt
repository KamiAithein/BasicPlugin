package edu.koepcke3.spigot.christmastime.items

import edu.koepcke3.spigot.Tick
import edu.koepcke3.spigot.christmastime.Main
import edu.koepcke3.spigot.christmastime.weather.SnowHandler
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ItemEventHandler(private val plugin: Main) : Listener {
    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val projectile = event.entity
        if(projectile is Snowball) {
            val hit = event.hitEntity
            if(hit is Player) {
                if(!hit.isBlocking && hit.gameMode == GameMode.ADVENTURE || hit.gameMode == GameMode.SURVIVAL) {
                    hit.damage(0.1)
                    hit.setVelocity(projectile.velocity.multiply(0.25))
                    hit.world.spawnParticle(Particle.SNOWBALL, hit.location, 100)
                }
            }
        }
        if(projectile is Arrow) {
            if(projectile.hasMetadata("FROSTY_BOW_ID")) {
                val meta = projectile.getMetadata("FROSTY_BOW_ID")
                if(meta.isNotEmpty()) {
                    Bukkit.getServer().scheduler.cancelTask(meta[0].asInt())
                }
                val hit = event.hitEntity
                if(hit != null) {
                    if(hit is LivingEntity && (hit !is Player || !hit.isBlocking)) {
                        val potionEffects = setOf(
                                PotionEffect(PotionEffectType.SLOW, Tick.secToTick(10).toInt(), 2),
                                PotionEffect(PotionEffectType.POISON, Tick.secToTick(5).toInt(), 1)
                        )
                        hit.addPotionEffects(potionEffects)
                    }
                }
                projectile.removeMetadata("FROSTY_BOW_ID", plugin)
            }
        }
    }
    @EventHandler
    fun onEntityShootBowEvent(event: EntityShootBowEvent) {
        val bow = event.bow ?: return
        if(bow.itemMeta?.lore?.contains("CHRISTMASTIME:ICEBOW") == true) {
            val arrow = event.projectile
//            if(arrow.location.world?.environment == World.Environment.NETHER) {
//                return //if in nether, do nothing
//            }
            val id = Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(plugin, {arrowTickHandler(arrow)}, 0, 1L)
            arrow.setMetadata("FROSTY_BOW_ID", FixedMetadataValue(plugin, id))
        }
    }
    private fun arrowTickHandler(arrow: Entity) {
        SnowHandler.snowOn(arrow, 5, 1.0, r = 2, rY = 1, yOffset = 0, forceSnow = true)
    }
}