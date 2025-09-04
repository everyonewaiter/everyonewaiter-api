package com.everyonewaiter.domain.support;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ClientUri {

  public static final String BASE_URL = "https://everyonewaiter.com";

  public static final String AUTH_EMAIL = "/auth/mail?email=%s&token=%s";
  public static final String STORE_REGISTRATION = "/stores/registrations";
  public static final String WAITING_CANCEL = "/waitings/cancel?storeId=%s&accessKey=%s&phone=%s";
  public static final String WAITING_CHECK_MY_TURN = "/waitings/my-turn?storeId=%s&accessKey=%s";
  public static final String MENU_PREVIEW = "/menus/preview?storeId=%s";

}
