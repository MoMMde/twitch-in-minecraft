package me.mommde.twitchinminecraft

import com.github.twitch4j.TwitchClientBuilder
import me.mommde.twitchinminecraft.commands.TwitchInMinecraftCommand
import me.mommde.twitchinminecraft.listener.AutoUnregistrationListener
import me.mommde.twitchinminecraft.twitch.TwitchChatProvider
import me.mommde.twitchinminecraft.twitch.TwitchLiveProvider
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit

class TwitchInMinecraftPlugin : KSpigot() {
    val twitch = TwitchClientBuilder.builder()
        .withEnableHelix(true)
        .withEnableChat(true)
        .build()
    val twitchLiveProvider = TwitchLiveProvider(twitch)
    val twitchChatProvider = TwitchChatProvider(twitch)

    override fun load() {
        slF4JLogger.debug("Load function called on Twitch in Minecraft Plugin")
    }

    override fun startup() {
        TwitchInMinecraftCommand(this).register()
        Bukkit.getPluginManager().registerEvents(AutoUnregistrationListener(this), this)
    }

    override fun shutdown() {
        twitch.close()
    }
}
