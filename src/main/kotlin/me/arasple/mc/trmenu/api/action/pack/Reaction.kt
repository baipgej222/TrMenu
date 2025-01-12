package me.arasple.mc.trmenu.api.action.pack

import me.arasple.mc.trmenu.api.action.Actions
import me.arasple.mc.trmenu.module.conf.prop.Property
import org.bukkit.entity.Player

/**
 * @author Rubenicos
 * @date 2021/11/23 14:59
 */
abstract class Reaction(val priority: Int) {

    abstract fun isEmpty(): Boolean

    abstract fun react(player: Player): Boolean

    companion object {

        fun of(priority: Int, any: Any): Reaction? {
            if (any is String || ((any is Map<*, *>) && any.entries.firstOrNull()?.key.toString().equals("catcher", true))) {
                return SingleReaction(priority, Actions.readAction(any))
            }

            val reaction = Property.asSection(any) ?: return null
            val keyPriority = Property.PRIORITY.getKey(reaction)
            val keyRequirement = Property.CONDITION.getKey(reaction)
            val keyActions = Property.ACTIONS.ofList(reaction)
            val keyDenyActions = Property.DENY_ACTIONS.ofList(reaction)

            return ConditionalReaction(
                reaction.getInt(keyPriority, priority),
                reaction.getString(keyRequirement, "")!!,
                Reactions.ofReaction(keyActions),
                Reactions.ofReaction(keyDenyActions)
            )
        }

    }
}