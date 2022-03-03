package me.mommde.twitchinminecraft

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.LiteralTextBuilder
import net.axay.kspigot.chat.col
import net.axay.kspigot.chat.literalText
import net.md_5.bungee.api.chat.ClickEvent

inline fun literalTimMessage(
    baseText: String = "",
    builder: LiteralTextBuilder.() -> Unit = { }
) = literalText {
    text("[TIM] ") {
        color = col("#6034b2")
        clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/MoMMde/twitch-in-minecraft")
        bold = true
    }
    text(baseText) {
        color = neutralChatColor
    }
    apply(builder)
}

public val positiveColor = col("#00e647")
public val negativeColor = col("#e6003a")

public val neutralDarkChatColor = col("#737373")
public val neutralChatColor = KColors.NAVAJOWHITE
