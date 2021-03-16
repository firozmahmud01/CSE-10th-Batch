package com.firoz.mahmud.cse10thbatch;

public class UserCreadit {
    String name,email,password,studentid;

    public UserCreadit(String name, String email, String password, String studentid) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.studentid = studentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }
}
