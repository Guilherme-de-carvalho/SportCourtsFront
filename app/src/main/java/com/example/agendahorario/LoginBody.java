package com.example.agendahorario;

public class LoginBody {
    public String email;
    public String password;
    public String name;

    public LoginBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginBody(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}