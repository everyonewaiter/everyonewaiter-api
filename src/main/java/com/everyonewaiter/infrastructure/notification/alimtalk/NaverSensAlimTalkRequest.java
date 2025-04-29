package com.everyonewaiter.infrastructure.notification.alimtalk;

import com.everyonewaiter.domain.notification.AlimTalkMessage;
import java.util.List;

record NaverSensAlimTalkRequest(
    String templateCode,
    String plusFriendId,
    List<AlimTalkMessage> messages
) {

}
