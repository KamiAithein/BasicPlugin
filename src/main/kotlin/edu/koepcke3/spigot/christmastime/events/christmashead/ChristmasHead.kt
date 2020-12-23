package edu.koepcke3.spigot.christmastime.events.christmashead

import edu.koepcke3.spigot.CustomSkull
import edu.koepcke3.spigot.christmastime.Main
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class ChristmasHead(val plugin: Main) {
    data class Head(val entityType: EntityType, val itemStack: ItemStack)

    fun makeHeadPair(entityType: EntityType, encodedUrl: String): Pair<EntityType, Head> {
        val skull = CustomSkull.createSkull(encodedUrl)
        val skullMeta = skull.itemMeta

        skullMeta?.lore = listOf("CHRISTMASHEAD", entityType.name)

        skull.itemMeta = skullMeta
        return Pair(entityType, Head(entityType, skull))
    }
    val CREATURE_HEADS: Map<EntityType, Head> = mapOf(
            //W=100 kill 5 head
            makeHeadPair(
                    EntityType.WITHER_SKELETON,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2IzYzUwMTFiMDliODczNWJhNzgwZTQ5MzA3ODQ5MGVkMzg3OTY5YjgyYmY1ZTc0MzRlYjI5MTA2MzM0YzhiYyJ9fX0="
            ),
            //D=100 kill 5 head
            makeHeadPair(
                    EntityType.DROWNED,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBjMjYwZDNjMjlhOTYwNzUzOGVlZjE1N2I2NDMxMTM2MDQwZGRiZmIyM2E4NTJmNjMyNjMxZWJmNjZlNzIxNyJ9fX0="
            ),
            //A=200 kill 10 head
            makeHeadPair(
                    EntityType.HUSK,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU1NWM0NjAxMDMyNzczOTM0NzBlMjY4NTg4MDY1NjRmYmExNDI3YjcyZWRmNzNjZjE3NjQzZjE5ZGEyZDFkZSJ9fX0="
            ),
            makeHeadPair(
                    EntityType.STRAY,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5NDI1NWJhNzY0ZDcyOGMwNzQwMGY4YmY4NjE5NWVkOGJiNTUyYjQ4NTc1YzQxOWYyNTY5N2RmNmQwN2E2MyJ9fX0="
            ),
            makeHeadPair(
                    EntityType.ZOMBIFIED_PIGLIN,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDkwZTJkNWRlYzM5MWNmY2ZhYTUzMTVkN2Q5ODdjZjY1YzdlYjM0YTA5ZWRkNjU5OWFmZDE5MDEzNjJmNDNkNiJ9fX0="
            ),
            //R=500 kills 25 heads
            makeHeadPair(
                    EntityType.SKELETON,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzOWIyZDE2N2JkNGFjOGE2YmFjNjFlYmU2ZmEyYWM3YjE1M2U1NDg1N2RkZTJmODZiZDA1ZDk5ZTA5ODFkIn19fQ=="
            ),
            makeHeadPair(
                    EntityType.ZOMBIE,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJkNzU3Yjc1NDk1ZDdhNDdiZGRhNWZlNzFjZDk2Y2JmZDExYWVmMTIyNWM4NmJhZGI3NjJiZWM5MDIxNDQifX19"
            )
    )
}