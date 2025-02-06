# 🚀 카카오 & 구글 로그인/로그아웃 흐름 (Spring Security OAuth2 미사용)

이 프로젝트는 **Spring Security OAuth2**를 사용하지 않고, 직접 OAuth 인증 흐름을 구현한 방식입니다. 카카오와 구글 로그인 및 로그아웃 기능을 수동으로 처리하며, JWT 토큰을 생성하여 사용자 인증을 관리합니다.

## 주요 특징

1. **OAuth2 토큰 발급**  
   카카오와 구글의 인증 코드를 받아 액세스 토큰을 요청합니다. Spring Security의 OAuth2 기능을 사용하지 않고, **직접 토큰 발급**을 처리합니다.

2. **JWT 토큰 생성**  
   인증 후, 사용자 정보를 기반으로 JWT 토큰을 생성하고 이를 **쿠키에 저장**하여 로그인 상태를 유지합니다.

3. **쿠키 관리**  
   `CookieUtil` 클래스를 사용하여 쿠키를 **직접 생성 및 삭제**하며, 로그인 및 로그아웃 상태를 관리합니다.

4. **세션 관리**  
   `HttpServletRequest`와 `HttpServletResponse`를 이용하여 세션과 쿠키를 **수동으로 관리**합니다.


**Spring Security OAuth2**를 사용하지 않고, OAuth2 인증 흐름을 **수동으로 구현**한 방식입니다.


## 🚀 카카오 & 구글 로그인/로그아웃 흐름

이 프로젝트는 카카오와 구글을 이용한 로그인 및 로그아웃 기능을 구현한 웹 애플리케이션입니다. 아래는 각 흐름에 대한 간단한 설명입니다.

### 1. 카카오 로그인

- **로그인 요청**  
  사용자가 카카오 로그인 버튼을 클릭하면 카카오는 인증 코드를 포함한 리디렉션 URL을 반환합니다.

- **인증 코드 처리**  
  반환된 인증 코드를 받아서 `KakaoAuthService`에서 카카오의 토큰을 요청합니다.

- **사용자 정보 요청**  
  카카오는 액세스 토큰을 반환하고, 이를 사용해 카카오 사용자 정보를 가져옵니다.

- **JWT 토큰 생성**  
  가져온 사용자 정보로 JWT 토큰을 생성하고 이를 사용자에게 전달합니다.

- **쿠키에 토큰 저장**  
  생성된 JWT 토큰을 쿠키에 저장하여 로그인 상태를 유지합니다.

### 2. 구글 로그인

- **로그인 요청**  
  사용자가 구글 로그인 버튼을 클릭하면 구글은 인증 코드를 포함한 리디렉션 URL을 반환합니다.

- **인증 코드 처리**  
  반환된 인증 코드를 받아서 `GoogleAuthService`에서 구글의 토큰을 요청합니다.

- **사용자 정보 요청**  
  구글은 액세스 토큰을 반환하고, 이를 사용해 구글 사용자 정보를 가져옵니다.

- **JWT 토큰 생성**  
  가져온 사용자 정보로 JWT 토큰을 생성하고 이를 사용자에게 전달합니다.

- **쿠키에 토큰 저장**  
  생성된 JWT 토큰을 쿠키에 저장하여 로그인 상태를 유지합니다.

### 3. 카카오 로그아웃

- **로그아웃 요청**  
  사용자가 카카오 로그아웃 버튼을 클릭하면 카카오는 로그아웃 처리를 위한 요청을 받고 세션을 종료합니다.

- **쿠키 삭제**  
  저장된 카카오 JWT 토큰을 쿠키에서 삭제하고 세션을 무효화하여 로그아웃 상태로 만듭니다.

### 4. 구글 로그아웃

- **로그아웃 요청**  
  사용자가 구글 로그아웃 버튼을 클릭하면 구글은 로그아웃 처리를 위한 요청을 받고 세션을 종료합니다.

- **쿠키 삭제**  
  저장된 구글 JWT 토큰을 쿠키에서 삭제하고 세션을 무효화하여 로그아웃 상태로 만듭니다.

---

위 흐름을 통해 카카오와 구글의 로그인 및 로그아웃 처리가 이루어지며, 사용자는 JWT 토큰을 이용해 인증 상태를 유지하거나 로그아웃을 통해 인증 상태를 종료할 수 있습니다.

