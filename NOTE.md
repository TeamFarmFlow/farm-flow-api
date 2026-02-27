# 회원가입 시

## 1안

- 계정만 생성
- 농장 등록

- CASE 1) 농장주
  - 회원가입 페이지 진입
  - 회원가입 완료 
  - 내 농장 목록에서 농장 등록

- CASE 2) 근로자
  - 회원가입 페이지 진입
  - 농장 목록 검색 조회
  - 농장 선택 후 회원가입 진행

## 2안

- 계정 생성과 농장 등록을 한번에

- CASE 1) 농장주
    - 회원가입 페이지 진입
    - 회원가입 진행(상태 : 농장 입력 필요)
    - 농장 등록(상태 : 회원가입 완료)

- CASE 2) 근로자
    - 회원가입 페이지 진입
    - 농장 목록 검색 조회
    - 농장 선택 후 회원가입 진행


## 3안

- 초대

- CASE 1) 농장주
    - 회원가입 완료
    - 내 농장 목록에서 농장 등록
    - 근로자에게 초대 코드 발송(이메일로)

- CASE 2) 근로자
    - 이메일로 초대 코드 받아서
    - 해당 코드로 회원가입 진행
    - 그러면 알아서 해당 농장의 근로자로 계정 생성


## JWT 갱신

- JWT 발급(로그인 또는 회원가입)
  - accessToken 발급(만료시간 : 10분)
  - refreshToken 발급(만료시간 : 20일)
  - refreshToken 저장(Redis, MySQL)
  - accessToken은 Response로 주고
  - refreshToken은 Request.setCookie("", "");
    (이때, 쿠키 설정은 httpOnly, Secure, SameSite)

- JWT 갱신(토큰 만료되었을 때)
  - refreshToken 유효성 검사(Redis, MySQL)
  - 있으면? accessToken 재발급 후 해당 refreshToken 삭제
  - refreshToken 신규 발급 후 Cookie에 넣어서 응답
