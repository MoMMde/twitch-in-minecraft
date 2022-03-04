package me.mommde.twitchinminecraft.twitch

import com.github.twitch4j.TwitchClient
import com.github.twitch4j.events.ChannelGoLiveEvent
import com.github.twitch4j.events.ChannelGoOfflineEvent
import me.mommde.twitchinminecraft.literalTimMessage
import me.mommde.twitchinminecraft.models.tim
import me.mommde.twitchinminecraft.neutralChatColor
import me.mommde.twitchinminecraft.positiveColor
import net.axay.kspigot.extensions.onlinePlayers
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.entity.Player

class TwitchLiveProvider(
    private val twitchClient: TwitchClient
) {
    init {
        twitchClient.eventManager.onEvent(ChannelGoLiveEvent::class.java) { event ->
            channelGoesLive(event.channel.name)
        }
        twitchClient.eventManager.onEvent(ChannelGoOfflineEvent::class.java) { event ->
            channelGoesOffline(event.channel.name)
        }
    }

    fun registerChannels(player: Player, vararg channels: String) {
        val tim = player.tim
        tim.channelsListeningNotificationInfo.addAll(
            channels.filter {
                it !in tim.channelsListeningNotificationInfo
            }
        )
        updateAttachedChannels(twitchClient)
    }

    fun unregisterChannels(player: Player, vararg channels: String) {
        val tim = player.tim
        tim.channelsListeningNotificationInfo.removeAll(
            channels.filter {
                it in tim.channelsListeningNotificationInfo
            }
        )
        updateAttachedChannels(twitchClient)
    }

    private fun channelGoesLive(channel: String) {
        onlinePlayers.filter { channel.lowercase() in it.tim.channelsListeningNotificationInfo.map { it.lowercase() } }.forEach { player: Player ->
            player.sendMessage(buildLiveMessage(channel))
        }
    }

    private fun channelGoesOffline(channel: String) {
        onlinePlayers.filter { channel.lowercase() in it.tim.channelsListeningNotificationInfo.map { it.lowercase() } }.forEach { player: Player ->
            player.sendMessage(buildOfflineMessage(channel))
        }
    }

    private fun buildLiveMessage(channel: String) = literalTimMessage("$channel just went Live!") {
        color = neutralChatColor
        clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitch.tv/$channel")
        text(" [Click]") {
            color = positiveColor
            this.clickEvent = this@literalTimMessage.clickEvent
        }
    }

    private fun buildOfflineMessage(channel: String) = literalTimMessage("$channel just went Offline!") {
        color = neutralChatColor
        clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitch.tv/$channel")
        text(" [Click]") {
            color = positiveColor
            this.clickEvent = this@literalTimMessage.clickEvent
        }
    }
}
