package capstone.skini.domain.mail.service;

import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MailService {

    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String senderEmail= "sungpyo8824@gmail.com";

    public MimeMessage CreateMail(String email, int number) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String email) {
        int number = (int) (Math.random() * (90000)) + 100000; // 메일마다 고유한 number 생성
        MimeMessage message = CreateMail(email, number);

        // 메일 전송
        javaMailSender.send(message);

        // redis에 {email : code} 형태로 저장
        stringRedisTemplate.opsForValue().set(email, String.valueOf(number), Duration.ofMinutes(5));

        return number;
    }

    public ResponseEntity<?> check(String email, int code) {
        String storedCode = stringRedisTemplate.opsForValue().get(email);
        if (storedCode == null || !storedCode.equals(String.valueOf(code))) {
            return ResponseEntity.badRequest().body("시간이 지났거나 인증코드가 틀립니다.");
        }
        return ResponseEntity.ok().build();
    }


}
