package me.mommde.twitchinminecraft

import net.axay.kspigot.main.KSpigotMainInstance

object Config {

    private fun <T> config(path: String, default: T): T {
        val config = KSpigotMainInstance.config
        if (!config.contains(path)) {
            config.addDefault(path, default)
            return default
        }
        return config.get(path) as T
    }

    val twitchChannelWritePrefix = config("twitch_channel_write_prefix", "#")
}
