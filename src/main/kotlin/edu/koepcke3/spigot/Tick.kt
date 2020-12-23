package edu.koepcke3.spigot

object Tick {
    val TICK_IN_SEC = 20L
    fun secToTick(sec: Int): Long {
        return sec* TICK_IN_SEC
    }
    fun minToTick(min: Int): Long {
        return secToTick(60*min)
    }

}