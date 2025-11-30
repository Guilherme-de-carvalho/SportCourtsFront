package com.example.agendahorario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendahorario.Agendamento;
import com.example.agendahorario.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        // Local
        String local = (a.getCourt_name() != null && !a.getCourt_name().isEmpty())
                ? a.getCourt_name()
                : "Quadra " + a.getCourt_id();
        holder.textViewLocal.setText(local);

        // Data
        holder.textViewData.setText(formatDateSafe(a.getStart_datetime()));

        // Horário
        holder.textViewHorario.setText(formatIntervalSafe(a.getStart_datetime(), a.getEnd_datetime()));
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

        AgendamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewData = itemView.findViewById(R.id.textViewData);
            textViewLocal = itemView.findViewById(R.id.textViewLocal);
            textViewHorario = itemView.findViewById(R.id.textViewHorario);
        }
    }
}