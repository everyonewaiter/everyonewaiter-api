package com.everyonewaiter.adapter.web.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admins")
class AccountAdminView {

  @GetMapping("/sign-in")
  public String signInView() {
    return "admin/sign-in";
  }

}
