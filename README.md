# 이메일 인증 기능 환경세팅
이메일 인증 기능 사용하려면 아래 내용을 진행하셔야 합니다.

## 1. 2단계 인증 활성화
1. [Google 계정](https://myaccount.google.com/)에 로그인합니다.
2. **보안** 탭으로 이동합니다.
3. **2단계 인증**을 선택하여 활성화합니다.
4. 2단계 인증을 설정하는 과정에서는 보통 문자 메시지나 인증 앱을 통해 인증을 진행합니다.

## 2. 애플리케이션 비밀번호 생성
1. 2단계 인증이 활성화되면, **앱 비밀번호**라는 옵션이 나타납니다.
2. **앱 비밀번호**를 클릭하여 비밀번호를 생성합니다.
3. **앱 선택**에서 `메일`을 선택하고, **기기 선택**에서 사용하는 기기를 선택합니다.
4. **생성** 버튼을 클릭하면 16자리의 애플리케이션 비밀번호가 생성됩니다.
5. 이 비밀번호를 복사하여 애플리케이션에서 사용합니다.

> **주의:** 애플리케이션 비밀번호는 **공백 없이** 16자리 문자열로 제공됩니다. 복사한 후 공백 없이 사용해야 하며, 공백이 포함되지 않도록 주의하세요.

## 3. Spring Boot 애플리케이션 설정
생성된 애플리케이션 비밀번호를 `application.properties` 파일에 입력합니다.

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-application-password  # 애플리케이션 비밀번호 사용
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
