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

## 로그인 시 토큰 발행 문제
### 1. 문제정의
- 로그인 후, 사용자 인증을 위한 토큰 발행 문제가 발생

### 2. 사실수집
- 요청과 응답에 토큰을 함께 보내 이 사용자가 유효한 사용자인지를 판단하기 위해 토큰을 사용해야한다.

### 3. 원인추론
- 토큰을 발행하기 위한 코드 미작성

### 4. 조사방법결정
- Json Web Token(JWT)의 개념과 사용방법에 대해 조사 및 프로젝트에 적용

### 5. 조사방법구현
``` Java
@Service
public class JwtUtil {
    private final String SECRETKEY = "2asdjklajdlas";

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public String generateToken(String memberid) {
        long tokenValidTime = 1000 * 60 * 60 * 24; // 토큰 유효시간 설정

        Map<String, Object> claims = new HashMap<>();
        String token = Jwts.builder().setClaims(claims).setSubject(memberid)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, SECRETKEY).compact();

        return token;
    }

    // 토큰에서 해당 아이디 정보 가져오기
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 토큰에서 만료시간 가져오기
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 토큰의 만료시간이 유효한지 확인
    public Boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    // 토큰 유효성 확인
    public Boolean validateToken(String token, String memberid) {
        final String username = this.extractUsername(token);

        if (username.equals(memberid) && !isTokenExpired(token)) {
            return true;
        }
        return false;
    }
}
```

### 6. 문제해결
- JWT를 도입함으로써, 사용자 인증뿐만 아니라 기존 쿠키/세션을 사용하는 방식보다 많은 보안 이슈를 막을 수 있다.
=> 서버는 클라이언트로부터 받은 JWT가 유효할 경우, 리소스를 사용하도록 허가하고 쿠키를 사용하지 않기 때문에 CORS이슈가 발생하지 않는다.

**[토큰 기반 인증 동작방식]**
- 클라이언트가 아이디와 비밀번호를 서버에게 전달하며 인증 요청
- 서버는 아이디와 비밀번호를 통해 유효한 사용자인지 검증하고, 유효한 사용자인 경우 토큰을 생성해서 응답
- 클라이언트는 토큰을 저장해두었다가, 인증이 필요한 api에 요청할 때 토큰 정보와 함께 요청
- 서버는 토큰이 유효한지 검증하고, 유효한 경우 응답

**[토큰 기반 인증 방식의 특징]**
- 무상태성: 사용자의 인증 정보가 담겨있는 토큰을 클라이언트에 저장하기 때문에 서버에서 별도의 저장소가 필요없어 완전한 무상태를 가질 수 있다.
- 확장성: 토큰 기반 인증을 사용하는 다른 시스템에 접근이 가능하다.
- 무결성: HMAC(Hash-based Message Authentication) 기법이라고 불리며, 발급 후 토큰의 정보를 변경하는 행위는 불가능하다.
- 보안성: 클라이언트가 서버에 요청을 할 때, 쿠키를 전달하지 않기 때문에 쿠키의 취약점은 사라진다.

---

## 이메일 인증코드 전송
### 1. 문제정의
- 회원가입 시, 유효한 이메일인지 확인하는 절차가 필요
- 비밀번호 찾기, 비밀번호 변경 기능 이용 시 이메일 인증을 통해 찾거나 변경하도록 설정

### 2. 사실수집
- 인증번호 발송 후 DB에 인증번호를 저장해두었다가 제한 시간 내에 옳게 입력했을 경우 검증완료, 그렇지 않을 경우 재발송

### 3. 원인추론
- 이메일 인증코드를 발송하기 위한 코드 미작성

### 4. 조사방법결정
- Spring 이메일 인증 구현 방법 조사 및 프로젝트 내 적용

### 5. 조사방법구현
``` Java
@Service
@AllArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    MemberRepository mRepository;

    private JavaMailSender jMailSender;
    private static final String FROM_ADDRESS = "admin@gmail.com";

    @Override
    public Mail createMailAndConfirmEmail(String memberId) {
        // getTempEmailCode메소드를 호출하여, 이메일 인증코드를 저장한다.
        String tempEmailCode = getTempEmailCode();

        // Mail객체를 생성하고, 속성을 설정한다.
        Mail mail = new Mail();
        mail.setAddress(memberId);
        mail.setTitle("이메일 인증코드 발송입니다. 확인해주세요.");
        mail.setMessage("안녕하세요. 이메일을 검증하기 위한 인증코드입니다. 3분이내에 등록해주세요." + "[" + tempEmailCode + "]");

        // 이전에 이메일 코드를 보낸 사용자인지 확인(1일 경우 수정, 0일경우 등록)
        int result = mRepository.queryCheckEmailCode(memberId);

        // 현재시간을 기준으로 3분이후 시간 설정
        Date curDate = new Date();
        Calendar calendar = Calendar.getInstance();

        // 현재 시간을 설정하고, 3분을 추가한 후 변수에 넣기
        calendar.setTime(curDate);
        calendar.add(Calendar.MINUTE, 3);
        Date validDate = calendar.getTime();
        System.out.println(validDate);

        if (result == 1) {
            // queryUpdateEmailCode메소드 호출(인증코드, 만료시간을 DB에 업데이트한다.)
            mRepository.queryUpdateEmailCode(memberId, tempEmailCode, validDate);
        } else {
            // querySaveEmailCode메소드 호출(회원 아이디, 인증코드, 만료시간을 DB에 저장한다.)
            mRepository.querySaveEmailCode(memberId, tempEmailCode, validDate);
        }
        return mail;
    }

    @Override
    public Mail createMailAndChangePassword(String memberId, String memberName) {
        // getTempPassword메소드를 호출하여, 임시 비밀번호를 저장한다.
        String tempPw = getTempPassword();

        // Mail객체를 생성하고, 속성을 설정한다.
        Mail mail = new Mail();
        mail.setAddress(memberId);
        mail.setTitle(memberName + "님의 임시비밀번호 안내 이메일 입니다.");
        mail.setMessage("안녕하세요. 임시비밀번호 안내 이메일 입니다." + "[" + memberName + "]" + "님의 임시 비밀번호는 " + tempPw + " 입니다.");

        // updatePassword메소드 호출
        updatePassword(memberId, tempPw);
        return mail;
    }

    @Override
    public void updatePassword(String memberId, String memberPw) {
        // 비밀번호 암호화
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        String hashMemberPw = bcpe.encode(memberPw);

        // 아이디를 조건으로 맴버객체 조회
        Member member = mRepository.findById(memberId).get();
        mRepository.queryUpdatePW(member.getId(), hashMemberPw);
    }

    @Override
    public String getTempEmailCode() {
        // 이메일 인증코드 생성을 위한 숫자 배열
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        // 배열을 접근하기 위한 인덱스, 이메일 인증코드를 저장하기 위한 변수
        int index = 0;
        String tempEmailCode = "";

        // 배열의 랜덤한 요소를 뽑아 4자리 비밀번호를 생성
        for (int i = 0; i < 4; i++) {
            index = (int) (charSet.length * Math.random());
            tempEmailCode += charSet[index];
        }
        // 결과 값 리턴
        return tempEmailCode;
    }

    @Override
    public String getTempPassword() {
        // 임시 비밀번호를 위한 문자, 숫자 배열
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        // 배열을 접근하기 위한 인덱스, 임시 비밀번호를 저장하기 위한 변수
        int index = 0;
        String tempPw = "";

        // 배열의 랜덤한 요소를 뽑아 10자리 비밀번호를 생성
        for (int i = 0; i < 10; i++) {
            index = (int) (charSet.length * Math.random());
            tempPw += charSet[index];
        }
        // 결과 값 리턴
        return tempPw;
    }

    @Override
    public void mailSend(Mail mail) {
        // 메세지를 보내기위한 객체 생성 및 설정
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setFrom(SendEmailServiceImpl.FROM_ADDRESS);
        message.setSubject(mail.getTitle());
        message.setText(mail.getMessage());

        // 메일 전송
        jMailSender.send(message);
    }

    @Override
    public int validEmailCode(String memberId, String emailCode, Date curDate) {
        int result = mRepository.queryValidEmailCode(memberId, emailCode, curDate);
        return result;
    }
}
```

### 6. 문제해결
- `createMailAndConfirmEmail`: 이메일 인증코드를 생성하는 메소드를 호출하고, 제목, 내용, 유효시간을 설정하여 DB에 저장한다. 리턴으로 메일 객체를 되돌려준다.
- `getTempEmailCode`: 임시 이메일 인증코드를 생성하기 위한 메서드로 배열 내 숫자를 랜덤으로 조합하여 인증코드를 생성한다.
- `getTempPassword`: 임시 이메일 인증코드와 전체적인 로직은 동일하고, 문자를 추가하였다.
- `mailSend`: 인자로 받은 메일 객체를 실제 메세지에 내용을 저장하고 `JavaMailSender`를 통해 메일을 보낸다. 실제 메일을 전송하기 위한 메소드, 이메일 주소, 전송자, 제목, 내용등을 속성으로 갖는다.
