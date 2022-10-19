package ray.mintcat.chencat.module.groupphoto

import module.report.data.AppData
import net.mamoe.mirai.event.AbstractEvent
import net.mamoe.mirai.event.events.GroupMessageEvent

class GroupPhotoEvent(
    var event: GroupMessageEvent,
    var appData: AppData
) : AbstractEvent()