package me.mommde.twitchinminecraft.twitch

enum class TwitchChannelSubscription(val description: String) {

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
