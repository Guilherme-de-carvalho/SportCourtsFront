package com.example.agendahorario.network;

import com.example.agendahorario.Agendamento;
import com.example.agendahorario.CreateReservationRequest;
import com.example.agendahorario.CreateReservationResponse;
import com.example.agendahorario.LoginBody;
import com.example.agendahorario.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/register")
    Call<User> register(@Body LoginBody body);

    @POST("auth/login")
    Call<User> login(@Body LoginBody body);

    @POST("reservations")
    Call<CreateReservationResponse> createReservation(@Body CreateReservationRequest request);

    @GET("reservations")
    Call<List<Agendamento>> getMyReservations(@Query("mine") boolean mine);

    @PUT("reservations/{id}/cancel")
    Call<CancelResponse> cancelReservation(@Path("id") int id);

    class CancelResponse {
        public boolean ok;
    }
}
