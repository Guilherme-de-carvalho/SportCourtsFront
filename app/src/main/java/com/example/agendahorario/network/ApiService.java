package com.example.agendahorario.network;

import com.example.agendahorario.LoginBody;
import com.example.agendahorario.User;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // ===== AUTH ENDPOINTS =====
    @POST("auth/register")
    Call<User> register(@Body LoginBody body);

    @POST("auth/login")
    Call<User> login(@Body LoginBody body);

    // ===== SPORTS ENDPOINTS =====
    @GET("sports")
    Call<SportsResponse> getSports();

    // ===== AVAILABILITY ENDPOINTS =====
    @GET("availability")
    Call<AvailabilityResponse> getAvailability(
            @Query("date") String date,
            @Query("club_id") Integer clubId,
            @Query("sport_id") Integer sportId
    );

    // ===== RESERVATIONS ENDPOINTS =====
    @POST("reservations")
    Call<ReservationResponse> createReservation(@Body ReservationRequest body);

    @GET("reservations")
    Call<ReservationsListResponse> getMyReservations(@Query("mine") boolean mine);

    @PUT("reservations/{id}/cancel")
    Call<CancelResponse> cancelReservation(@Path("id") int id);
}

// ===== RESPONSE MODELS =====

class SportsResponse {
    public String status;
    public java.util.List<Sport> data;
}

class Sport {
    public int id;
    public String name;
}

class AvailabilityResponse {
    public String status;
    public java.util.List<Availability> data;
}

class Availability {
    public int id;
    public int sport_id;
    public int club_id;
    public String date;
    public String start_time;
    public String end_time;
    public double price;
}

class ReservationRequest {
    public int availability_id;
    public int user_id;

    public ReservationRequest(int availability_id, int user_id) {
        this.availability_id = availability_id;
        this.user_id = user_id;
    }
}

class ReservationResponse {
    public String status;
    public Reservation data;
}

class Reservation {
    public int id;
    public int user_id;
    public int availability_id;
    public String created_at;
}

class ReservationsListResponse {
    public String status;
    public java.util.List<Reservation> data;
}

class CancelResponse {
    public String status;
    public String message;
}