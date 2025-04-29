package com.everyonewaiter.application.notification.request;

import com.everyonewaiter.domain.notification.alimtalk.AlimTalkMessage;

public record AlimTalkSend(String templateCode, AlimTalkMessage messages) {

}
