package edu.upc.talent.swqa.campus.domain;

public record User(String id, String name, String surname, String email, String role, String groupName) {
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getGroupName() {
        return groupName;
    }
}
