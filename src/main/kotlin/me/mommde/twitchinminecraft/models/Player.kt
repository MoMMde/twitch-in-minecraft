package me.mommde.twitchinminecraft.models

import org.bukkit.entity.Player

private var timPlayers = mutableMapOf<Player, TimPlayer>()

val Player.tim: TimPlayer
    get() = timPlayers[this] ?: TimPlayer().also { timPlayers[this] = it }

data class TimPlayer(
    val channelsListeningChatMessages: MutableList<String> = mutableListOf(),
    val channelsListeningNotificationInfo: MutableList<String> = mutableListOf()
)
