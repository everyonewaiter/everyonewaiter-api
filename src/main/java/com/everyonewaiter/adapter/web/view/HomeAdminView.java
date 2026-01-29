package com.everyonewaiter.adapter.web.view;

import com.everyonewaiter.adapter.web.auth.AuthenticationAccount;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admins")
class HomeAdminView {

  @GetMapping
  public String homeView(
      Model model,
      @AuthenticationAccount(permission = AccountPermission.ADMIN) Account account
  ) {
    model.addAttribute("account", account);
    return "admin/home";
  }

}
