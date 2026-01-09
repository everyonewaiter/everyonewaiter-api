package com.everyonewaiter.adapter.persistence;

import static com.everyonewaiter.domain.account.QAccount.account;
import static com.everyonewaiter.domain.device.QDevice.device;

import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.application.device.required.DeviceRepository;
import com.everyonewaiter.application.health.provided.HealthCheckCreator;
import com.everyonewaiter.application.health.required.ApkVersionRepository;
import com.everyonewaiter.application.menu.required.CategoryRepository;
import com.everyonewaiter.application.menu.required.MenuRepository;
import com.everyonewaiter.application.store.required.RegistrationRepository;
import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountCreateRequest;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.PasswordEncoder;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.device.DeviceCreateRequest;
import com.everyonewaiter.domain.device.DevicePaymentType;
import com.everyonewaiter.domain.device.DevicePurpose;
import com.everyonewaiter.domain.menu.Category;
import com.everyonewaiter.domain.menu.CategoryCreateRequest;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuCreateRequest;
import com.everyonewaiter.domain.menu.MenuLabel;
import com.everyonewaiter.domain.menu.MenuOptionGroupModifyRequest;
import com.everyonewaiter.domain.menu.MenuOptionGroupType;
import com.everyonewaiter.domain.menu.MenuOptionModifyRequest;
import com.everyonewaiter.domain.menu.MenuState;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationApplyRequest;
import com.everyonewaiter.domain.store.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!prod")
@Component
@RequiredArgsConstructor
class DataInitializer implements ApplicationRunner {

  private static final String ADMIN_EMAIL = "admin@everyonewaiter.com";
  private static final String ADMIN_PASSWORD = "@password1";
  private static final String ADMIN_PHONE = "01044591812";

  private final DataInitializerTransactionSupporter tx;
  private final PasswordEncoder passwordEncoder;
  private final JPAQueryFactory queryFactory;
  private final HealthCheckCreator healthCheckCreator;
  private final ApkVersionRepository apkVersionRepository;
  private final AccountRepository accountRepository;
  private final RegistrationRepository registrationRepository;
  private final StoreRepository storeRepository;
  private final CategoryRepository categoryRepository;
  private final MenuRepository menuRepository;
  private final DeviceRepository deviceRepository;

  @Override
  public void run(@NonNull ApplicationArguments args) throws Exception {
    createApkVersion();

    boolean existsDefaultAdmin = accountRepository.exists(new Email(ADMIN_EMAIL));
    if (existsDefaultAdmin) {
      return;
    }

    // Account
    var adminAccount = createAdminAccount();
    authorizeAdminAccount(adminAccount);

    // Store
    createStoreRegistration(adminAccount);
    Store store = storeRepository.findAll(adminAccount.getId()).getFirst();

    // Menu
    List<Category> categories = createCategories(store);
    createMenus(categories);

    // Device
    List<Device> devices = createDevices(store);
    setSimpleSecretKey(devices);
  }

  private void createApkVersion() {
    if (apkVersionRepository.findLatest().isEmpty()) {
      healthCheckCreator.createApkVersion();
    }
  }

  private Account createAdminAccount() {
    var request = new AccountCreateRequest(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_PHONE);
    var adminAccount = Account.create(request, passwordEncoder);
    adminAccount.activate();
    return accountRepository.save(adminAccount);
  }

  private void authorizeAdminAccount(Account adminAccount) {
    tx.executeInTransaction(() -> queryFactory
        .update(account)
        .set(account.permission, AccountPermission.ADMIN)
        .where(account.id.eq(adminAccount.getId()))
        .execute()
    );
  }

  private void createStoreRegistration(Account account) {
    var request = new RegistrationApplyRequest(
        "모두의 웨이터", "손정웅", "경상남도 창원시 의창구 도계로 135", "0551234567", "443-60-00875", null
    );
    var registration = Registration.apply(account, request, "test/license.webp");
    registration.approve();
    registrationRepository.save(registration);
  }

  private List<Category> createCategories(Store store) {
    var categories = new ArrayList<Category>();
    var categoryNames = new String[]{"Steak", "Pasta", "Rice", "Pizza", "Salad", "Drink", "Etc"};
    for (int i = 0; i < categoryNames.length; i++) {
      var category = Category.create(store, new CategoryCreateRequest(categoryNames[i]), i);
      categories.add(category);
      categoryRepository.save(category);
    }
    return Collections.unmodifiableList(categories);
  }

  private Category findCategoryByName(List<Category> categories, String name) {
    for (Category category : categories) {
      if (category.getName().equalsIgnoreCase(name)) {
        return category;
      }
    }
    throw new IllegalArgumentException("카테고리 " + name + " 을(를) 찾을 수 없습니다.");
  }

  private void createMenus(List<Category> categories) {
    var menus = new ArrayList<Menu>();
    var position = 0;

    var steak = findCategoryByName(categories, "Steak");
    var steak1 = new MenuCreateRequest(
        "수비드 소고기 스테이크",
        "굽기: 미디움 / 변경 불가능",
        35000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "선택 옵션",
                MenuOptionGroupType.OPTIONAL,
                false,
                List.of(
                    new MenuOptionModifyRequest("레드 와인 1잔", 6000)
                )
            )
        )
    );
    menus.add(Menu.create(steak, steak1, "test/steak.webp", position));
    position++;

    var pasta = findCategoryByName(categories, "Pasta");
    var pasta1 = new MenuCreateRequest(
        "라구 볼로네제 파스타",
        "",
        19500,
        1,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("안맵게", 0),
                    new MenuOptionModifyRequest("맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(pasta, pasta1, "test/pasta1.webp", position));
    position++;

    var pasta2 = new MenuCreateRequest(
        "새우품은 빠네 크림 파스타",
        "",
        19800,
        0,
        MenuState.DEFAULT,
        MenuLabel.RECOMMEND,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(pasta, pasta2, "test/pasta2.webp", position));
    position++;

    var pasta3 = new MenuCreateRequest(
        "새우품은 빠네 로제 파스타",
        "",
        19900,
        1,
        MenuState.DEFAULT,
        MenuLabel.RECOMMEND,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("안맵게", 0),
                    new MenuOptionModifyRequest("맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(pasta, pasta3, "test/pasta3.webp", position));
    position++;

    var pasta4 = new MenuCreateRequest(
        "통통새우 크림 파스타",
        "",
        18500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(pasta, pasta4, "test/pasta4.webp", position));
    position++;

    var pasta5 = new MenuCreateRequest(
        "통통새우 로제 파스타",
        "",
        18900,
        1,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("안맵게", 0),
                    new MenuOptionModifyRequest("맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(pasta, pasta5, "test/pasta5.webp", position));
    position++;

    var pasta6 = new MenuCreateRequest(
        "까르보나라",
        "",
        14900,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(pasta, pasta6, "test/pasta6.webp", position));
    position++;

    var pasta7 = new MenuCreateRequest(
        "매운 크림 파스타",
        "",
        18900,
        2,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("더 맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(pasta, pasta7, "test/pasta7.webp", position));
    position++;

    var pasta8 = new MenuCreateRequest(
        "씨푸드 토마토 파스타",
        "",
        19900,
        1,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("안맵게", 0),
                    new MenuOptionModifyRequest("맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(pasta, pasta8, "test/pasta8.webp", position));
    position++;

    var pasta9 = new MenuCreateRequest(
        "빠쉐",
        "",
        19900,
        2,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("안맵게", 0),
                    new MenuOptionModifyRequest("맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(pasta, pasta9, "test/pasta9.webp", position));
    position++;

    var pasta10 = new MenuCreateRequest(
        "통통새우 알리오올리오",
        "",
        17500,
        1,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("안맵게", 0),
                    new MenuOptionModifyRequest("맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(pasta, pasta10, "test/pasta10.webp", position));
    position++;

    var rice = findCategoryByName(categories, "Rice");
    var rice1 = new MenuCreateRequest(
        "씨푸드 크림 리조또",
        "",
        18900,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of(
            new MenuOptionGroupModifyRequest(
                "맵기",
                MenuOptionGroupType.OPTIONAL,
                true,
                List.of(
                    new MenuOptionModifyRequest("맵게", 0)
                )
            )
        )
    );
    menus.add(Menu.create(rice, rice1, "test/rice1.webp", position));
    position++;

    var rice2 = new MenuCreateRequest(
        "참숯 불고기 필라프(돈육)",
        "",
        16900,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(rice, rice2, "test/rice2.webp", position));
    position++;

    var pizza = findCategoryByName(categories, "Pizza");
    var pizza1 = new MenuCreateRequest(
        "고르곤졸라 피자",
        "",
        12900,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(pizza, pizza1, "test/pizza1.webp", position));
    position++;

    var pizza2 = new MenuCreateRequest(
        "알알이 옥수수 피자",
        "",
        15500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(pizza, pizza2, "test/pizza2.webp", position));
    position++;

    var pizza3 = new MenuCreateRequest(
        "아몬드 고구마 피자",
        "",
        15500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(pizza, pizza3, "test/pizza3.webp", position));
    position++;

    var pizza4 = new MenuCreateRequest(
        "샐러드 피자",
        "",
        25000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(pizza, pizza4, "test/pizza4.webp", position));
    position++;

    var pizza5 = new MenuCreateRequest(
        "불고기 피자",
        "",
        26000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(pizza, pizza5, "test/pizza5.webp", position));
    position++;

    var pizza6 = new MenuCreateRequest(
        "루꼴라 불고기 피자",
        "토핑에 레드페퍼 포함",
        26000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(pizza, pizza6, "test/pizza6.webp", position));
    position++;

    var salad = findCategoryByName(categories, "Salad");
    var salad1 = new MenuCreateRequest(
        "클래식 샐러드",
        "",
        16900,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(salad, salad1, "test/salad1.webp", position));
    position++;

    var salad2 = new MenuCreateRequest(
        "하우스 샐러드",
        "",
        14900,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(salad, salad2, "test/salad2.webp", position));
    position++;

    var salad3 = new MenuCreateRequest(
        "연어 샐러드",
        "",
        17900,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(salad, salad3, "test/salad3.webp", position));
    position++;

    var drink = findCategoryByName(categories, "Drink");
    var drink1 = new MenuCreateRequest(
        "아메리카노(HOT)",
        "식사 메뉴(스테이크, 파스타, 라이스) 주문 시 1메뉴 1잔 3000원",
        4500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of(
            new MenuOptionGroupModifyRequest(
                "할인",
                MenuOptionGroupType.OPTIONAL,
                false,
                List.of(
                    new MenuOptionModifyRequest("식사 메뉴 주문 시", -1500)
                )
            )
        )
    );
    menus.add(Menu.create(drink, drink1, "test/drink1.webp", position));
    position++;

    var drink2 = new MenuCreateRequest(
        "아메리카노(ICE)",
        "식사 메뉴(스테이크, 파스타, 라이스) 주문 시 1메뉴 1잔 3000원",
        4500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of(
            new MenuOptionGroupModifyRequest(
                "할인",
                MenuOptionGroupType.OPTIONAL,
                false,
                List.of(
                    new MenuOptionModifyRequest("식사 메뉴 주문 시", -1500)
                )
            )
        )
    );
    menus.add(Menu.create(drink, drink2, "test/drink2.webp", position));
    position++;

    var drink3 = new MenuCreateRequest(
        "레몬에이드(ICE)",
        "",
        5000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of()
    );
    menus.add(Menu.create(drink, drink3, "test/drink3.webp", position));
    position++;

    var drink4 = new MenuCreateRequest(
        "자몽에이드(ICE)",
        "",
        5000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of()
    );
    menus.add(Menu.create(drink, drink4, "test/drink4.webp", position));
    position++;

    var drink5 = new MenuCreateRequest(
        "아이스티(복숭아)",
        "",
        3500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of()
    );
    menus.add(Menu.create(drink, drink5, "test/drink5.webp", position));
    position++;

    var drink6 = new MenuCreateRequest(
        "콜라",
        "",
        2500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of(
            new MenuOptionGroupModifyRequest(
                "선택 옵션",
                MenuOptionGroupType.OPTIONAL,
                false,
                List.of(
                    new MenuOptionModifyRequest("얼음컵", 0)
                )
            )
        )
    );
    menus.add(Menu.create(drink, drink6, "test/drink6.webp", position));
    position++;

    var drink7 = new MenuCreateRequest(
        "제로 콜라",
        "",
        2500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of(
            new MenuOptionGroupModifyRequest(
                "선택 옵션",
                MenuOptionGroupType.OPTIONAL,
                false,
                List.of(
                    new MenuOptionModifyRequest("얼음컵", 0)
                )
            )
        )
    );
    menus.add(Menu.create(drink, drink7, "test/drink7.webp", position));
    position++;

    var drink8 = new MenuCreateRequest(
        "사이다",
        "",
        2500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of(
            new MenuOptionGroupModifyRequest(
                "선택 옵션",
                MenuOptionGroupType.OPTIONAL,
                false,
                List.of(
                    new MenuOptionModifyRequest("얼음컵", 0)
                )
            )
        )
    );
    menus.add(Menu.create(drink, drink8, "test/drink8.webp", position));
    position++;

    var drink9 = new MenuCreateRequest(
        "환타(파인애플)",
        "",
        2500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of(
            new MenuOptionGroupModifyRequest(
                "선택 옵션",
                MenuOptionGroupType.OPTIONAL,
                false,
                List.of(
                    new MenuOptionModifyRequest("얼음컵", 0)
                )
            )
        )
    );
    menus.add(Menu.create(drink, drink9, "test/drink9.webp", position));
    position++;

    var drink10 = new MenuCreateRequest(
        "수제 레몬차",
        "",
        5500,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of(
            new MenuOptionGroupModifyRequest(
                "선택 옵션",
                MenuOptionGroupType.OPTIONAL,
                false,
                List.of(
                    new MenuOptionModifyRequest("ICE", 500)
                )
            )
        )
    );
    menus.add(Menu.create(drink, drink10, "test/drink10.webp", position));
    position++;

    var drink11 = new MenuCreateRequest(
        "레드 와인 1잔",
        "",
        8000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of()
    );
    menus.add(Menu.create(drink, drink11, "test/drink11.webp", position));
    position++;

    var drink12 = new MenuCreateRequest(
        "레드 와인 1병",
        "",
        40000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of()
    );
    menus.add(Menu.create(drink, drink12, "test/drink12.webp", position));
    position++;

    var drink13 = new MenuCreateRequest(
        "일품진로",
        "",
        25000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of()
    );
    menus.add(Menu.create(drink, drink13, "test/drink13.webp", position));
    position++;

    var drink14 = new MenuCreateRequest(
        "켈리",
        "",
        5000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of()
    );
    menus.add(Menu.create(drink, drink14, "test/drink14.webp", position));
    position++;

    var drink15 = new MenuCreateRequest(
        "카스",
        "",
        5000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        false,
        List.of()
    );
    menus.add(Menu.create(drink, drink15, "test/drink15.webp", position));
    position++;

    var etc = findCategoryByName(categories, "Etc");
    var etc1 = new MenuCreateRequest(
        "공기밥",
        "",
        1000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(etc, etc1, "test/etc1.webp", position));
    position++;

    var etc2 = new MenuCreateRequest(
        "빵 추가",
        "",
        3000,
        0,
        MenuState.DEFAULT,
        MenuLabel.DEFAULT,
        true,
        List.of()
    );
    menus.add(Menu.create(etc, etc2, "test/etc2.webp", position));

    for (Menu menu : menus) {
      menuRepository.save(menu);
    }
  }

  private List<Device> createDevices(Store store) {
    var devices = new ArrayList<Device>();
    DevicePurpose[] purposes = DevicePurpose.values();
    for (DevicePurpose purpose : purposes) {
      var tableNo = purpose == DevicePurpose.TABLE ? 100 : 0;
      var tableName = purpose == DevicePurpose.TABLE
          ? purpose.name() + "-" + tableNo
          : purpose.name();
      var request = new DeviceCreateRequest(
          ADMIN_PHONE,
          "TEST-" + tableName,
          purpose,
          tableNo,
          DevicePaymentType.POSTPAID
      );
      var device = Device.create(store, request);
      devices.add(deviceRepository.save(device));
      deviceRepository.save(device);
    }
    return Collections.unmodifiableList(devices);
  }

  private void setSimpleSecretKey(List<Device> devices) {
    for (Device d : devices) {
      tx.executeInTransaction(() -> queryFactory
          .update(device)
          .set(device.secretKey, d.getName())
          .where(device.id.eq(d.getId()))
          .execute());
    }
  }

}
