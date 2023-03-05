package edu.upc.talent.swqa.campus.infrastructure;

import edu.upc.talent.swqa.campus.domain.EmailSender;

public class ConsoleEmailSender implements EmailSender {
  @Override
  public void sendEmail(String email, String subject, String body) {
    System.out.println("To: " + email + "\nSubject: " + subject + "\nBody:\n" + body + "\n");
  }
}
