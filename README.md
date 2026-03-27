# FarmFlow

> 실제 농장 운영 문제를 해결하기 위해 만든 농장 관리 시스템 백엔드

FarmFlow는 새송이 버섯 농장의 생육 관리, 작업자 관리, 운영 데이터를 체계적으로 관리하기 위해 개발한 백엔드 API 서버입니다.
Spring Boot + JPA 기반으로 설계했으며, 향후 PWA 모바일 앱과 연동하는 것을 목표로 합니다.

---

## 2. 기술 스택

- **Backend**
    - Java 17
    - Spring Boot
    - Spring Data JPA (Hibernate)

- **Database**
    - MySQL

- **Authentication**
    - JWT

- **Infra**
    - Docker (개발 환경)

---

## 2. 핵심 설계 포인트

### 1. 농장 단위 RBAC 구조

단순한 사용자 Role이 아닌, 농장 단위 권한 구조로 설계

> User ↔ Farm (N:M)
> → FarmUser (역할 포함)
> → Role
> → RolePermission

- 농장마다 다른 역할을 가질 수 있음
- 권한 확장 가능 구조
- 실제 서비스 확장 고려 설계

### 2. 초대 기반 협업 구조

- 이메일 기반 초대 생성
- 초대 코드(hash) 사용
- 초대 승인 시 자동으로 farm_user 생성
- Slack / Notion과 유사한 협업 구조를 참고

### 3. 확장 가능한 도메인 설계

현재는 사용자/농장 중심 구조이지만, 아래 도메인 확장을 고려하여 설계

- Room (생육동)
- CultivationCycle (생육 사이클)
- RoomReading (환경 데이터)
- Attendance (출퇴근)

---

## 3. 주요 기능

### 사용자 관리

- 회원가입
- 로그인 (JWT)
- 토큰 재발급 (Refresh Token)

### 2. 농장 관리

- 농장 생성
- 농장 단위 사용자 관리 (FarmUser)

### 3. 역할 관리 (RBAC)

- 농장 단위 Role 기반 권한 구조
- 기본 역할: `OWNER`, `WORKER`
- Role - Permission 구조 설계 및 확장 가능

### 4. 농장 초대 기능

- 이메일 기반 초대
- 초대 코드(6자리) 발급 및 검증

### 5. 농장 멤버 관리

- 멤버 관리
- 역할 변경
- 멤버 탈퇴

### 6. 향후 개발 계획

- 생육동(Room) 관리
- 생육 사이클(CultivationCycle)
- 환경 데이터 기록 (CO2 / 온도 / 습도 / 이미지)
- 출퇴근 관리 (Attendance)
- 농장 운영 통계
- 급여 계산 보조 기능

--- 

## 3. 도메인 구조

#### 주요 도메인

- User
- Farm
- FarmUser
- Role
- RolePermission
- FarmInvitation
- RefreshToken

#### 예정 도메인

- Room
- CultivationCycle
- RoomReading
- Attendance

---

## 4. 실행 방법

### 1. MySQL 실행 (Docker)

> MySQL은 Docker를 사용하여 로컬 환경에서 실행합니다.

```bash
docker run -d \
  --name farmflow-mysql \
  -p 33065:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=farmflow \
  mysql:8.0
```

### 2. application.yml 설정

> 애플리케이션 실행을 위해 DB, JWT, 메일 관련 설정이 필요합니다.
> 해당 값들은 application.yml 또는 환경변수를 통해 설정해주세요.

- DB (MySQL)
- JWT Secret
- Mail (Gmail SMTP)

### 3. 서버 실행

```bash
./gradlew bootRun
```

### 4. Swagger

- http://localhost:4000/swagger-ui/index.html
