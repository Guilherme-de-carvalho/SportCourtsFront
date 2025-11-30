package com.example.agendahorario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_COURT_ID = "court_id";

    public static final String EXTRA_LOCAL = "EXTRA_LOCAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Listeners de clique para abrir a tela de agendamento com o nome do local
        findViewById(R.id.campo_areia1_container).setOnClickListener(v -> openAgendarActivityWithLocal("Campo de Areia 1"));
        findViewById(R.id.campo_areia2_container).setOnClickListener(v -> openAgendarActivityWithLocal("Campo de Areia 2"));
        findViewById(R.id.campo1_container).setOnClickListener(v -> openAgendarActivityWithLocal("Campo 1"));
        findViewById(R.id.campo2_container).setOnClickListener(v -> openAgendarActivityWithLocal("Campo 2"));
        findViewById(R.id.campo3_container).setOnClickListener(v -> openAgendarActivityWithLocal("Campo 3"));
        findViewById(R.id.campo4_container).setOnClickListener(v -> openAgendarActivityWithLocal("Campo 4"));
        findViewById(R.id.campofut_container).setOnClickListener(v -> openAgendarActivityWithLocal("Campo Futebol"));
        findViewById(R.id.campofut2_container).setOnClickListener(v -> openAgendarActivityWithLocal("Campo Futebol 2"));

        // Botão para ver os agendamentos
        Button buttonAgendados = findViewById(R.id.buttonAgendados);
        buttonAgendados.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, agendados_activity.class);
            startActivity(intent);
        });
    }

    private void openAgendarActivityWithLocal(String local) {
        Intent intent = new Intent(MainActivity.this, agendar_activity.class);
        intent.putExtra(EXTRA_LOCAL, local);
        startActivity(intent);
    }
}