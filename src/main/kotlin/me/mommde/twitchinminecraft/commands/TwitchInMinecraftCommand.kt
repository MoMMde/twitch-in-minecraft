package me.mommde.twitchinminecraft.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import me.mommde.twitchinminecraft.TwitchInMinecraftPlugin
import me.mommde.twitchinminecraft.literalTimMessage
import me.mommde.twitchinminecraft.models.tim
import me.mommde.twitchinminecraft.negativeColor
import me.mommde.twitchinminecraft.neutralChatColor
import me.mommde.twitchinminecraft.neutralDarkChatColor
import me.mommde.twitchinminecraft.positiveColor
import me.mommde.twitchinminecraft.twitch.TwitchChannelSubscription
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.commands.CommandContext
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.literal
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.axay.kspigot.main.KSpigotMainInstance
import net.md_5.bungee.api.chat.ClickEvent
import net.minecraft.commands.CommandSourceStack
import org.bukkit.entity.Player

enum class ChannelConnectionType {
    JOIN, QUIT
}

class TwitchInMinecraftCommand(
    private val twitchInMinecraftPlugin: TwitchInMinecraftPlugin
) {

    fun register() = command("tim", true) {
        literal("list") {
            argument("filter", StringArgumentType.word()) {
                runs {
                    val filter = getArgument<String>("filter")
                    if (TwitchChannelSubscription.values().map { it.name }.contains(filter.uppercase())) {
                        player.sendMessage(
                            buildListTextComponent(
                                player,
                                TwitchChannelSubscription.valueOf(filter.uppercase())
                            )
                        )
                    } else {
                        player.sendMessage(
                            literalTimMessage("Invalid Filter! Using none...") {
                                color = negativeColor
                            }
                        )
                        player.sendMessage(buildListTextComponent(player))
                    }
                }
            }
            runs {
                player.sendMessage(buildListTextComponent(player))
            }
        }
        literal("join_notification") {
            argument("channels", StringArgumentType.string()) {
                runs {
                    executeCommand(
                        TwitchChannelSubscription.NOTIFICATION,
                        ChannelConnectionType.JOIN,
                        getArgument<String>("channels")
                    ).invoke(this@command)
                }
            }
        }
        literal("quit_notification") {
            argument("channels", StringArgumentType.string()) {
                runs {
                    executeCommand(
                        TwitchChannelSubscription.NOTIFICATION,
                        ChannelConnectionType.QUIT,
                        getArgument<String>("channels")
                    ).invoke(this@command)
                }
            }
        }
        literal("join_chat") {
            argument("channels", StringArgumentType.string()) {
                runs {
                    executeCommand(
                        TwitchChannelSubscription.CHAT,
                        ChannelConnectionType.JOIN,
                        getArgument("channels")
                    ).invoke(this@command)
                }
            }
        }
        literal("quit_chat") {
            argument("channels", StringArgumentType.string()) {
                runs {
                    executeCommand(
                        TwitchChannelSubscription.CHAT,
                        ChannelConnectionType.QUIT,
                        getArgument("channels")
                    ).invoke(this@command)
                }
            }
        }
        literal("join_both") {
            argument("channels", StringArgumentType.string()) {
                runs {
                    executeCommand(
                        TwitchChannelSubscription.BOTH,
                        ChannelConnectionType.JOIN,
                        getArgument("channels")
                    ).invoke(this@command)
                }
            }
        }
        literal("quit_both") {
            argument("channels", StringArgumentType.string()) {
                runs {
                    executeCommand(
                        TwitchChannelSubscription.BOTH,
                        ChannelConnectionType.QUIT,
                        getArgument("channels")
                    ).invoke(this@command)
                }
            }
        }
        literal("admin") {
            requiresPermission("tim.admin")
            literal("channels") {
                runs {
                    val connections = twitchInMinecraftPlugin.twitch.chat.channels
                    player.sendMessage(
                        literalTimMessage("Connected to: ${connections.size}; ") {
                            connections.forEachIndexed { index, channel ->
                                text("${index + 1}. $channel") {
                                    color = KColors.GREEN
                                }
                                if (index < connections.size - 1) {
                                    text(", ") {
                                        color = neutralDarkChatColor
                                    }
                                }
                            }
                        }
                    )
                }
            }
            literal("reconnect") {
                runs {
                    twitchInMinecraftPlugin.twitch.chat.reconnect()
                    player.sendMessage(
                        literalTimMessage("Reconnected!") {
                            bold = true
                        }
                    )
                }
            }
        }
    }

    private fun CommandContext.executeCommand(
        subscription: TwitchChannelSubscription,
        type: ChannelConnectionType,
        channelsArgumentDefault: String
    ): ArgumentBuilder<CommandSourceStack, *>.() -> Unit {
        return {
            val channels = mutableListOf<String>()
            val channelsArgument = channelsArgumentDefault.replace(",", "")
            channelsArgument.split(' ').forEach(channels::add)
            when (subscription) {
                TwitchChannelSubscription.BOTH -> {
                    executeCommand(TwitchChannelSubscription.CHAT, type, channelsArgumentDefault).invoke(this)
                    executeCommand(TwitchChannelSubscription.NOTIFICATION, type, channelsArgumentDefault).invoke(this)
                }
                TwitchChannelSubscription.NOTIFICATION -> {
                    when (type) {
                        ChannelConnectionType.JOIN -> joinNotificationChannel(player, channels)
                        ChannelConnectionType.QUIT -> quitNotificationChannel(player, channels)
                    }
                }
                TwitchChannelSubscription.CHAT -> {
                    when (type) {
                        ChannelConnectionType.JOIN -> joinChatChannel(player, channels)
                        ChannelConnectionType.QUIT -> quitChatChannel(player, channels)
                    }
                }
            }
        }
    }

    private fun joinNotificationChannel(player: Player, channels: List<String>) {
        KSpigotMainInstance.slF4JLogger.debug("Registering notification channels: $channels for user: ${player.name}")
        twitchInMinecraftPlugin.twitchLiveProvider.registerChannels(player, *channels.toTypedArray())
        player.sendMessage(
            literalTimMessage("You will now be notified when these channels go live.") {
                newLine()
                text("Total: ") {
                    color = neutralChatColor
                }
                text(player.tim.channelsListeningNotificationInfo.size.toString()) {
                    color = positiveColor
                    bold = true
                }
                text(" [Click]") {
                    color = KColors.GREEN
                    clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tim list NOTIFICATION")
                }
            }
        )
    }

    private fun quitNotificationChannel(player: Player, channels: List<String>) {
        KSpigotMainInstance.slF4JLogger.debug("Unregistering notification channels: $channels for user: ${player.name}")
        twitchInMinecraftPlugin.twitchLiveProvider.unregisterChannels(player, *channels.toTypedArray())
        player.sendMessage(
            literalTimMessage("You will now be notified when these channels go live.") {
                newLine()
                text("Total: ") {
                    color = neutralChatColor
                }
                text(player.tim.channelsListeningNotificationInfo.size.toString()) {
                    color = positiveColor
                    bold = true
                }
                text(" [Click]") {
                    color = KColors.GREEN
                    clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tim list NOTIFICATION")
                }
            }
        )
    }

    private fun joinChatChannel(player: Player, channels: List<String>) {
        KSpigotMainInstance.slF4JLogger.info("Registering chat channels: $channels for user: ${player.name}")
        twitchInMinecraftPlugin.twitchChatProvider.registerChannels(player, *channels.toTypedArray())
        player.sendMessage(
            literalTimMessage("You will now be notified when there is a new message in these Channels.") {
                newLine()
                text("Total: ") {
                    color = neutralChatColor
                }
                text(player.tim.channelsListeningChatMessages.size.toString()) {
                    color = positiveColor
                    bold = true
                }
                text(" [Click]") {
                    color = KColors.GREEN
                    clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tim list CHAT")
                }
            }
        )
    }

    private fun quitChatChannel(player: Player, channels: List<String>) {
        KSpigotMainInstance.slF4JLogger.info("Unregistering chat channels: $channels for user: ${player.name}")
        twitchInMinecraftPlugin.twitchChatProvider.unregisterChannels(player, *channels.toTypedArray())
        player.sendMessage(
            literalTimMessage("You will now be notified when there is a new message in these Channels.") {
                newLine()
                text("Total: ") {
                    color = neutralChatColor
                }
                text(player.tim.channelsListeningChatMessages.size.toString()) {
                    color = positiveColor
                    bold = true
                }
                text(" [Click]") {
                    color = KColors.GREEN
                    clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tim list CHAT")
                }
            }
        )
    }
}
