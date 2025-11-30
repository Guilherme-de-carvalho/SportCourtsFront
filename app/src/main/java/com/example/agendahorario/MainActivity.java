package com.example.agendahorario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_COURT_ID = "com.example.agendahorario.COURT_ID";
    public static final String EXTRA_LOCAL = "com.example.agendahorario.LOCAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botão para ver agendamentos
        Button buttonAgendados = findViewById(R.id.buttonAgendados);
        buttonAgendados.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, agendados_activity.class);
            startActivity(intent);
        });

        // Configura o clique para cada quadra
        setupCourtClickListener(R.id.campo_areia1_container, 1, "Campo de Areia 1");
        setupCourtClickListener(R.id.campo_areia2_container, 2, "Campo de Areia 2");
        setupCourtClickListener(R.id.campo1_container, 3, "Campo 1");
        setupCourtClickListener(R.id.campo2_container, 4, "Campo 2");
        setupCourtClickListener(R.id.campo3_container, 5, "Campo 3");
        setupCourtClickListener(R.id.campo4_container, 6, "Campo 4");
        setupCourtClickListener(R.id.campofut_container, 7, "Campo Futebol");
        setupCourtClickListener(R.id.campofut2_container, 8, "Campo Futebol 2");
    }

    private void setupCourtClickListener(int layoutId, int courtId, String courtName) {
        LinearLayout layout = findViewById(layoutId);
        if (layout != null) {
            layout.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, agendar_activity.class);
                intent.putExtra(EXTRA_COURT_ID, courtId);
                intent.putExtra(EXTRA_LOCAL, courtName);
                startActivity(intent);
            });
        }
    }
}