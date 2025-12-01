package com.example.agendahorario;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agendahorario.network.ApiClient;
import com.example.agendahorario.network.ApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class agendar_activity extends AppCompatActivity {

    private Button buttonSelecionarData, buttonSelecionarHorario, buttonConfirmarAgendamento;
    private TextView textViewDataSelecionada, textViewHorarioSelecionado;

    private Calendar dataSelecionada = null;
    private String horarioSelecionado = null;
    private String local;
    private int courtId = 1;
    private List<String> horariosOcupados = new ArrayList<>();

    private static final int SLOT_MINUTES = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agendar_activity);

        local = getIntent().getStringExtra(MainActivity.EXTRA_LOCAL);
        courtId = getIntent().getIntExtra(MainActivity.EXTRA_COURT_ID, 1);

        if (local == null) {
            local = "Local não especificado";
        }

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText("Agendar para: " + local);

        buttonSelecionarData = findViewById(R.id.buttonSelecionarData);
        buttonSelecionarHorario = findViewById(R.id.buttonSelecionarHorario);
        buttonConfirmarAgendamento = findViewById(R.id.buttonConfirmarAgendamento);
        textViewDataSelecionada = findViewById(R.id.textViewDataSelecionada);
        textViewHorarioSelecionado = findViewById(R.id.textViewHorarioSelecionado);

        buttonConfirmarAgendamento.setVisibility(View.GONE);
        buttonSelecionarHorario.setEnabled(false);

        buttonSelecionarData.setOnClickListener(v -> showDatePickerDialog());
        buttonSelecionarHorario.setOnClickListener(v -> showCustomTimePickerDialog());
        buttonConfirmarAgendamento.setOnClickListener(v -> confirmarAgendamento());
    }

    private void showDatePickerDialog() {
        final Calendar calendario = Calendar.getInstance();
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            dataSelecionada = Calendar.getInstance();
            dataSelecionada.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            textViewDataSelecionada.setText("Data: " + sdf.format(dataSelecionada.getTime()));
            textViewDataSelecionada.setVisibility(View.VISIBLE);

            horarioSelecionado = null;
            textViewHorarioSelecionado.setVisibility(View.GONE);
            buttonConfirmarAgendamento.setVisibility(View.GONE);
            fetchHorariosOcupados();

        }, ano, mes, dia);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void fetchHorariosOcupados() {
        if (dataSelecionada == null) return;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dataFormatada = sdf.format(dataSelecionada.getTime());

        ApiService apiService = ApiClient.getApiService(this);
        Call<List<Agendamento>> call = apiService.getReservationsForCourt(courtId, dataFormatada);

        call.enqueue(new Callback<List<Agendamento>>() {
            @Override
            public void onResponse(Call<List<Agendamento>> call, Response<List<Agendamento>> response) {
                horariosOcupados.clear();
                if (response.isSuccessful() && response.body() != null) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    for (Agendamento agendamento : response.body()) {
                        try {
                            Date startTime = inputFormat.parse(agendamento.getStart_datetime());
                            if (startTime != null) {
                                horariosOcupados.add(timeFormat.format(startTime));
                            }
                        } catch (ParseException e) {
                        }
                    }
                }
                buttonSelecionarHorario.setEnabled(true);
                Toast.makeText(agendar_activity.this, "Selecione um horário", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Agendamento>> call, Throwable t) {
                horariosOcupados.clear();
                buttonSelecionarHorario.setEnabled(true);
                Toast.makeText(agendar_activity.this, "Erro ao buscar horários. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCustomTimePickerDialog() {
        List<String> horariosDisponiveis = new ArrayList<>();
        for (int hora = 8; hora <= 22; hora++) {
            String horarioCheio = String.format(Locale.getDefault(), "%02d:00", hora);
            if (!horariosOcupados.contains(horarioCheio)) {
                horariosDisponiveis.add(horarioCheio);
            }
            if (hora < 22) {
                String horarioMeia = String.format(Locale.getDefault(), "%02d:30", hora);
                 if (!horariosOcupados.contains(horarioMeia)) {
                    horariosDisponiveis.add(horarioMeia);
                }
            }
        }

        if (horariosDisponiveis.isEmpty()) {
            Toast.makeText(this, "Nenhum horário disponível para esta data.", Toast.LENGTH_LONG).show();
            return;
        }

        CharSequence[] items = horariosDisponiveis.toArray(new CharSequence[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione um Horário Disponível");
        builder.setItems(items, (dialog, which) -> {
            horarioSelecionado = horariosDisponiveis.get(which);
            textViewHorarioSelecionado.setText("Horário: " + horarioSelecionado);
            textViewHorarioSelecionado.setVisibility(View.VISIBLE);
            verificarCondicoesParaConfirmar();
        });
        builder.show();
    }

    private void verificarCondicoesParaConfirmar() {
        if (dataSelecionada != null && horarioSelecionado != null) {
            buttonConfirmarAgendamento.setVisibility(View.VISIBLE);
        }
    }

    private void confirmarAgendamento() {
        if (dataSelecionada == null || horarioSelecionado == null) {
            Toast.makeText(this, "Selecione a data e o horário", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dataFormatada = dateSdf.format(dataSelecionada.getTime());

        String[] parts = horarioSelecionado.split(":");
        int hora = Integer.parseInt(parts[0]);
        int minuto = Integer.parseInt(parts[1]);

        Calendar startCal = (Calendar) dataSelecionada.clone();
        startCal.set(Calendar.HOUR_OF_DAY, hora);
        startCal.set(Calendar.MINUTE, minuto);
        startCal.set(Calendar.SECOND, 0);
        Date startDate = startCal.getTime();

        Calendar endCal = (Calendar) startCal.clone();
        endCal.add(Calendar.MINUTE, SLOT_MINUTES);
        Date endDate = endCal.getTime();

        SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String startDatetime = dateTimeSdf.format(startDate);
        String endDatetime = dateTimeSdf.format(endDate);

        CreateReservationRequest req = new CreateReservationRequest(courtId, startDatetime, endDatetime);

        ApiService apiService = ApiClient.getApiService(this);
        Call<CreateReservationResponse> call = apiService.createReservation(req);

        buttonConfirmarAgendamento.setEnabled(false);

        call.enqueue(new Callback<CreateReservationResponse>() {
            @Override
            public void onResponse(Call<CreateReservationResponse> call, Response<CreateReservationResponse> response) {
                buttonConfirmarAgendamento.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    int reservationId = response.body().getId();
                    Toast.makeText(agendar_activity.this, "Reserva criada! ID: " + reservationId, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(agendar_activity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    int code = response.code();
                    String msg = "Erro desconhecido: " + code;
                     try {
                        if (response.errorBody() != null) {
                             msg = response.errorBody().string();
                        }
                    } catch (Exception ignored) {}
                    
                    if (code == 401) {
                        msg = "Token inválido. Faça login novamente.";
                    } else if (code == 422) {
                        msg = "Dados inválidos. Verifique o intervalo.";
                    } else if (code == 409) {
                        msg = "Conflito: horário indisponível ou blackout.";
                    }

                    Toast.makeText(agendar_activity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CreateReservationResponse> call, Throwable t) {
                buttonConfirmarAgendamento.setEnabled(true);
                Toast.makeText(agendar_activity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
