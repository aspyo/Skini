package capstone.skini.domain.mail.controller;

import capstone.skini.domain.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

}
