# SSL 인증서 갱신

오라클 클라우드 로드밸런서 SSL 인증서 갱신 방법에 대해 설명합니다.

## 1. 오라클 클라우드 인스턴스 접속

오라클 클라우드의 Bastion 세션을 생성하고, SSH를 통해 인스턴스로 접속합니다.

## 2. OCI DNS 인증 플러그인 설치

[이 프로젝트](https://github.com/therealcmj/certbot-dns-oci)를 참고하여 플러그인을 설치합니다.

## 3. 인증서 생성

```
cd certbot-dns-oci

certbot certonly \
 --logs-dir logs --work-dir work --config-dir config \
 --authenticator dns-oci -d *.everyonewaiter.com -d everyonewaiter.com
```

## 4. 인증서 갱신

생성된 `cert.pem`, `fullchain.pem`, `private.pem`을 이용해 인증서를 갱신합니다.

- OCI 접속
- `Certificates` -> `Certificates` -> `wildcard-everyonewaiter`
- Renew Certificate

---

## History

- Renew 2025.04.04 10:38:07 UTC ~ 2025.07.03 10:38:06 UTC
- Renew 2025.07.04 13:04:55 UTC ~ 2025.10.02 13:04:54 UTC
- Renew 2025.09.22 05:28:53 UTC ~ 2025.12.21 05:28:52 UTC
- Renew 2025.12.14 23:14:32 UTC ~ 2026.03.14 23:14:31 UTC
