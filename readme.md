🔑 **JWT 인증 방식**
--
+ **회원가입** ("/api/jwt/signup")


+ **로그인** ("/api/jwt/login")
  + 로그인 성공 시 JWT 토큰 발급


+ **인증된 사용자만 접근 가능** (/api/jwt/info)
  + Authorization 헤더에 Bearer + <JWT 토큰> 추가 시 사용자 정보를 반환


+ **관리 페이지** (/api/jwt/admin)
     + 추후 Role 기반 인증 추가 필요

____

🚀 **API 명세**
--
### 1️⃣ 회원가입 API

+ **POST** /api/jwt/signup

요청 예시

>{
"username": "testuser",
"email": "test@example.com",
"password": "password123"
}

응답 예시

>"회원가입 성공"

### 2️⃣ 로그인 API

+ **POST** /api/jwt/login

요청 예시

>{
"username": "testuser",
"password": "password123"
}

응답 예시 (JWT 토큰 반환)

>"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

### 3️⃣ 사용자 정보 조회 API

+ **GET** /api/jwt/info

헤더에 Authorization: Bearer <JWT 토큰> 추가 필요

응답 예시

>{
"id": 1,
"username": "testuser",
"email": "test@example.com"
}

### 4️⃣ 관리자 페이지 API

+ **GET** /api/jwt/admin (관리자 권한이 있는 경우 접근 가능)

응답 예시

>"관리자 페이지 접근 성공"

