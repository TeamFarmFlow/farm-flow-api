# Attendance MVP Design

## 목표

FarmFlow 출퇴근 MVP는 버튼 기반 출근/퇴근 기록을 중심으로 구현한다.

- 근로자는 본인 출근/퇴근을 기록할 수 있다.
- 근로자는 본인 출퇴근 기록을 조회할 수 있다.
- 농장주 및 매니저는 소속 근로자의 출퇴근 기록을 조회할 수 있다.
- 농장주 및 매니저는 소속 근로자의 출퇴근 시간을 수정할 수 있다.
- 수정 이력은 별도 테이블로 남긴다.

QR 기반 출퇴근은 MVP 범위에서 제외하고, 이후 확장 기능으로 분리한다.

## 현재 코드 기준 권한 모델

이미 출퇴근 관리용 권한으로 아래 항목을 사용한다.

- `ATTENDANCE_MANAGE`

출근/퇴근 자체는 권한으로 분리하지 않고, 활성 상태의 농장 소속 사용자라면 누구나 가능하게 설계한다.
관리자 기능만 `FarmUser -> Role -> RolePermission` 구조를 활용한다.

권장 역할 정책:

- `OWNER`: 본인 출퇴근 + 소속 근로자 조회/수정 가능
- `MANAGER`: 본인 출퇴근 + 소속 근로자 조회/수정 가능
- `WORKER`: 본인 출근/퇴근/조회 가능

권장 권한 매핑:

- `OWNER`: `ATTENDANCE_MANAGE`
- `MANAGER`: `ATTENDANCE_MANAGE`
- `WORKER`: 별도 출퇴근 권한 없음

## 도메인 정책

### 출근

- 사용자는 본인이 속한 농장에서만 출근할 수 있다.
- 같은 농장 기준으로 하루 1회만 출근할 수 있다.
- 이미 해당 날짜의 출근 기록이 있으면 중복 출근은 불가하다.

### 퇴근

- 퇴근은 같은 날짜의 출근 기록이 있을 때만 가능하다.
- 이미 퇴근한 기록은 다시 퇴근할 수 없다.

### 조회

- 근로자는 본인 기록만 조회할 수 있다.
- `ATTENDANCE_MANAGE` 권한이 있으면 소속 근로자 기록을 조회할 수 있다.
- 다른 농장 기록은 조회할 수 없다.

### 수정

- `ATTENDANCE_MANAGE` 권한이 있는 사용자만 수정할 수 있다.
- 수정 전/후 시간과 수정자는 반드시 저장한다.
- 수정 사유는 MVP에서 필수 처리하는 것을 권장한다.
- `clockOutAt`은 `clockInAt`보다 빠를 수 없다.

## 엔티티 제안

### 1. Attendance

역할:

- 하루 단위 출퇴근 기록의 본체

권장 필드:

- `id`
- `farmId`
- `userId`
- `workDate`
- `clockInAt`
- `clockOutAt`
- `status`
- `note`
- `createdAt`
- `updatedAt`

권장 상태값:

- `WORKING`: 출근 후 퇴근 전
- `COMPLETED`: 퇴근 완료

설계 메모:

- `workDate`는 조회와 중복 방지 기준이 된다.
- 시간은 `Instant`를 기본으로 저장하고, 화면 표시 시 로컬 타임존으로 변환한다.
- `note`는 향후 메모 용도로 열어두되 MVP에서는 없어도 된다.

### 2. AttendanceAdjustment

역할:

- 출퇴근 시간 수정 이력 저장

권장 필드:

- `id`
- `attendanceId`
- `beforeClockInAt`
- `beforeClockOutAt`
- `afterClockInAt`
- `afterClockOutAt`
- `reason`
- `adjustedBy`
- `adjustedAt`

설계 메모:

- 관리자가 직접 수정한 경우에만 생성한다.
- 이력을 남기면 추후 근태 이슈 대응이 쉬워진다.

## 테이블 스키마 초안

### attendances

```sql
create table attendances (
    id bigint not null auto_increment,
    farm_id bigint not null,
    user_id bigint not null,
    work_date date not null,
    clock_in_at datetime(6) null,
    clock_out_at datetime(6) null,
    status varchar(20) not null,
    note varchar(255) null,
    created_at datetime(6) not null,
    updated_at datetime(6) null,
    primary key (id),
    constraint uk_attendances_farm_user_work_date unique (farm_id, user_id, work_date),
    constraint fk_attendances_farm foreign key (farm_id) references farms (id),
    constraint fk_attendances_user foreign key (user_id) references users (id)
);
```

인덱스 권장:

- `(farm_id, work_date)`
- `(user_id, work_date)`

### attendance_adjustments

```sql
create table attendance_adjustments (
    id bigint not null auto_increment,
    attendance_id bigint not null,
    before_clock_in_at datetime(6) null,
    before_clock_out_at datetime(6) null,
    after_clock_in_at datetime(6) null,
    after_clock_out_at datetime(6) null,
    reason varchar(255) not null,
    adjusted_by bigint not null,
    adjusted_at datetime(6) not null,
    created_at datetime(6) not null,
    updated_at datetime(6) null,
    primary key (id),
    constraint fk_attendance_adjustments_attendance foreign key (attendance_id) references attendances (id),
    constraint fk_attendance_adjustments_user foreign key (adjusted_by) references users (id)
);
```

## JPA 엔티티 방향

현재 프로젝트는 모듈별 패키지 분리와 `BaseTimeEntity`를 사용하고 있다. 출퇴근도 같은 패턴으로 맞춘다.

권장 패키지 구조:

```text
com.example.app.attendance
  ├── application
  │   ├── AttendanceService.java
  │   └── command
  ├── domain
  │   ├── Attendance.java
  │   ├── AttendanceAdjustment.java
  │   ├── AttendanceRepository.java
  │   ├── AttendanceAdjustmentRepository.java
  │   ├── enums
  │   │   └── AttendanceStatus.java
  │   └── exception
  └── presentation
      ├── AttendanceController.java
      ├── request
      └── response
```

권장 연관관계:

- `Attendance` -> `Farm`: `ManyToOne(fetch = LAZY)`
- `Attendance` -> `User`: `ManyToOne(fetch = LAZY)`
- `AttendanceAdjustment` -> `Attendance`: `ManyToOne(fetch = LAZY)`
- `AttendanceAdjustment` -> `User(adjustedBy)`: `ManyToOne(fetch = LAZY)`

## API 초안

### 근로자용

#### 출근

`POST /farms/{farmId}/attendances/clock-in`

응답 예시:

```json
{
  "attendanceId": 1,
  "workDate": "2026-04-07",
  "clockInAt": "2026-04-07T08:31:15Z",
  "clockOutAt": null,
  "status": "WORKING"
}
```

#### 퇴근

`POST /farms/{farmId}/attendances/clock-out`

응답 예시:

```json
{
  "attendanceId": 1,
  "workDate": "2026-04-07",
  "clockInAt": "2026-04-07T08:31:15Z",
  "clockOutAt": "2026-04-07T18:04:50Z",
  "status": "COMPLETED"
}
```

#### 내 기록 조회

`GET /farms/{farmId}/attendances/me?from=2026-04-01&to=2026-04-30`

### 관리자용

#### 농장 출퇴근 기록 조회

`GET /farms/{farmId}/attendances?userId=10&from=2026-04-01&to=2026-04-30`

권장 필터:

- `userId`
- `from`
- `to`
- 추후 `status`

#### 출퇴근 시간 수정

`PATCH /farms/{farmId}/attendances/{attendanceId}`

요청 예시:

```json
{
  "clockInAt": "2026-04-07T08:30:00Z",
  "clockOutAt": "2026-04-07T18:10:00Z",
  "reason": "출근 버튼 누락으로 관리자 수정"
}
```

## 응답 DTO 권장 필드

공통 응답 필드:

- `attendanceId`
- `userId`
- `userName`
- `workDate`
- `clockInAt`
- `clockOutAt`
- `status`
- `isAdjusted`

수정 이력 상세 조회를 붙일 경우:

- `lastAdjustedAt`
- `lastAdjustedBy`
- `lastAdjustmentReason`

## 서비스 레벨 검증 체크리스트

- 해당 `farmId`가 활성 농장인지 확인
- 요청 사용자가 해당 농장 소속인지 확인
- 요청 권한이 충분한지 확인
- 중복 출근 여부 확인
- 퇴근 가능 상태인지 확인
- 수정 시 시간 역전 여부 확인
- 관리자 수정 대상이 같은 농장 소속인지 확인

현재 프로젝트의 `FarmUserRepository.existsByFarmIdAndUserIdAndPermissionKey(...)` 패턴은 관리자 조회/수정 권한 확인에 재사용하는 것이 가장 자연스럽다.

## 예외 초안

권장 예외:

- `AttendanceNotFoundException`
- `AlreadyClockedInException`
- `AttendanceClockInNotFoundException`
- `AlreadyClockedOutException`
- `AttendancePermissionDeniedException`
- `InvalidAttendanceTimeRangeException`

## 구현 순서 제안

1. `AttendanceStatus` enum 추가
2. `Attendance`, `AttendanceAdjustment` 엔티티 추가
3. Repository 추가
4. 출근/퇴근 서비스 구현
5. 본인 조회 API 구현
6. 관리자 조회 API 구현
7. 관리자 수정 API 구현
8. 수정 이력 저장
9. 테스트 작성

## QR 기능 확장 포인트

MVP 이후에는 아래 방향으로 확장 가능하다.

- `AttendanceQrToken` 엔티티 추가
- 1회성 토큰 발급
- 1분 만료
- `usedAt`, `usedBy` 저장

단, QR 기능은 출퇴근 본체 도메인을 흔들지 않도록 `Attendance`와 분리된 인증 수단으로 설계하는 것이 좋다.
