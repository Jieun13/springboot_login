
🚀 1. 카카오 로그인 + JWT 인증 구조
---
1. 클라이언트(프론트엔드)에서 카카오 로그인 버튼 클릭
2. 카카오 OAuth 서버에서 인가 코드(code) 발급
3. 클라이언트에서 받은 인가 코드를 백엔드로 전달
4. 백엔드에서 카카오 OAuth 서버로 토큰 요청하여 access_token 수신
5. 백엔드에서 access_token을 이용해 카카오 사용자 정보 요청
6. 사용자 정보를 바탕으로 자체적인 JWT 생성 및 반환
7. 이후 클라이언트는 JWT를 Authorization: Bearer <토큰> 형태로 API 요청


### 카카오 로그인 흐름

- 프론트엔드 → 인가 코드 요청
- 프론트엔드 → 백엔드로 인가 코드 전달 (KakaoLoginRequest)
- 백엔드 → 카카오 API에서 access_token 요청 (KakaoTokenResponse)
- 백엔드 → 사용자 정보 요청 (KakaoUserResponse)
- 백엔드 → JWT 발급 후 클라이언트에 응답
----

## **📌 백엔드만 구현한 경우 카카오 로그인 테스트하는 방법**


### **1️⃣ 카카오 로그인 URL에 직접 접속해서 인가 코드 받기**
### **✅ 브라우저 주소창에서 아래 URL로 이동**
```plaintext
https://kauth.kakao.com/oauth/authorize?client_id=YOUR_CLIENT_ID&redirect_uri=YOUR_REDIRECT_URI&response_type=code
```
🔹 **필수 값**
- `client_id`: 카카오 개발자 콘솔에서 확인한 **REST API 키**
- `redirect_uri`: 카카오에서 **인가 코드를 받을 백엔드 URL** (예: `http://localhost:8080/auth/login/kakao`)

#### **📝 예시 (`client_id`를 실제 값으로 변경)**
```plaintext
https://kauth.kakao.com/oauth/authorize?client_id=abcd1234efgh5678&redirect_uri=http://localhost:8080/auth/login/kakao&response_type=code
```
### **✅ 실행 결과**
1. 해당 URL로 이동하면 카카오 로그인 페이지가 뜸.
2. 로그인 후 **인가 코드가 포함된 URL로 리디렉트됨**
    - 예: `http://localhost:8080/auth/login/kakao?code=abcdefg123456`
3. **인가 코드(`code=abcdefg123456`)를 복사해두기!**
    - 이걸 다음 단계에서 API 테스트할 때 사용함.

---

### **2️⃣ Postman으로 백엔드 API 테스트하기**

### **✅ `GET /auth/login/kakao?code=YOUR_AUTH_CODE` 요청**
1. **Postman 실행 후, 새 요청 생성**
    - `GET http://localhost:8080/auth/login/kakao?code=abcdefg123456`
2. **전송 (Send) 버튼 클릭**
3. 응답 확인
    - 성공하면 JWT 토큰이 반환됨.
    - 실패하면 에러 메시지 확인.

#### **📝 예제 요청 (`abcdefg123456`는 1단계에서 받은 인가 코드)**
```plaintext
GET http://localhost:8080/auth/login/kakao?code=abcdefg123456
```

---

### **✅ `POST /auth/login/kakao` 요청 (POST 방식일 경우)**
1. **Postman 실행 후, 새 요청 생성**
    - `POST http://localhost:8080/auth/login/kakao`
2. **Body → raw → JSON 선택 후, 아래 데이터 입력**
```json
{
    "code": "abcdefg123456"
}
```
3. **전송 (Send) 버튼 클릭**
4. 응답 확인
    - 정상적으로 로그인되면 **JWT 토큰**이 반환됨.


---

## **✅ 최종 정리**
1️⃣ **브라우저 주소창에 카카오 로그인 URL 입력**
- 로그인 후, `code=abcdefg123456` 값을 받아오기.  

2️⃣ **Postman에서 `GET` 또는 `POST` 요청으로 백엔드 API 테스트**
- `http://localhost:8080/login/auth/login/kakao?code=abcdefg123456`
- 또는 `{ "code": "abcdefg123456" }` JSON 데이터로 `POST` 요청.  

3️⃣ **응답에서 JWT 토큰 확인**
- 정상 작동하면 백엔드가 제대로 구현된 것! 🚀
