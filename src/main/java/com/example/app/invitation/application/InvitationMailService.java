package com.example.app.invitation.application;

import com.example.app.core.mail.SmtpMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationMailService {

  private final SmtpMailSender mailSender;

  public void sendInvitationCode(String email, String code) {
    String subject = "[FarmFlow] 농장 초대 코드";
    String text =
        """
                FarmFlow 초대 코드 안내

                초대 코드: %s

                앱에서 해당 코드를 입력해 주세요.
                """
            .formatted(code);

    mailSender.send(email, subject, text);
  }
}
