package me.mommde.twitchinminecraft.commands

import me.mommde.twitchinminecraft.TwitchInMinecraftPlugin
import me.mommde.twitchinminecraft.literalTimMessage
import me.mommde.twitchinminecraft.models.tim
import me.mommde.twitchinminecraft.neutralChatColor
import me.mommde.twitchinminecraft.neutralDarkChatColor
import me.mommde.twitchinminecraft.twitch.TwitchChannelSubscription
import me.mommde.twitchinminecraft.twitch.getSubscriptionType
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.literal
import net.axay.kspigot.commands.runs

class TwitchInMinecraftCommand(
    val twitchInMinecraftPlugin: TwitchInMinecraftPlugin
) {

    fun register() = command("tim", true) {
        literal("list") {
            runs {
                player.sendMessage(buildListTextComponent(player))
            }
        }
    }
}
