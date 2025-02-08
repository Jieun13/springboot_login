## 🚀 OAuth2 로그인 구현 (구글, 카카오)

이 프로젝트에서는 **구글**과 **카카오**의 OAuth2 로그인을 구현하고, 로그인 시 발급된 **access_token**을 `localStorage`에 저장하며, **refresh_token**을 쿠키에 저장하여 인증 상태를 유지합니다.

## 구현 내용

### 1. 로그인 프로세스

- **구글/카카오 로그인**: 사용자가 구글 또는 카카오 계정을 통해 로그인하면, `OAuth2` 인증 방식으로 로그인 정보를 가져옵니다.
- **Access Token**: 로그인 성공 시, 서버에서 `access_token`을 발급하여 `localStorage`에 저장합니다.
- **Refresh Token**: `refresh_token`은 쿠키에 저장되어, 서버와의 세션을 유지할 수 있도록 합니다.

### 2. `Access Token`과 `Refresh Token`의 역할

- **Access Token**: 서버와 클라이언트 간의 인증을 처리하는 데 사용되며, 주로 API 요청 시 사용됩니다. `localStorage`에 저장됩니다.
- **Refresh Token**: `access_token`이 만료되었을 때, 서버로부터 새로운 `access_token`을 발급받기 위해 사용됩니다. 이 값은 `쿠키`에 저장됩니다.

### 3. 로그인 시 처리 흐름

1. **로그인 성공**: 사용자가 구글 또는 카카오 계정으로 로그인하면, 인증 정보를 서버로 전달합니다.
2. 서버에서 `access_token`과 `refresh_token`을 생성합니다.
3. **Access Token**은 클라이언트의 `localStorage`에 저장됩니다.
4. **Refresh Token**은 클라이언트의 `쿠키`에 저장됩니다.

### 4. 로그아웃 처리

1. **로그아웃 클릭**: 사용자가 로그아웃을 클릭하면, `localStorage`에 저장된 `access_token`이 삭제됩니다.
2. `refresh_token`은 서버에서 관리됩니다.

### (+) OAuth2 로그인 흐름

1. 리프레시 토큰 생성 및 저장 -> 쿠키에 저장
2. 액세스 토큰 생성 및 localStorage에 추가할 URL로 리다이렉트
3. 리다이렉트 전략 실행