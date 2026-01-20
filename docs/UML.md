# UML

모두의 웨이터 중요 도메인 흐름을 UML 다이어그램으로 정리한 문서입니다.

---

## 목차

- [계정 생성](#계정-생성)
- [매장 등록](#매장-등록)
- [기기 생성](#기기-생성)
- [POS](#POS)

---

## 계정 생성

```mermaid
sequenceDiagram
    autonumber
    actor U as 신규 사용자
    participant S as 서버
    participant N as 네이버 클라우드 SENS
    participant M as 오라클 클라우드 메일 서버
    U ->> S: 휴대폰 번호 인증 요청
    S -->> U: 204 응답
    S ->> N: 인증 번호 알림톡 전송 요청
    N -->> U: 인증 번호 알림톡 전송
    U ->> S: 인증 번호 검증 요청
    S -->> U: 204 응답 (인증 번호 검증 완료)
    U ->> S: 계정 생성 요청
    S -->> U: 201 응답 (계정 생성 완료, 비활성 상태)
    S ->> M: 이메일 인증 메일 발송 요청
    M -->> U: 이메일 인증 메일 발송
    U ->> S: 이메일 인증 요청
    S -->> U: 204 응답 (이메일 인증 완료, 계정 활성화)
```

---

## 매장 등록

### 신청 -> 승인

```mermaid
sequenceDiagram
    autonumber
    actor U as 사용자
    participant S as 서버
    participant D as 디스코드
    actor A as 관리자
    participant M as 오라클 클라우드 메일 서버
    U ->> S: 매장 등록 신청 요청
    S -->> U: 201 응답 (매장 등록 생성)
    S ->> D: 매장 등록 신청 알림
    A ->> S: 매장 등록 승인 요청
    S -->> A: 204 응답 (매장 등록 승인, 매장 생성, 사용자 계정 권한 사장님으로 변경)
    S ->> M: 매장 등록 승인 메일 발송 요청
    M -->> U: 매장 등록 승인 메일 발송
```

### 신청 -> 반려 -> 재신청 -> 승인

```mermaid
sequenceDiagram
    autonumber
    actor U as 사용자
    participant S as 서버
    participant D as 디스코드
    actor A as 관리자
    participant M as 오라클 클라우드 메일 서버
    U ->> S: 매장 등록 신청 요청
    S -->> U: 201 응답 (매장 등록 생성)
    S ->> D: 매장 등록 신청 알림
    A ->> S: 매장 등록 반려 요청
    S -->> A: 204 응답 (매장 등록 반려)
    S ->> M: 매장 등록 반려 메일 발송 요청
    M -->> U: 매장 등록 반려 메일 발송
    U ->> S: 매장 등록 재신청 요청
    S -->> U: 204 응답 (매장 등록 재신청)
    S ->> D: 매장 등록 재신청 알림
    A ->> S: 매장 등록 승인 요청
    S -->> A: 204 응답 (매장 등록 승인, 매장 생성, 사용자 계정 권한 사장님으로 변경)
    S ->> M: 매장 등록 승인 메일 발송 요청
    M -->> U: 매장 등록 승인 메일 발송
```

---

## 기기 생성

```mermaid
sequenceDiagram
    autonumber
    actor O as 사장님
    participant S as 서버
    participant N as 네이버 클라우드 SENS
    O ->> S: 휴대폰 번호 인증 요청
    S -->> O: 204 응답
    S ->> N: 인증 번호 알림톡 전송 요청
    N -->> O: 인증 번호 알림톡 전송
    O ->> S: 인증 번호 검증 요청
    S -->> O: 200 응답 (인증 번호 검증 완료, 사장님 소유 매장 목록 응답)
    O ->> S: 매장 선택 후 기기 생성 요청
    S -->> O: 201 응답 (기기 생성, 기기 ID 및 시크릿 키 응답)
```

---

## POS

### POS 테이블 목록 생성

```mermaid
sequenceDiagram
    autonumber
    actor P as POS 기기
    participant S as 서버
    participant E as 이벤트 핸들러
    P ->> S: 매장 오픈 요청
    S ->> E: 매장 오픈 이벤트 발행
    E -->> S: POS 테이블 목록 생성
    S -->> P: 204 응답 (매장 오픈)
```

### POS 테이블 목록 비활성

```mermaid
sequenceDiagram
    autonumber
    actor P as POS 기기
    participant S as 서버
    participant E as 이벤트 핸들러
    P ->> S: 매장 마감 요청
    S ->> E: 매장 마감 이벤트 발행
    E -->> S: POS 테이블 목록 비활성
    S -->> P: 204 응답 (매장 마감)
```

### POS 테이블 액티비티 생성

```mermaid
flowchart TD
    A((주문 또는 주문 결제)) --> B{POS 테이블 액티비티 조회}
    B -- 액티비티가 있는 경우 --> C((주문 또는 주문 결제 생성))
    B -- 액티비티가 없는 경우 --> D[POS 테이블 액티비티 생성]
    D --> C
```

### POS 테이블 액티비티 비활성

#### 선결제 테이블인 경우

```mermaid
sequenceDiagram
    autonumber
    actor P as POS 기기
    participant S as 서버
    P ->> S: 테이블 완료 요청
    S -->> P: 204 응답 (POS 테이블 액티비티 비활성)
```

#### 후결제 테이블인 경우

```mermaid
flowchart TD
    A((주문 결제 승인 요청)) --> B{POS 테이블 액티비티 조회}
    B -- 액티비티가 있는 경우 --> C((주문 결제 승인))
    B -- 액티비티가 없는 경우 --> D[POS 테이블 액티비티 생성]
    D --> C
    C -- 잔여 결제 금액이 있는 경우 --> E((주문 결제 승인 응답))
    C -- 잔여 결제 금액이 없는 경우 --> F[POS 테이블 액티비티 비활성]
    F --> E
```

---
