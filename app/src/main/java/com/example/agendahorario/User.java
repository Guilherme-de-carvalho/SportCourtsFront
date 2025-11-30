package com.example.agendahorario;

public class User {
    private int id;
    private String name;
    private String email;
    private String api_token;

    // Construtor vazio necessário para Gson/Retrofit
    public User() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getApi_token() {
        return api_token;
    }

    // Setters necessários para Gson/Retrofit parsear a resposta JSON
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }
}
