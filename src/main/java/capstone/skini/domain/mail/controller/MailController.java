package capstone.skini.domain.mail.controller;

import capstone.skini.domain.mail.service.MailService;
import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.service.UserService;
import capstone.skini.security.user.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final UserService userService;

    @PostMapping("/mailSend")
    public ResponseEntity<?> mailSend(@AuthenticationPrincipal CustomPrincipal principal,
                                      @RequestParam("email") String email) {
        User user = userService.findByLoginId(principal.getLoginId());
        if (!user.getEmail().equals(email)) {
            return ResponseEntity.badRequest().body("유저의 이메일과 입력된 이메일이 다릅니다");
        }

        mailService.sendMail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mailAuth")
    public ResponseEntity<?> mailAuth(@RequestParam("email") String email,
                                      @RequestParam("code") int code) {
        return mailService.check(email, code);
    }
}
