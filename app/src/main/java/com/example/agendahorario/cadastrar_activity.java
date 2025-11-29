package com.example.agendahorario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agendahorario.network.ApiClient;
import com.example.agendahorario.network.ApiService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cadastrar_activity extends AppCompatActivity {

    private EditText editTextNome, editTextEmail, editTextPassword;
    private Button buttonCadastrar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_activity);

        // Inicializar views
        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        // Inicializar API service uma única vez
        apiService = ApiClient.getApiService();

        buttonCadastrar.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String nome = editTextNome.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validação de campos
        if (!isValidInput(nome, email, password)) {
            return;
        }

        // Desabilitar botão enquanto processa
        buttonCadastrar.setEnabled(false);
        buttonCadastrar.setText("Cadastrando...");

        // Criar body da requisição
        LoginBody registerBody = new LoginBody(nome, email, password);

        // Fazer chamada à API
        Call<User> call = apiService.register(registerBody);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                buttonCadastrar.setEnabled(true);
                buttonCadastrar.setText("Cadastrar");

                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    // Verificar se houve erro na resposta


                    // Ir para a tela de login
                    Intent intent = new Intent(cadastrar_activity.this, login_activity.class);
                    startActivity(intent);
                    finish();

                } else {
                    // Erro HTTP (422, 500, etc)
                    int statusCode = response.code();
                    String errorMsg = getErrorMessage(statusCode);

                    Toast.makeText(cadastrar_activity.this,
                            "Erro no cadastro: " + errorMsg,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                buttonCadastrar.setEnabled(true);
                buttonCadastrar.setText("Cadastrar");

                // Erro de conexão
                Toast.makeText(cadastrar_activity.this,
                        "Erro de conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Validação de entrada
    private boolean isValidInput(String nome, String email, String password) {
        if (nome.isEmpty()) {
            editTextNome.setError("Nome é obrigatório");
            return false;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email é obrigatório");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email inválido");
            return false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Senha é obrigatória");
            return false;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Senha deve ter pelo menos 6 caracteres");
            return false;
        }

        return true;
    }

    // Mapear códigos HTTP para mensagens amigáveis
    private String getErrorMessage(int statusCode) {
        switch (statusCode) {
            case 400:
                return "Requisição inválida";
            case 422:
                return "Email já cadastrado ou dados inválidos";
            case 500:
                return "Erro no servidor. Tente novamente mais tarde";
            default:
                return "Erro desconhecido (código: " + statusCode + ")";
        }
    }
}