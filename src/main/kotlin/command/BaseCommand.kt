package command

import net.mamoe.mirai.message.data.MessageChain
import ray.mintcat.chencat.PluginMain

abstract class BaseCommand(val group: String) {

    abstract val subCommand: Map<String, String>

    abstract suspend fun run(sender: Sender, from: MessageChain)

    abstract val alias: MutableList<String>

    fun load() {
        PluginMain.logger.info("注入命令: $group")
        Command.commands.add(this)
    }

    suspend fun getInfo(sender: Sender, from: MessageChain) {
        val list = mutableListOf<String>()
        list.add("子命令列表:")
        var i = 1
        subCommand.forEach { (key, info) ->
            list.add("${i}. $info ↓")
            list.add(".$group $key")
            i += 1
        }
        sender.sendMessage(list.joinToString("\n"), from)
    }

}