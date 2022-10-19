package ray.mintcat.chencat.command

import net.mamoe.mirai.message.data.MessageChain

interface ShortCommand {

    val short: List<String>

    suspend fun evalShort(short: String, sender: Sender, from: MessageChain)

    fun loadS() {
        Command.shortCommands.add(this)
    }

}