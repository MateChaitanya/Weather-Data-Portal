package com.example.userldap.user;

public class UserDTO {
    private String fullName;
    private String emailid;
    private String password; // Raw password from frontend, will be used by LDAP

    public UserDTO() {}

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "fullName='" + fullName + '\'' +
                ", emailid='" + emailid + '\'' +
                '}';
    }
}
