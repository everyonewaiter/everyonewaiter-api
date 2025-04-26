package com.everyonewaiter.application.notification.alimtalk.service.request;

import com.everyonewaiter.domain.notification.alimtalk.AlimTalkMessage;

public record AlimTalkSend(String templateCode, AlimTalkMessage messages) {

}
