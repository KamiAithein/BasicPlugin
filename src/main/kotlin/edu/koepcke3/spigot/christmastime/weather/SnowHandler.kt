package edu.koepcke3.spigot.christmastime.weather

import org.bukkit.*
import org.bukkit.block.Biome
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

object SnowHandler {
    object Debug {
        /**
         * Update snow location to be around player. Only works with one player
         */
        var updateLoc: Boolean = true
        lateinit var lastLoc: Location
    }
    const val SNOW_FALL_RAY_LEN = 100.0
    const val SNOW_FALL_RATE = 0.07
    val SNOW_PARTICLE_V = Vector(0.0, -0.2, 0.0)
    val INVALID_HIT_MATS = setOf(
            Material.SNOW,
            Material.GRASS_PATH,
            Material.STONECUTTER,
            Material.BREWING_STAND,
            Material.ENCHANTING_TABLE,
            Material.CAMPFIRE,
            Material.SOUL_CAMPFIRE,
            Material.LANTERN,
            Material.SOUL_LANTERN,
            Material.BAMBOO,
            Material.BAMBOO_SAPLING,
            Material.CACTUS) +
            Tag.SLABS.values +
            Tag.BEDS.values +
            Tag.FENCES.values +
            Tag.FENCE_GATES.values +
            Tag.STAIRS.values +
            Tag.TRAPDOORS.values +
            Tag.SIGNS.values +
            Tag.BANNERS.values +
            Tag.PRESSURE_PLATES.values +
            Tag.WALLS.values +
            Tag.BUTTONS.values +
            Tag.CROPS.values +
            Tag.DOORS.values +
            Tag.FLOWER_POTS.values +
            Tag.RAILS.values +
            Tag.ICE.values

    val VALID_REPLACE_MATS = setOf(Material.GRASS, Material.TALL_GRASS, Material.AIR, Material.CAVE_AIR)

    val INVALID_BIOMES = setOf(Biome.JUNGLE, Biome.DESERT, Biome.SAVANNA, Biome.SWAMP, Biome.DESERT_HILLS, Biome.DESERT_LAKES)

    /**
     * Gets random location around a given point (Location) and returns a location within the given r of that point
     *  Y has custom radius not necessarily centered at player
     */
    private fun getRandomLocationAround(loc: Location, r: Int, rY: Int, yOffset: Int): Location {
        val randWithCenter = {center: Int -> center + Math.random()*r - r/2}
        return Location(
            loc.world,
            randWithCenter(loc.blockX),
            loc.blockY + Math.random()*rY - rY/2 - yOffset,
            randWithCenter(loc.blockZ)
        )
    }
    private fun isShouldSnow(loc: Location, forceSnow: Boolean): Boolean {
        val world = loc.world

        if(world?.environment != World.Environment.NORMAL) {
            return false //should only snow in overworld
        }
        if(!forceSnow && loc.block.biome in INVALID_BIOMES) {
            return false //should not snow in invalid biomes
        }

        val shouldSnowRay = world.rayTraceBlocks(
            Location(world, loc.x, loc.y, loc.z),
            Vector(0,1,0),
            200.0,
            FluidCollisionMode.ALWAYS
        )
        //snow iff nothing above point blocking
        return forceSnow || shouldSnowRay == null || shouldSnowRay.hitBlock == null
    }
    private fun makeSnowParticle(loc: Location, vel: Vector, type: Particle) {
        loc.world?.spawnParticle(
            type,
            loc.x,
            loc.y,
            loc.z,
            0, //when 0 offset accounts for vel vector
            vel.x,
            vel.y,
            vel.z
        )
    }
    fun snowOn(player: Entity,
               nParticles: Int = 1000,
               percentSnowFall: Double = SNOW_FALL_RATE,
               snowParticleV: Vector = SNOW_PARTICLE_V,
               isSnowStorm: Boolean = false,
               r: Int = 75, rY: Int = 30, yOffset: Int = -5,
               forceSnow: Boolean = false) {
        //create n number of particles
        for (i in 0..nParticles) {

            val snowLoc = if(Debug.updateLoc) { //default true
                val nextLoc = getRandomLocationAround(player.location, r, rY, yOffset)
                Debug.lastLoc = nextLoc
                nextLoc
            }
            else {
                Debug.lastLoc
            }

            if(!isShouldSnow(snowLoc, forceSnow)) {
                continue
            }

            makeSnowParticle(snowLoc, snowParticleV, Particle.FIREWORKS_SPARK)

            val snowPath = snowLoc.world?.rayTraceBlocks(
                Location(snowLoc.world, snowLoc.x, snowLoc.y, snowLoc.z),
                snowParticleV,
                    SNOW_FALL_RAY_LEN,
                FluidCollisionMode.ALWAYS,
                false
            )
            //snow spawns randomly; is this random success choice?
            val randSucc = Math.random() < percentSnowFall
            if(!randSucc || snowPath == null || snowPath.hitBlock == null) {
                continue
            }

            val hit: Block = snowPath.hitBlock!! //checked above

            /** Extend block class to be easy to use in this instance by exposing various attributes */
            class BlockExt(val loc: Location, val world: World, val mat: Material, val block: Block) {
                /** is this block valid for replacement with snow */
                fun isValidReplAtrib(): Boolean = this.block.type.isSolid && !this.block.isLiquid
                /** set this block mat to replMat */
                fun replMat(replMat: Material) { this.block.type = replMat }

                fun isTooHot(): Boolean = this.block.lightFromBlocks >= 12
            }

            val (x, y, z) = listOf(hit.x.toDouble(), hit.y.toDouble(), hit.z.toDouble())

            val extHit = hit.location.let{ locHit ->
                BlockExt(locHit, hit.world, locHit.block.blockData.material, locHit.block)
            }
            val extAbove = Location(hit.world, x, y + 1, z).let { locAbove ->
                BlockExt(locAbove, hit.world, locAbove.block.blockData.material, locAbove.block)
            }

            //things we want to replace
            val validReplaceMats = VALID_REPLACE_MATS
            //things we cant put snow on
            val invalidHitMats = INVALID_HIT_MATS

            val isTooHot =  (extAbove.isTooHot() || extHit.isTooHot())
            if(!isSnowStorm && isTooHot) {
                continue
            }

            val isHitValidForSnow = extHit.mat !in invalidHitMats && extHit.isValidReplAtrib()
            //hit a block
            if(extAbove.mat in validReplaceMats && isHitValidForSnow){
                extAbove.replMat(Material.SNOW)
            }
            //if hit something not a block and we want to replace it
            else if (extHit.mat in validReplaceMats) {
                extHit.replMat(Material.SNOW)
            }
        }
    }
}