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

