package me.arasple.mc.trmenu.module.internal.database

import me.arasple.mc.trmenu.TrMenu
import org.bukkit.entity.Player
import taboolib.library.configuration.FileConfiguration

/**
 * @Author sky
 * @Since 2020-08-14 14:38
 */
abstract class Database {

    val index: PlayerIndex
        get() = PlayerIndex.of(TrMenu.SETTINGS.getString("Database.Index.Player", "USERNAME")!!)

    fun pull(player: Player): FileConfiguration {
        return pull(player, if (index == PlayerIndex.UUID) player.uniqueId.toString() else player.name)
    }

    abstract fun pull(player: Player, indexPlayer: String): FileConfiguration

    fun push(player: Player) {
        push(player, if (index == PlayerIndex.UUID) player.uniqueId.toString() else player.name)
    }

    abstract fun push(player: Player, indexPlayer: String)

    fun release(player: Player) {
        release(player, if (index == PlayerIndex.UUID) player.uniqueId.toString() else player.name)
    }

    abstract fun release(player: Player, indexPlayer: String)

}