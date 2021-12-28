# Problems and Solutions

---

## 회원가입 시 보안을 위해 비밀번호 암호화 작업
### 1. 문제정의
- 암호화를 하지 않고 비밀번호를 그대로 DB에 넣었을 경우, 보안 문제가 발생

### 2. 사실수집
- POSTMAN을 실행하고 회원가입에 대한 필요정보를 입력한 후, 회원가입 주소를 입력한 후 POST메서드로 전송을 할 경우 DB에 입력받은 비밀번호가 그대로 저장되는 것을 확인
- `SQL injection`과 같은 공격이 발생했을 경우 암호화를 하지 않으면 사용자의 비밀번호가 그대로 노출되는 상황이 발생할 것으로 예상

### 3. 원인추론
- 암호화 과정을 수행하는 코드 미작성

### 4. 조사방법결정
- 스프링 시큐리티 프레임워크에서 제공하는 클래스 중 하나인 BCryptPasswordEncoder를 활용한 비밀번호 암호화 구현

### 5. 조사방법구현
``` Java
// 회원가입 (아이디, 비밀번호, 이름, 닉네임, 휴대폰번호, 우편번호, 우편주소, 회원가입날짜, 프로필사진)
// POST > http://localhost:9090/REST/api/member/join
@RequestMapping(value = "/join", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public Map<String, Object> memberJoinPost(@ModelAttribute Member member,
        @RequestParam(name = "file") MultipartFile[] file) throws IOException {
    Map<String, Object> map = new HashMap<>();
    try {
        // 비밀번호 암호화
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        member.setPassword(bcpe.encode(member.getPassword()));

        // 이미지 설정
        member.setImage(file[0].getBytes());
        member.setImagesize(file[0].getSize());
        member.setImagetype(file[0].getContentType());

        // joinMember메소드 호출 => 회원가입 수행
        int result = mService.joinMember(member);
        if (result == 1) {
            map.put("result", 1L);
            map.put("data", "회원가입을 성공했습니다.");
        } else {
            map.put("result", 0L);
            map.put("data", "회원가입을 실패했습니다.");
        }
    } catch (Exception e) {
        // 에러를 출력한다.
        e.printStackTrace();
        map.put("result", 0L);
        map.put("data", "회원가입을 실패했습니다.");
    }
    // 결과 값 리턴
    return map;
}
```
- `BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();`를 입력하여 bcpe객체를 생성하고, `bcpe.encode(member.getPassword())`를 통해 입력받은 스트링을 암호화수행
- BCryptPasswordEncoder는 BCrypt해싱 함수를 사용해서 비밀번호를 인코딩해주는 메서드와 사용자에 의해 제출된 비밀번호와 저장소에 저장되어 있는 비밀번호의 일치 여부를 확인해주는 메서드를 제공한다.
- PasswordEncoder 인터페이스를 구현한 클래스이다.
- 생성자의 인자 값(version, strength, SecureRandom instance)을 통해 해시의 강도를 조절할 수 있다.
- 자세한 내용은 블로그 내 정리: https://velog.io/@newtownboy/SpringBCryptPasswordEncoder

### 6. 문제해결
- BCryptPasswordEncoder의 encode메서드를 활용하여 입력받은 문자열을 암호화하는 과정을 수행한 후 DB에 저장
- **[적용 후 사진]**

![image](https://user-images.githubusercontent.com/38236367/147587902-e4e7d616-2374-4807-93f4-0534137f5268.png)

---

## 회원가입 시 보안을 위해 비밀번호 암호화 작업
### 1. 문제정의
- 암호화를 하지 않고 비밀번호를 그대로 DB에 넣었을 경우, 보안 문제가 발생

### 2. 사실수집
- POSTMAN을 실행하고 회원가입에 대한 필요정보를 입력한 후, 회원가입 주소를 입력한 후 POST메서드로 전송을 할 경우 DB에 입력받은 비밀번호가 그대로 저장되는 것을 확인
- `SQL injection`과 같은 공격이 발생했을 경우 암호화를 하지 않으면 사용자의 비밀번호가 그대로 노출되는 상황이 발생할 것으로 예상

### 3. 원인추론
- 암호화 과정을 수행하는 코드 미작성

### 4. 조사방법결정
- 스프링 시큐리티 프레임워크에서 제공하는 클래스 중 하나인 BCryptPasswordEncoder를 활용한 비밀번호 암호화 구현

### 5. 조사방법구현
``` Java
// 회원가입 (아이디, 비밀번호, 이름, 닉네임, 휴대폰번호, 우편번호, 우편주소, 회원가입날짜, 프로필사진)
// POST > http://localhost:9090/REST/api/member/join
@RequestMapping(value = "/join", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public Map<String, Object> memberJoinPost(@ModelAttribute Member member,
        @RequestParam(name = "file") MultipartFile[] file) throws IOException {
    Map<String, Object> map = new HashMap<>();
    try {
        // 비밀번호 암호화
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        member.setPassword(bcpe.encode(member.getPassword()));

        // 이미지 설정
        member.setImage(file[0].getBytes());
        member.setImagesize(file[0].getSize());
        member.setImagetype(file[0].getContentType());

        // joinMember메소드 호출 => 회원가입 수행
        int result = mService.joinMember(member);
        if (result == 1) {
            map.put("result", 1L);
            map.put("data", "회원가입을 성공했습니다.");
        } else {
            map.put("result", 0L);
            map.put("data", "회원가입을 실패했습니다.");
        }
    } catch (Exception e) {
        // 에러를 출력한다.
        e.printStackTrace();
        map.put("result", 0L);
        map.put("data", "회원가입을 실패했습니다.");
    }
    // 결과 값 리턴
    return map;
}
```
- `BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();`를 입력하여 bcpe객체를 생성하고, `bcpe.encode(member.getPassword())`를 통해 입력받은 스트링을 암호화수행
- BCryptPasswordEncoder는 BCrypt해싱 함수를 사용해서 비밀번호를 인코딩해주는 메서드와 사용자에 의해 제출된 비밀번호와 저장소에 저장되어 있는 비밀번호의 일치 여부를 확인해주는 메서드를 제공한다.
- PasswordEncoder 인터페이스를 구현한 클래스이다.
- 생성자의 인자 값(version, strength, SecureRandom instance)을 통해 해시의 강도를 조절할 수 있다.
- 자세한 내용은 블로그 내 정리: https://velog.io/@newtownboy/SpringBCryptPasswordEncoder

### 6. 문제해결
- BCryptPasswordEncoder의 encode메서드를 활용하여 입력받은 문자열을 암호화하는 과정을 수행한 후 DB에 저장
- **[적용 후 사진]**

![image](https://user-images.githubusercontent.com/38236367/147587902-e4e7d616-2374-4807-93f4-0534137f5268.png)
