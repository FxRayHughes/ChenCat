package command

import net.mamoe.mirai.message.data.MessageChain
import ray.mintcat.chencat.command.impl.DayCommand

object Command {

    fun loadAll() {
        DayCommand.load()
        DayCommand.loadS()
    }

    val commands = ArrayList<BaseCommand>()
    val shortCommands = ArrayList<ShortCommand>()

    suspend fun runCommand(sender: Sender, message: MessageChain) {
        if (message.contentToString().startsWith(".")) {
            val args = message.contentToString().split(" ")
            val key = args[0].replace(".", "")
            val group = commands.firstOrNull() { it.group == key || it.alias.contains(key) } ?: return
            group.run(sender, message)
        }
        val data = shortCommands.firstOrNull { it.short.contains(message.contentToString()) } ?: return
        data.evalShort(message.contentToString(), sender, message)
    }
}