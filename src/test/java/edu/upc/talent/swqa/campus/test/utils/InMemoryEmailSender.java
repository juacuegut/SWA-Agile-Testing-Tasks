package edu.upc.talent.swqa.campus.test.utils;

import edu.upc.talent.swqa.campus.domain.EmailSender;

import java.util.List;

public class InMemoryEmailSender implements EmailSender {

  private List<SentEmail> sentEmails;

  public InMemoryEmailSender(List<SentEmail> sentEmails) {
    this.sentEmails = sentEmails;
  }

  @Override
  public void sendEmail(String email, String subject, String body) {
    sentEmails.add(new SentEmail(email, subject, body));
  }

}
