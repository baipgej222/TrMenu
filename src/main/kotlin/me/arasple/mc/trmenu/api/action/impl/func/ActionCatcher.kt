package me.arasple.mc.trmenu.api.action.impl.func

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.module.conf.prop.Property
import me.arasple.mc.trmenu.module.internal.inputer.Inputer
import me.arasple.mc.trmenu.util.collections.CycleList
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 20:15
 */
class ActionCatcher(private val inputer: Inputer) : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        inputer.startInput(player.session())
    }

    companion object {

        val type = "type".toRegex()
        val start = "before|start".toRegex()
        val cancel = "cancel".toRegex()
        val end = "after|end".toRegex()
        val display = "display|name|title".toRegex()
        val bookContent = "content|book".toRegex()
        val itemLeft = "item-?left".toRegex()
        val itemRight = "item-?right".toRegex()


        val name = "(input)?-?catchers?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, _ ->
            val stages = mutableListOf<Inputer.Catcher>()
            if (value is Map<*, *>) {
                value.forEach {
                    val section = Property.asSection(it.value)
                    if (section != null) {
                        val id = it.key.toString()
                        val type = Inputer.Type.of(section.getString(Property.getSectionKey(section, type)) ?: "CHAT")

                        val start =
                            Reactions.ofReaction(Property.asAnyList(section[Property.getSectionKey(section, start, "start")]))
                        val cancel =
                            Reactions.ofReaction(Property.asAnyList(section[Property.getSectionKey(section, cancel, "cancel")]))
                        val end =
                            Reactions.ofReaction(Property.asAnyList(section[Property.getSectionKey(section, end, "end")]))

                        val display = arrayOf(
                            section.getString(Property.getSectionKey(section, display, "display")) ?: "TrMenu Catcher",
                            section.getStringList(Property.getSectionKey(section, display, "display")).joinToString("\n"),
                        )

                        val items = arrayOf(
                            section.getString(Property.getSectionKey(section, itemLeft)) ?: "left",
                            section.getString(Property.getSectionKey(section, itemRight)) ?: "right"
                        )

                        stages += Inputer.Catcher(id, type, start, cancel, end, display, items, section)
                    }
                }
            }
            ActionCatcher(Inputer(CycleList(stages)))
        }

        val registery = name to parser

    }

}