package command

import net.mamoe.mirai.message.data.MessageChain
import command.Command
import command.Sender

interface ShortCommand {

    val short: List<String>

    suspend fun evalShort(short: String, sender: Sender, from: MessageChain)

    fun loadS() {
        Command.shortCommands.add(this)
    }

}