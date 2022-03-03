package me.mommde.twitchinminecraft.twitch

import com.github.philippheuer.events4j.core.EventManager
import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import org.bukkit.entity.Player

class TwitchChatProvider(val player: Player, val eventManager: EventManager) {
    init {
        eventManager.onEvent(IRCMessageEvent::class.java) {
        }
    }

    fun handleChatMessage(chatMessage: IRCMessageEvent, players: List<Player>) {
    }
}
