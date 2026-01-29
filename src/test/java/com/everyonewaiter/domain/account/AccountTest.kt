package com.everyonewaiter.domain.account

import com.everyonewaiter.domain.ADMIN_PASSWORD
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.time.Instant

class AccountTest {

  @Test
  fun `계정 생성`() {
    val account = createAccount()

    assertThat(account.state).isEqualTo(AccountState.INACTIVE)
    assertThat(account.permission).isEqualTo(AccountPermission.USER)
    assertThat(account.lastSignIn).isEqualTo(Instant.ofEpochMilli(0))

    val domainEvents = ReflectionTestUtils.invokeGetterMethod(account, "domainEvents")
    assertThat(domainEvents as MutableList<*>).hasSize(1)
    assertThat(domainEvents[0]).isInstanceOf(AccountCreateEvent::class.java)
  }

  @Test
  fun `계정이 비활성 상태인지 여부 반환`() {
    val account = createAccount()

    assertThat(account.isInactive).isTrue

    ReflectionTestUtils.setField(account, "state", AccountState.ACTIVE)

    assertThat(account.isInactive).isFalse
  }

  @Test
  fun `계정이 활성 상태인지 여부 반환`() {
    val account = createAccount()

    assertThat(account.isActive).isFalse

    ReflectionTestUtils.setField(account, "state", AccountState.ACTIVE)

    assertThat(account.isActive).isTrue
  }

  @Test
  fun `계정이 계층형 권한을 가지고 있는지 여부 반환`() {
    val account = createAccount()

    assertThat(account.hasPermission(AccountPermission.USER)).isTrue
    assertThat(account.hasPermission(AccountPermission.OWNER)).isFalse
    assertThat(account.hasPermission(AccountPermission.ADMIN)).isFalse

    ReflectionTestUtils.setField(account, "permission", AccountPermission.OWNER)

    assertThat(account.hasPermission(AccountPermission.USER)).isTrue
    assertThat(account.hasPermission(AccountPermission.OWNER)).isTrue
    assertThat(account.hasPermission(AccountPermission.ADMIN)).isFalse

    ReflectionTestUtils.setField(account, "permission", AccountPermission.ADMIN)

    assertThat(account.hasPermission(AccountPermission.USER)).isTrue
    assertThat(account.hasPermission(AccountPermission.OWNER)).isTrue
    assertThat(account.hasPermission(AccountPermission.ADMIN)).isTrue
  }

  @Test
  fun `계정 활성화`() {
    val account = createAccount()

    assertThat(account.state).isEqualTo(AccountState.INACTIVE)

    account.activate()

    assertThat(account.state).isEqualTo(AccountState.ACTIVE)

    assertThatThrownBy { account.activate() }
      .isInstanceOf(AlreadyVerifiedEmailException::class.java)
  }

  @Test
  fun `계정 권한 부여`() {
    val account = createActiveAccount()

    assertThat(account.permission).isEqualTo(AccountPermission.USER)

    account.authorize(AccountPermission.OWNER)

    assertThat(account.permission).isEqualTo(AccountPermission.OWNER)
  }

  @Test
  fun `계정이 이미 해당 권한을 가지고 있다면 권한 부여 X`() {
    val account = createActiveAccount(permission = AccountPermission.ADMIN)

    assertThat(account.permission).isEqualTo(AccountPermission.ADMIN)

    account.authorize(AccountPermission.OWNER)

    assertThat(account.permission).isEqualTo(AccountPermission.ADMIN)
  }

  @Test
  fun `계정이 활성 상태가 아닌 경우 권한 부여 실패`() {
    val account = createAccount()

    AccountState.entries
      .stream()
      .filter { it != AccountState.ACTIVE }
      .forEach {
        ReflectionTestUtils.setField(account, "state", it)
        assertThatThrownBy { account.authorize(AccountPermission.OWNER) }
          .isInstanceOf(DisabledAccountException::class.java)
      }
  }

  @Test
  fun `계정에 관리자 권한을 부여하려는 경우 권한 부여 실패`() {
    val account = createActiveAccount()

    assertThatThrownBy { account.authorize(AccountPermission.ADMIN) }
      .isInstanceOf(IllegalStateException::class.java)
  }

  @Test
  fun `계정 로그인`() {
    val account = createActiveAccount()

    assertThat(account.lastSignIn).isEqualTo(Instant.ofEpochMilli(0))

    account.signIn(createAccountSignInRequest(), createPasswordEncoder(), AccountPermission.USER)

    assertThat(account.lastSignIn).isNotEqualTo(Instant.ofEpochMilli(0))
  }

  @Test
  fun `이메일 인증이 완료되지 않은 경우 로그인 실패`() {
    val account = createAccount()

    assertThatThrownBy {
      account.signIn(
        createAccountSignInRequest(),
        createPasswordEncoder(),
        AccountPermission.USER
      )
    }
      .isInstanceOf(NotCompleteEmailVerificationException::class.java)
  }

  @Test
  fun `비밀번호가 일치하지 않는 경우 로그인 실패`() {
    val account1 = createAccount()
    val account2 = createActiveAccount()

    assertThatThrownBy {
      account1.signIn(
        createAccountSignInRequest(password = "invalid"),
        createPasswordEncoder(),
        AccountPermission.USER
      )
    }.isInstanceOf(FailedSignInException::class.java)

    assertThatThrownBy {
      account2.signIn(
        createAccountSignInRequest(password = "invalid"),
        createPasswordEncoder(),
        AccountPermission.USER
      )
    }.isInstanceOf(FailedSignInException::class.java)
  }

  @Test
  fun `로그인에 필요한 권한이 없는 경우 로그인 실패`() {
    val account = createActiveAccount(permission = AccountPermission.OWNER)

    assertThatThrownBy {
      account.signIn(
        createAccountSignInRequest(),
        createPasswordEncoder(),
        AccountPermission.ADMIN
      )
    }.isInstanceOf(FailedSignInException::class.java)
  }

  @Test
  fun `비밀번호 변경`() {
    val passwordEncoder = createPasswordEncoder()
    val account = createAccount()

    assertThat(passwordEncoder.matches(ADMIN_PASSWORD, account.password)).isTrue

    account.changePassword(createAccountPasswordChangeRequest(), passwordEncoder)

    assertThat(passwordEncoder.matches("@password2", account.password)).isTrue
  }

  @Test
  fun `현재 비밀번호가 일치하지 않는 경우 비밀번호 변경 실패`() {
    val passwordEncoder = createPasswordEncoder()
    val account = createAccount()

    assertThatThrownBy {
      account.changePassword(
        createAccountPasswordChangeRequest(currentPassword = "@invalid123"),
        passwordEncoder
      )
    }.isInstanceOf(MismatchedCurrentPasswordException::class.java)
  }

  @Test
  fun `관리자가 사용자 계정 업데이트`() {
    val adminAccount = createActiveAccount(permission = AccountPermission.ADMIN)
    val userAccount = createAccount()

    assertThat(userAccount.state).isEqualTo(AccountState.INACTIVE)
    assertThat(userAccount.permission).isEqualTo(AccountPermission.USER)

    adminAccount.update(userAccount, createAccountAdminUpdateRequest())

    assertThat(userAccount.state).isEqualTo(AccountState.ACTIVE)
    assertThat(userAccount.permission).isEqualTo(AccountPermission.OWNER)
  }

  @Test
  fun `관리자 계정이 활성 상태가 아니라면 사용자 계정 업데이트 실패`() {
    val adminAccount = createActiveAccount(permission = AccountPermission.ADMIN)
    val userAccount = createAccount()

    ReflectionTestUtils.setField(adminAccount, "state", AccountState.INACTIVE)

    assertThatThrownBy { adminAccount.update(userAccount, createAccountAdminUpdateRequest()) }
      .isInstanceOf(IllegalStateException::class.java)
  }

  @Test
  fun `관리자 권한이 없는 계정이라면 사용자 계정 업데이트 실패`() {
    val fakeAdminAccount = createActiveAccount(permission = AccountPermission.OWNER)
    val userAccount = createAccount()

    assertThatThrownBy {
      fakeAdminAccount.update(
        userAccount,
        createAccountAdminUpdateRequest()
      )
    }.isInstanceOf(IllegalStateException::class.java)
  }
}
