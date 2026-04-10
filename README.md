# FarmFlow

부모님이 운영하는 새송이 버섯 농장을 더 체계적으로 관리하기 위해 개발 중인 농장 관리 시스템 백엔드입니다.

Spring Boot, JPA, MySQL 기반으로 설계하고 있으며, 현장에서 바로 사용할 수 있는 PWA 모바일 앱과 연동하는 것을 목표로 합니다.

## 프로젝트 소개

FarmFlow는 단순한 CRUD를 넘어, 실제 농장 운영 흐름을 도메인 모델로 옮기는 데 초점을 둔 프로젝트입니다.

- 농장 단위 사용자, 멤버십, 권한 관리
- 생육동(Room) 운영 관리
- `CultivationCycle` 기반 생육 흐름 추적
- 환경 기록(CO2 / 온도 / 습도 / 이미지) 관리
- 출퇴근 관리

가장 중요한 기준은 "부모님 농장에서 실제로 사용할 수 있는가?"입니다.

## 프로젝트 배경

새송이 버섯 농장은 비교적 명확한 작업 사이클을 가집니다.

1. 생육동(Room)이 존재합니다.
2. 종균을 입상(in-date)하면 생육이 시작됩니다.
3. 약 5일 후 솎기(thinning)를 진행합니다.
4. 약 7일 후부터 수확(harvest)을 진행합니다.
5. 수확이 끝나면 퇴상(out-date)으로 사이클을 마무리합니다.

FarmFlow는 이 한 번의 생육 흐름을 하나의 `CultivationCycle`로 관리합니다.

즉, 이 시스템은 일반적인 농장 관리 기능보다 더 구체적으로, 버섯 농장의 운영 흐름을 소프트웨어로 모델링하는 데 초점을 두고 있습니다.

## 핵심 도메인

현재 기준 핵심 엔티티는 다음과 같습니다.

- `User`
- `Farm`
- `FarmUser`
- `Room`
- `CultivationCycle`
- `RoomReading`
- `RoomReadingImage`
- `Attendance`

권한 및 협업 구조를 위해 아래 엔티티도 함께 사용합니다.

- `Role`
- `RolePermission`
- `FarmInvitation`
- `RefreshToken`

## 도메인 정책

현재 프로젝트의 핵심 정책은 다음과 같습니다.

1. 유저는 회원가입 후 로그인합니다.
2. `OWNER` 권한 유저가 농장을 생성합니다.
3. 농장 생성 시 `farm_users` 테이블에 OWNER로 연결됩니다.
4. 하나의 농장은 여러 명의 `WORKER`를 가질 수 있습니다.
5. 한 유저는 여러 농장에 소속될 수 있습니다.
6. 시급은 사용자별로 저장하지 않고, 전역 설정값(예: 최저시급) 기준으로 계산합니다.

## 현재 구현 범위

현재 레포지토리 기준으로 아래 기능이 구현되어 있습니다.

### 인증 / 사용자

- 회원가입
- 로그인
- Refresh Token 기반 토큰 갱신
- JWT 인증/인가

### 농장 / 멤버십

- 농장 생성, 조회, 수정, 삭제
- 농장 소속 사용자(`FarmUser`) 관리
- 농장별 멤버 역할 변경
- 농장 멤버 제거

### 권한 관리

- 농장 단위 RBAC 구조
- 시스템 기본 역할 제공: `OWNER`, `WORKER`
- `Role` - `RolePermission` 확장 구조

### 초대 기능

- 농장 멤버 초대 생성
- 초대 승인
- 초대 목록 조회
- 초대 취소

### 운영 기능

- 생육동(Room) 등록/조회/수정/삭제
- 출근 / 퇴근 처리
- 개인 출퇴근 조회
- 관리자용 출퇴근 조회 및 수정

## 설계 중이거나 확장 예정인 기능

아래 기능은 도메인 모델이 포함되어 있거나, 다음 단계 구현 대상으로 보고 있습니다.

- `CultivationCycle` CRUD
- 생육 사이클 상태 전이: 입상 / 솎기 / 수확 / 퇴상
- `RoomReading` CRUD
- 환경 기록 필터 조회
- 환경 기록과 활성 생육 사이클 자동 연결
- 환경 데이터 + 생육 상태 통합 조회 API
- 테스트 코드 확장

## 설계 포인트

### 1. 농장 단위 멤버십 및 권한 구조

FarmFlow는 "사용자 1명 = 농장 1개" 구조가 아니라, 한 유저가 여러 농장에 소속될 수 있도록 설계했습니다.

- `User` 와 `Farm` 은 N:M 관계
- 이를 `FarmUser` 로 연결합니다
- 권한은 전역 Role이 아니라 농장 맥락에서 해석합니다

이 구조 덕분에 가족 농장, 공동 운영, 향후 외부 농장 확장까지 대응할 수 있습니다.

### 2. 농장 단위 RBAC

권한은 단순히 `USER`, `ADMIN` 같은 전역 역할이 아니라, 농장 안에서의 역할로 설계했습니다.

- 어떤 농장에서는 `OWNER`
- 다른 농장에서는 `WORKER`

로 동작할 수 있습니다.

또한 시스템 기본 역할 외에도 향후 커스텀 역할 확장을 고려해 `Role`, `RolePermission` 구조를 두었습니다.

### 3. 실제 운영 흐름 중심 도메인 모델링

FarmFlow의 핵심은 생육동별로 반복되는 생산 사이클을 추적하는 것입니다.

- 생육동이 있고
- 특정 날짜에 입상되고
- 일정 시점에 솎기/수확/퇴상이 일어나며
- 그 기간 동안 환경 데이터와 작업 이력이 쌓입니다

이 흐름을 `CultivationCycle` 중심으로 묶어 관리하는 방향으로 설계했습니다.

## 기술 스택

- Java 17
- Spring Boot 4
- Spring Data JPA
- Spring Security
- JWT
- MySQL 8
- Docker / Docker Compose
- Springdoc OpenAPI (Swagger UI)
- Lombok

## API 베이스 경로

모든 REST API는 아래 prefix를 사용합니다.

```text
/api/v1
```

예시:

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/farms`
- `POST /api/v1/farms/{farmId}/invitations`

## 실행 방법

### 1. MySQL 실행

로컬 개발 환경에서는 Docker Compose를 사용합니다.

```bash
docker compose up -d
```

기본 설정은 다음과 같습니다.

- MySQL Port: `33065`
- Database: `farm-flow`
- Username: `root`

### 2. 애플리케이션 설정 확인

`src/main/resources/application.yml` 기준으로 아래 설정이 필요합니다.

- MySQL datasource
- JWT secret
- SMTP mail 설정
- Refresh Token cookie 설정
- CORS 허용 origin 설정

실제 운영이나 협업 환경에서는 민감한 값을 `application.yml`에 직접 두기보다, 환경변수 또는 별도 설정 파일로 분리하는 방식을 권장합니다.

### 3. 서버 실행

```bash
./gradlew bootRun
```

기본 포트:

```text
http://localhost:4000
```

### 4. API 문서 확인

Swagger UI:

[http://localhost:4000/swagger-ui/index.html](http://localhost:4000/swagger-ui/index.html)

## 예시 사용 흐름

FarmFlow의 기본 사용 흐름은 아래와 같습니다.

1. 사용자가 회원가입 후 로그인합니다.
2. OWNER 사용자가 농장을 생성합니다.
3. 농장 생성과 함께 `farm_users`에 OWNER 멤버십이 생성됩니다.
4. OWNER가 작업자를 초대합니다.
5. 작업자가 초대를 승인해 농장 멤버가 됩니다.
6. 농장 내에서 생육동, 생육 사이클, 환경 기록, 출퇴근 데이터를 관리합니다.

## 앞으로 만들고 싶은 방향

FarmFlow는 최종적으로 아래 방향을 목표로 합니다.

- 부모님 농장에서 실제로 사용할 수 있는 운영 도구
- 모바일 PWA 기반 현장 중심 UX
- 생육 사이클과 환경 데이터를 연결한 기록 시스템
- 출퇴근과 작업 데이터를 바탕으로 운영 보조 기능 제공
- 장기적으로는 농장 운영 데이터 기반 의사결정 지원

## 한 줄 요약

FarmFlow는 새송이 버섯 농장의 실제 생육 흐름과 작업 운영을 관리하기 위해 개발 중인 Spring Boot 기반 농장 관리 백엔드 프로젝트입니다.
