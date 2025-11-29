package com.example.agendahorario;

public class RegisterBody {
    private String name;
    private String email;
    private String password;

    public RegisterBody(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
