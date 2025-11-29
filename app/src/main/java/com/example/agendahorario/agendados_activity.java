package com.example.agendahorario;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendahorario.network.ApiClient;
import com.example.agendahorario.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class agendados_activity extends AppCompatActivity {

    private RecyclerView recyclerViewAgendamentos;
    private AgendamentoAdapter agendamentoAdapter;
    private List<Agendamento> agendamentos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agendados_activity);

        recyclerViewAgendamentos = findViewById(R.id.recyclerViewAgendamentos);
        recyclerViewAgendamentos.setLayoutManager(new LinearLayoutManager(this));

        agendamentoAdapter = new AgendamentoAdapter(agendamentos);
        recyclerViewAgendamentos.setAdapter(agendamentoAdapter);

        fetchAgendamentos();
    }

    private void fetchAgendamentos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<Agendamento>> call = apiService.getAgendamentos();
        call.enqueue(new Callback<List<Agendamento>>() {
            @Override
            public void onResponse(Call<List<Agendamento>> call, Response<List<Agendamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    agendamentos.clear();
                    agendamentos.addAll(response.body());
                    agendamentoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(agendados_activity.this, "Falha ao buscar agendamentos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Agendamento>> call, Throwable t) {
                Toast.makeText(agendados_activity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}