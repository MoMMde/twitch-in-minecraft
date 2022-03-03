package me.mommde.twitchinminecraft.models

import org.bukkit.entity.Player

private var timPlayers = mutableMapOf<Player, TimPlayer>()

val Player.tim: TimPlayer
    get() = timPlayers[this] ?: TimPlayer()

data class TimPlayer(
    val channelsListeningChatMessages: MutableList<String> = mutableListOf("Papaplatte", "TjanTV", "MoMMde"),
    val channelsListeningNotificationInfo: MutableList<String> = mutableListOf("Renter88", "Faisterino", "TjanTV", "Amouranth")
)
