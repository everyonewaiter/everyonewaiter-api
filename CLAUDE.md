# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## 프로젝트 개요

무약정 테이블 오더 서비스 "모두의 웨이터" 백엔드 API 서버입니다.

## 기술 스택

- Java 21
- Spring Boot 4.0.1 (WebMVC, Security, Data JPA, Validation)
- MySQL 8.4.3, Redis 7.4.1
- Flyway (DB 마이그레이션)
- QueryDSL 7.1 (동적 쿼리)
- Redisson 4.0.0 (분산 락)
- OpenFeign (HTTP 클라이언트)

## 빌드 및 실행

```bash
# 빌드
./gradlew clean build

# 테스트 실행
./gradlew test

# 단일 테스트 실행
./gradlew test --tests "com.everyonewaiter.EveryonewaiterApiApplicationTest"

# 애플리케이션 실행 (Docker Compose)
docker compose up --build -d

# 종료
docker compose down
```

## 아키텍처

헥사고날 아키텍처 기반의 3계층 구조:

```
com.everyonewaiter
├── domain/          # 도메인 모델 (Entity, Value Object, Exception, Event)
├── application/     # 유스케이스 (Service, EventHandler)
│   ├── provided/    # 외부에 제공하는 인터페이스 구현체
│   └── required/    # 외부 의존성 인터페이스 (Repository, Client)
└── adapter/         # 인프라 어댑터
    ├── web/         # REST API 컨트롤러
    ├── persistence/ # JPA/Redis Repository 구현체
    ├── integration/ # 외부 서비스 연동 (알림톡, 이미지 스토리지, AOP)
    ├── security/    # 보안 관련 구현체
    └── config/      # Spring 설정
```

### 의존성 규칙

- `domain` → 어디에도 의존하지 않음
- `application` → `domain`에만 의존
- `adapter` → `domain`, `application`에 의존

### 주요 도메인

- **Account**: 계정 (회원가입, 로그인, 권한)
- **Auth**: 인증 (휴대폰 인증, JWT)
- **Store**: 매장 (등록, 설정, 영업 상태)
- **Device**: 기기 (POS, 홀 관리, 테이블, 웨이팅)
- **Menu**: 메뉴 (카테고리, 메뉴, 옵션)
- **Order**: 주문 (주문, 결제)
- **POS**: POS 테이블 (테이블 관리, 액티비티)
- **Waiting**: 웨이팅 (대기열 관리)
- **StaffCall**: 직원 호출
- **Notification**: 알림 (알림톡, 이메일)
- **Image**: 이미지 (업로드, 변환)
- **SSE**: Server-Sent Events (실시간 이벤트)

### AOP 어노테이션

- `@StoreOwner`: 매장 소유자 검증
- `@StoreOpen`: 매장 영업 상태 검증
- `@StoreExist`: 매장 존재 여부 검증
- `@DistributedLock`: 분산 락 적용
- `@AuthenticationAccount`: 계정 인증 검증
- `@AuthenticationDevice`: 기기 인증 검증

## 코딩 컨벤션

- 포매터: `google-intellij-formatter.xml` 사용
- 테스트: Java + JUnit 5 + Testcontainers

## API 문서

로컬 실행 후 http://localhost:8081/documents 에서 확인 가능 (SpringDoc OpenAPI)
