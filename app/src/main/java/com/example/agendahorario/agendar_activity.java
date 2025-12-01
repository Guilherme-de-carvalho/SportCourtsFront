package com.example.agendahorario;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agendahorario.network.ApiClient;
import com.example.agendahorario.network.ApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class agendar_activity extends AppCompatActivity {

    private Button buttonSelecionarData, buttonSelecionarHorario, buttonConfirmarAgendamento;
    private TextView textViewDataSelecionada, textViewHorarioSelecionado;

    private Calendar dataSelecionada = null;
    private String horarioSelecionado = null;
    private String local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agendar_activity);

        local = getIntent().getStringExtra(MainActivity.EXTRA_LOCAL);

        buttonSelecionarData = findViewById(R.id.buttonSelecionarData);
        buttonSelecionarHorario = findViewById(R.id.buttonSelecionarHorario);
        buttonConfirmarAgendamento = findViewById(R.id.buttonConfirmarAgendamento);
        textViewDataSelecionada = findViewById(R.id.textViewDataSelecionada);
        textViewHorarioSelecionado = findViewById(R.id.textViewHorarioSelecionado);

        buttonSelecionarData.setOnClickListener(v -> showDatePickerDialog());
        buttonSelecionarHorario.setOnClickListener(v -> showTimePickerDialog());
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
            textViewDataSelecionada.setText(sdf.format(dataSelecionada.getTime()));
            textViewDataSelecionada.setVisibility(View.VISIBLE);
            verificarCondicoesParaConfirmar();
        }, ano, mes, dia);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            tempCalendar.set(Calendar.MINUTE, minute);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            horarioSelecionado = sdf.format(tempCalendar.getTime());
            textViewHorarioSelecionado.setText(horarioSelecionado);
            textViewHorarioSelecionado.setVisibility(View.VISIBLE);
            verificarCondicoesParaConfirmar();
        }, hora, minuto, true);

        timePickerDialog.show();
    }

    private void verificarCondicoesParaConfirmar() {
        if (dataSelecionada != null && horarioSelecionado != null) {
            buttonConfirmarAgendamento.setVisibility(View.VISIBLE);
        }
    }

}