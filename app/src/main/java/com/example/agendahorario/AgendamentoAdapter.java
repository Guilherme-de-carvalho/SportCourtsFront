package com.example.agendahorario;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendahorario.network.ApiClient;
import com.example.agendahorario.network.ApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.AgendamentoViewHolder> {

    private final List<Agendamento> agendamentos;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public AgendamentoAdapter(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    @NonNull
    @Override
    public AgendamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendamento, parent, false);
        return new AgendamentoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendamentoViewHolder holder, int position) {
        Agendamento a = agendamentos.get(position);

        String local = (a.getCourt_name() != null && !a.getCourt_name().isEmpty())
                ? a.getCourt_name()
                : "Quadra " + a.getCourt_id();
        holder.textViewLocal.setText(local);
        holder.textViewData.setText(formatDateSafe(a.getStart_datetime()));
        holder.textViewHorario.setText(formatIntervalSafe(a.getStart_datetime(), a.getEnd_datetime()));

        holder.itemView.setOnClickListener(v -> {
            boolean isVisible = holder.cancelLayout.getVisibility() == View.VISIBLE;
            holder.cancelLayout.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        });

        holder.buttonCancelar.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            new AlertDialog.Builder(context)
                .setTitle("Confirmar Cancelamento")
                .setMessage("Tem certeza que deseja cancelar este agendamento?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition == RecyclerView.NO_POSITION) return;

                    Agendamento agendamentoParaCancelar = agendamentos.get(currentPosition);
                    int reservationId = agendamentoParaCancelar.getId();

                    ApiService apiService = ApiClient.getApiService(context);
                    Call<ApiService.CancelResponse> call = apiService.cancelReservation(reservationId);

                    call.enqueue(new Callback<ApiService.CancelResponse>() {
                        @Override
                        public void onResponse(Call<ApiService.CancelResponse> call, Response<ApiService.CancelResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Reserva cancelada com sucesso!", Toast.LENGTH_SHORT).show();
                                agendamentos.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                notifyItemRangeChanged(currentPosition, agendamentos.size());
                            } else {
                                Toast.makeText(context, "Falha ao cancelar a reserva.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiService.CancelResponse> call, Throwable t) {
                            Toast.makeText(context, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Não", null)
                .show();
        });
    }

    @Override
    public int getItemCount() {
        return agendamentos.size();
    }

    private String formatDateSafe(String start) {
        if (start == null) return "";
        try {
            Date d = inputFormat.parse(start);
            return d != null ? dateFormat.format(d) : start.split(" ")[0];
        } catch (ParseException e) {
            return start.split(" ")[0];
        }
    }

    private String formatIntervalSafe(String start, String end) {
        if (start == null || end == null) return "";
        try {
            Date s = inputFormat.parse(start);
            Date e = inputFormat.parse(end);
            String sStr = (s != null) ? timeFormat.format(s) : start;
            String eStr = (e != null) ? timeFormat.format(e) : end;
            return sStr + " - " + eStr;
        } catch (ParseException ex) {
            return start + " - " + end;
        }
    }

    static class AgendamentoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewData;
        TextView textViewLocal;
        TextView textViewHorario;
        LinearLayout cancelLayout;
        Button buttonCancelar;

        AgendamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewData = itemView.findViewById(R.id.textViewData);
            textViewLocal = itemView.findViewById(R.id.textViewLocal);
            textViewHorario = itemView.findViewById(R.id.textViewHorario);
            cancelLayout = itemView.findViewById(R.id.cancelLayout);
            buttonCancelar = itemView.findViewById(R.id.buttonCancelar);
        }
    }
}