package edu.upc.talent.swqa.campus.domain;

public final class CampusApp {

  private UsersRepository usersRepository;
  private EmailSender emailSender;

  public CampusApp(UsersRepository usersRepository, EmailSender emailSender) {
    this.usersRepository = usersRepository;
    this.emailSender = emailSender;
  }

  public void createGroup(String name) {
    usersRepository.createGroup(name);
  }

  public void createUser(String name, String surname, String email, String role, String groupName) {
    usersRepository.createUser(name, surname, email, role, groupName);
  }

  public void sendMailToGroupRole(String groupName, String onlyRole, String subject, String body) {
    var users = usersRepository.getUsersByGroupAndRole(groupName, onlyRole);
    users.forEach(u -> emailSender.sendEmail(u.email(), subject, body));
  }

  public void sendMailToGroup(String groupName, String subject, String body) {
    var users = usersRepository.getUsersByGroup(groupName);
    users.forEach(u -> emailSender.sendEmail(u.email(), subject, body));
  }
}
