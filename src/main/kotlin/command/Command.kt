package ray.mintcat.chencat.command

import net.mamoe.mirai.message.data.MessageChain
import ray.mintcat.chencat.module.clazz.command.DayCommand
import ray.mintcat.chencat.module.clazz.command.ToDayCommand
import ray.mintcat.chencat.module.clazz.command.YingJiCommand
import ray.mintcat.chencat.module.report.ReportCommand

object Command {

    fun loadAll() {
        DayCommand.load()
        DayCommand.loadS()
        ToDayCommand.load()
        ToDayCommand.loadS()
        YingJiCommand.load()
        YingJiCommand.loadS()
        ReportCommand.load()
    }

    val commands = ArrayList<BaseCommand>()

    val shortCommands = ArrayList<ShortCommand>()

    suspend fun runCommand(sender: Sender, message: MessageChain) {
        val data = shortCommands.firstOrNull { it.short.contains(message.contentToString()) }
        if (data != null) {
            data.evalShort(message.contentToString(), sender, message)
            return
        }
        if (message.contentToString().startsWith(".")) {
            val args = message.contentToString().split(" ")
            val key = args[0].replace(".", "")
            val group = commands.firstOrNull() { it.group == key || it.alias.contains(key) } ?: return
            group.run(sender, message)
            return
        } else {
            val args = message.contentToString().split(" ")
            val key = args[0].replace(".", "")
            val group = commands.firstOrNull() { it.group == key || it.alias.contains(key) } ?: return
            group.run(sender, message)
            return
        }
    }

}