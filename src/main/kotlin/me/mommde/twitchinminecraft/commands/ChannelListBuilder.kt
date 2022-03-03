package me.mommde.twitchinminecraft.commands

import me.mommde.twitchinminecraft.literalTimMessage
import me.mommde.twitchinminecraft.models.tim
import me.mommde.twitchinminecraft.neutralChatColor
import me.mommde.twitchinminecraft.neutralDarkChatColor
import me.mommde.twitchinminecraft.twitch.TwitchChannelSubscription
import me.mommde.twitchinminecraft.twitch.getSubscriptionType
import net.axay.kspigot.chat.KColors
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

fun buildListTextComponent(player: Player): TextComponent {
    val totalNotification = player.tim.channelsListeningNotificationInfo
    val totalChat = player.tim.channelsListeningChatMessages
    return literalTimMessage("Here is a list of Channels you subscribed") {
        newLine()
        text("> You see the Chat of ${totalChat.size} channels.") {
            color = neutralChatColor
        }
        newLine()
        text("> You see the Live Notification of ${totalNotification.size} channels.") {
            color = neutralChatColor
        }
        newLine()
        text("▣ Notification") {
            color = KColors.BLUEVIOLET
        }
        text(" | ") {
            color = neutralDarkChatColor
            bold = true
        }
        text("▣ Chat") {
            color = KColors.YELLOW
        }
        text(" | ") {
            color = neutralDarkChatColor
            bold = true
        }
        text("▣ Both") {
            color = KColors.GREEN
        }
        newLine()
        newLine()
        getSubscriptionType(totalNotification, totalChat).forEach { (channel, type) ->
            val coloredChannelSubscription = when (type) {
                TwitchChannelSubscription.BOTH -> KColors.GREEN
                TwitchChannelSubscription.CHAT -> KColors.YELLOW
                TwitchChannelSubscription.NOTIFICATION -> KColors.BLUEVIOLET
            }
            text(" ▣ ") {
                color = coloredChannelSubscription
                bold = true
            }
            text(" $channel") {
                color = coloredChannelSubscription
                hoverText(type.description)
            }
            newLine()
        }
    }
}
