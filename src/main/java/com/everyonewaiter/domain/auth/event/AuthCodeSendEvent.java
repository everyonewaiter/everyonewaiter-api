package com.everyonewaiter.domain.auth.event;

public record AuthCodeSendEvent(String phoneNumber, int code) {

}
