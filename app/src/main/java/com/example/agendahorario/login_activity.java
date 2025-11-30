package com.example.agendahorario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agendahorario.network.ApiClient;
import com.example.agendahorario.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_activity extends AppCompatActivity {

    private EditText campoEmail;
    private EditText campoSenha;
    private Button botaoLogin;
    private TextView linkCadastro; // Alterado de Button para TextView para corresponder ao seu layout anterior

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Button loginButton = findViewById(R.id.buttonLogin);


        // Correção: IDs corretos para cada campo
        campoEmail = findViewById(R.id.editTextEmail);
        campoSenha = findViewById(R.id.editTextPassword); // ID da senha corrigido
        botaoLogin = findViewById(R.id.buttonLogin);
        linkCadastro = findViewById(R.id.textViewCadastro); // ID do texto/link de cadastro

        botaoLogin.setOnClickListener(v -> {
            String email = campoEmail.getText().toString().trim();
            String senha = campoSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            executarLogin(email, senha);
        });

        linkCadastro.setOnClickListener(v -> {
            startActivity(new Intent(this, cadastrar_activity.class));
        });
    }

    private void executarLogin(String email, String senha) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        LoginBody corpoLogin = new LoginBody(email, senha);

        Call<User> chamada = apiService.login(corpoLogin);

        chamada.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User usuario = response.body();

                    String token = usuario.getApi_token();

                    if (token != null && !token.isEmpty()) {
                        // salvar token e id antes de abrir MainActivity
                        SharedPreferences prefs = getApplicationContext()
                                .getSharedPreferences("agendahorario", MODE_PRIVATE);
                        prefs.edit()
                                .putString("api_token", token)
                                .putInt("user_id", usuario.getId())
                                .putString("user_name", usuario.getName())
                                .apply();
                    }

                    Toast.makeText(login_activity.this,
                            "Bem-vindo " + (usuario.getName() != null ? usuario.getName() : ""),
                            Toast.LENGTH_SHORT).show();

                    // agora inicia a MainActivity
                    Intent intent = new Intent(login_activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(login_activity.this,
                            "Email ou senha inválidos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(login_activity.this,
                        "Erro de conexão: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}