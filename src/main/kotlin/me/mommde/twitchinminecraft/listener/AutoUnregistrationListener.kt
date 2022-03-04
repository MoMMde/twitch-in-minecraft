package me.mommde.twitchinminecraft.listener

import me.mommde.twitchinminecraft.TwitchInMinecraftPlugin
import me.mommde.twitchinminecraft.models.tim
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class AutoUnregistrationListener(
    private val plugin: TwitchInMinecraftPlugin
) : Listener {
    @EventHandler
    fun onClose(event: PlayerQuitEvent) {
        val player = event.player
        val tim = event.player.tim
        if (tim.channelsListeningNotificationInfo.isEmpty() && tim.channelsListeningChatMessages.isEmpty()) return
        plugin.twitchChatProvider.unregisterChannels(player, *tim.channelsListeningChatMessages.toTypedArray())
        plugin.twitchLiveProvider.unregisterChannels(player, *tim.channelsListeningNotificationInfo.toTypedArray())
    }
}
