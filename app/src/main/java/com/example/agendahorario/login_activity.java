package com.example.agendahorario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agendahorario.network.ApiClient;
import com.example.agendahorario.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_activity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextEmail);
        loginBtn = findViewById(R.id.buttonLogin);

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            performLogin(email, password);
        });

        registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, cadastrar_activity.class));
        });
    }

    private void performLogin(String email, String password) {
        ApiService apiService = ApiClient.getApiService();
        LoginBody loginBody = new LoginBody(email, password);

        Call<User> call = apiService.login(loginBody);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Toast.makeText(login_activity.this,
                            "Bem-vindo " + user.getName(),
                            Toast.LENGTH_SHORT).show();

                    // Salve o ID do usuário em SharedPreferences ou intent
                    Intent intent = new Intent(login_activity.this, MainActivity.class);
                    intent.putExtra("user_id", user.getId());
                    intent.putExtra("user_name", user.getName());
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
                        "Erro: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}