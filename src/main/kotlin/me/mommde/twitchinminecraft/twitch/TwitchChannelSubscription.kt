package me.mommde.twitchinminecraft.twitch

import com.github.twitch4j.TwitchClient
import me.mommde.twitchinminecraft.models.tim
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.main.KSpigotMainInstance
import net.md_5.bungee.api.ChatColor

enum class TwitchChannelSubscription(val description: String) {

    // Todo: implement a language file
    NOTIFICATION("You'll be getting updates when the Channel turns on/off its livestream."),
    CHAT("You get a notification whenever a Chat Message is sent in the Channels livestream chat."),
    BOTH("You'll get Notification on turning on/off the livestream as so on new Messages in the Channels livestream chat.")
}

fun getSubscriptionType(notifications: List<String>, chat: List<String>): Map<String, TwitchChannelSubscription> {
    return buildMap {
        (notifications + chat).forEach { channel ->
            if (channel in notifications && channel in chat) put(channel, TwitchChannelSubscription.BOTH)
            if (channel in notifications && channel !in chat) put(channel, TwitchChannelSubscription.NOTIFICATION)
            if (channel !in notifications && channel in chat) put(channel, TwitchChannelSubscription.CHAT)
        }
    }
}

val twitchColors = mutableMapOf<String, ChatColor>()

fun updateAttachedChannels(twitchClient: TwitchClient) {
    val totalChats = mutableListOf<String>()
    val twitchChat = twitchClient.chat
    for(player in onlinePlayers) {
        val tim = player.tim
        if (tim.channelsListeningChatMessages.isEmpty() && tim.channelsListeningNotificationInfo.isEmpty()) continue
        totalChats.addAll (tim.channelsListeningChatMessages + tim.channelsListeningNotificationInfo)
    }
    for (chat in totalChats) {
        if (!twitchChat.isChannelJoined(chat)) {
            twitchChat.joinChannel(chat)
            twitchColors[chat.lowercase()] =
                KColors::class.java.declaredFields.random().get(ChatColor::class.java) as ChatColor
        }
    }
    twitchChat.channels.filter { twitchChannel ->
        twitchChannel.lowercase() !in totalChats.map { tChat -> tChat.lowercase() }
    }.forEach { twitchChannel ->
        twitchClient.chat.leaveChannel(twitchChannel)
        if (twitchColors.contains(twitchChannel.lowercase())) twitchColors.remove(twitchChannel.lowercase())
    }
    KSpigotMainInstance.slF4JLogger.info("Updated attached Twitch Channels. Total: ${twitchClient.chat.channels.size}")
}
