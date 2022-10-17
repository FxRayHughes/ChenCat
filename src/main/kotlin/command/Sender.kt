package command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.net.URL

class Sender(
    val group: Group? = null,
    val member: Member? = null,
    val friend: Friend? = null
) {

    fun get(): Contact {
        if (group != null) {
            return group
        } else if (member != null) {
            return member
        }
        return friend!!
    }

    suspend fun sendMessage(message: String) {
        if (group != null) {
            group.sendMessage(message)
            return
        } else if (member != null) {
            member.sendMessage(message)
            return
        } else if (friend != null) {
            friend.sendMessage(message)
            return
        }
    }

    suspend fun sendFriendMessage(message: String) {
        if (member != null) {
            member.sendMessage(message)
            return
        } else if (friend != null) {
            friend.sendMessage(message)
            return
        }
    }

    suspend fun sendMessage(message: String, from: MessageChain) {
        if (group != null) {
            group.sendMessage(from.quote() + message)
            return
        } else if (member != null) {
            member.sendMessage(from.quote() + message)
            return
        } else if (friend != null) {
            friend.sendMessage(from.quote() + message)
            return
        }
    }

    suspend fun sendMessage(from: MessageChain) {
        if (group != null) {
            group.sendMessage(from)
            return
        } else if (member != null) {
            member.sendMessage(from)
            return
        } else if (friend != null) {
            friend.sendMessage(from)
            return
        }
    }

    suspend fun sendMessageAll(message: Message) {
        if (group != null) {
            group.sendMessage(message)
            return
        } else if (member != null) {
            member.sendMessage(message)
            return
        } else if (friend != null) {
            friend.sendMessage(message)
            return
        }
    }

    suspend fun sendWebImage(url: String) {
        sendImage("https://webscreenshot-production.up.railway.app/?url=${url}&forceWait=1")
    }

    suspend fun sendImage(url: String, from: MessageChain) {
        withContext(Dispatchers.IO) {
            val im = get().uploadImage(getImage(url))
            if (group != null) {
                group.sendMessage(from.quote() + im)
                return@withContext
            } else if (member != null) {
                member.sendMessage(from.quote() + im)
                return@withContext
            } else if (friend != null) {
                friend.sendMessage(from.quote() + im)
                return@withContext
            }
        }
    }

    suspend fun sendImage(url: String) {
        withContext(Dispatchers.IO) {
            val base = URL(url).openConnection().getInputStream().toExternalResource()
            if (group != null) {
                group.sendImage(base)
                return@withContext
            } else if (member != null) {
                member.sendImage(base)
                return@withContext
            } else if (friend != null) {
                friend.sendImage(base)
                return@withContext
            }
        }
    }

    companion object{
        suspend fun getImage(url: String): ExternalResource {
            return withContext(Dispatchers.IO) {
                return@withContext URL(url).openConnection().getInputStream().toExternalResource()
            }
        }
    }

}