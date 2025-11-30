package com.example.agendahorario;
public class Agendamento {
    private int id;
    private int court_id;
    private int user_id;
    private String start_datetime;
    private String end_datetime;
    private String total; // veio como string "50.00"
    private String total_price; // também string "0.00"
    private String status;
    private String created_at;
    private String updated_at;
    private String court_name;

    // Construtor vazio necessário para Gson/Retrofit
    public Agendamento() {
    }

    public int getId() {
        return id;
    }

    public int getCourt_id() {
        return court_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getStart_datetime() {
        return start_datetime;
    }

    public String getEnd_datetime() {
        return end_datetime;
    }

    public String getTotal() {
        return total;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCourt_name() {
        return court_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourt_id(int court_id) {
        this.court_id = court_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setStart_datetime(String start_datetime) {
        this.start_datetime = start_datetime;
    }

    public void setEnd_datetime(String end_datetime) {
        this.end_datetime = end_datetime;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setCourt_name(String court_name) {
        this.court_name = court_name;
    }
}
