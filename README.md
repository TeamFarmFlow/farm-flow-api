# FarmFlow

FarmFlow는 실제 버섯 농장을 관리하기 위한 농장 관리 시스템 입니다.  
농장 단위의 사용자 관리, 권한 관리, 초대 시스템 등을 중심으로 설계되었습니다.

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
## 2. 주요 기능

### 사용자 관리
- 회원가입 / 로그인 (JWT 기반 인증)
- 사용자 상태 관리

### 2. 농장 관리
- 농장 생성
- 농장 단위 사용자 관리 (FarmUser)

### 3. 권한 관리 (RBAC)
- 농장 단위 Role 기반 권한 구조
- 기본 역할: `OWNER`, `WORKER`
- Role - Permission 구조 설계 및 확장 가능

### 4. 농장 초대 기능
- 이메일 기반 초대
- 초대 코드(6자리) 발급 및 검증

### 5. 도메인 구조
- User
- Farm
- FarmUser
- Role
- RolePermission
- Room
- CultivationCycle
- RoomReading
- Attendance
