# 모두의 웨이터 API

무약정 테이블 오더 서비스, 모두의 웨이터 백엔드 저장소입니다.

## Getting Start

### 요구사항

- JDK 21
- MySQL 8.4.3
- Redis 7.4.1
- Docker & Docker Compose

### 프로젝트 설정

설정 예제는 [application.properties.example](./src/main/resources/application.properties.example)를
참고해 주세요.

#### 필수 수정 항목

- `#DB`: MySQL 연결 설정
- `#REDIS`: Redis 연결 설정
- `#MAIL`: 이메일 발송 설정
- `#NAVER SENS`: 알림톡 발송 설정 ([네이버 클라우드](https://www.ncloud.com/))
- `#DISCORD`: 디스코드 알림 설정 (이벤트 및 에러, 웹훅)
- `#ORACLE OBJECT STORAGE`: 클라우드 스토리지 설정 ([오라클 클라우드](https://www.oracle.com/cloud/))
- `#JWT`: JWT 시크릿 키

### 빌드

```
$  ./gradlew clean build
```

### 실행

```
$  docker compose up --build -d
```

### 종료

```
$  docker compose down
```

## Getting Help

- [개발 가이드](https://github.com/everyonewaiter/documentation/tree/main/backend)

## Coding Convention

- intellij-formatter: [google-intellij-formatter.xml](./google-intellij-formatter.xml)
