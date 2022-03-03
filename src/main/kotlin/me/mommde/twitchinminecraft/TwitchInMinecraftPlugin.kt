package me.mommde.twitchinminecraft

import com.github.twitch4j.TwitchClientBuilder
import me.mommde.twitchinminecraft.commands.TwitchInMinecraftCommand
import me.mommde.twitchinminecraft.twitch.TwitchLiveProvider
import net.axay.kspigot.main.KSpigot

class TwitchInMinecraftPlugin : KSpigot() {
    private val twitch = TwitchClientBuilder.builder()
        .withEnableHelix(true)
        .build()
    private val twitchLiveProvider = TwitchLiveProvider(twitch)

    override fun load() {
        slF4JLogger.debug("Load function called on Twitch in Minecraft Plugin")
    }

    override fun startup() {
        TwitchInMinecraftCommand(this).register()
    }

    override fun shutdown() {
        twitch.close()
    }
}
