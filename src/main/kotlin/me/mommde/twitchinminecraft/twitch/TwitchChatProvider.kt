package me.mommde.twitchinminecraft.twitch

import com.github.twitch4j.TwitchClient
import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import me.mommde.twitchinminecraft.literalTimMessage
import me.mommde.twitchinminecraft.models.tim
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.onlinePlayers
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.entity.Player
import java.util.Optional

class TwitchChatProvider(
    private val twitchClient: TwitchClient
) {
    init {
        twitchClient.eventManager.onEvent(IRCMessageEvent::class.java) { event ->
            val channel = event.channel.name
            if (event.message != Optional.empty<String>()) {
                val message = event.message.get()
                onlinePlayers.filter { it.tim.channelsListeningChatMessages.map { timChannel -> timChannel.lowercase() }.contains(channel.lowercase()) }.forEach { player: Player ->
                    player.sendMessage(
                        literalTimMessage("#") {
                            color = KColors.DARKGRAY
                            text(channel) {
                                color = twitchColors[channel.lowercase()] ?: KColors.GOLD
                                italic = true
                            }
                            text(" ${event.userName}") {
                                color = KColors.GRAY
                                clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.twitch.tv/popout/$channel/viewercard/${event.userName}")
                            }
                            text(": ") {
                                color = KColors.GRAY
                                italic = true
                            }
                            text(message) {
                                color = KColors.WHITE
                            }
                            clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitch.tv/$channel")
                        }
                    )
                }
            }
        }
    }

    fun registerChannels(player: Player, vararg channels: String) {
        val tim = player.tim
        tim.channelsListeningChatMessages.addAll(
            channels.filter {
                it !in tim.channelsListeningChatMessages
            }
        )
        updateAttachedChannels(twitchClient)
    }

    fun unregisterChannels(player: Player, vararg channels: String) {
        val tim = player.tim
        tim.channelsListeningChatMessages.removeAll(
            channels.filter {
                it in tim.channelsListeningChatMessages
            }
        )
        updateAttachedChannels(twitchClient)
    }
}
