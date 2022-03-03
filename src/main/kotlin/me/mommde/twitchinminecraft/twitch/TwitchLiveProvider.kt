package me.mommde.twitchinminecraft.twitch

import com.github.twitch4j.TwitchClient
import com.github.twitch4j.events.ChannelGoLiveEvent
import me.mommde.twitchinminecraft.literalTimMessage
import me.mommde.twitchinminecraft.models.tim
import me.mommde.twitchinminecraft.neutralChatColor
import me.mommde.twitchinminecraft.positiveColor
import net.axay.kspigot.extensions.onlinePlayers
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.entity.Player

class TwitchLiveProvider(
    twitchClient: TwitchClient
) {
    init {
        twitchClient.eventManager.onEvent(ChannelGoLiveEvent::class.java) { event ->
            channelGoesLive(event.channel.name)
        }
    }

    fun registerChannels(player: Player, vararg channels: String) {
        val activeChannels = channelsListeningActivity(player)
        channelsListeningActivity(player).addAll(
            channels.filter {
                it !in activeChannels
            }
        )
    }

    fun unregisterChannels(player: Player, vararg channels: String) {
        val activeChannels = channelsListeningActivity(player)
        channelsListeningActivity(player).removeAll(activeChannels)
    }

    private fun channelGoesLive(channel: String) {
        for (player in onlinePlayers) {
            val tim = player.tim
            if (tim.channelsListeningNotificationInfo.isNullOrEmpty()) continue
            val channels = tim.channelsListeningNotificationInfo
            if (channel in channels) {
                player.sendMessage(buildLiveMessage(channel) as Component)
            }
        }
    }
    private fun channelsListeningActivity(player: Player) = player.tim.channelsListeningNotificationInfo

    private fun buildLiveMessage(channel: String) = literalTimMessage("$channel just went Live!") {
        color = neutralChatColor
        clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitch.tv/$channel")
        text(" [Click]") {
            color = positiveColor
            this.clickEvent = this@literalTimMessage.clickEvent
        }
    }
}
