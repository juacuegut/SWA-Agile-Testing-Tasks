package edu.upc.talent.swqa.campus.domain;

public interface EmailSender {
  void sendEmail(String email, String subject, String body);
}
