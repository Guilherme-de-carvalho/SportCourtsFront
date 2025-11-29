package com.example.agendahorario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.AgendamentoViewHolder> {

    private List<Agendamento> agendamentos;

    public AgendamentoAdapter(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    @NonNull
    @Override
    public AgendamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendamento, parent, false);
        return new AgendamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendamentoViewHolder holder, int position) {
        Agendamento agendamento = agendamentos.get(position);
        holder.textViewData.setText(agendamento.getData());
        holder.textViewHorario.setText(agendamento.getHorario());
    }

    @Override
    public int getItemCount() {
        return agendamentos.size();
    }

    static class AgendamentoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewData;
        TextView textViewLocal;
        TextView textViewHorario;

        public AgendamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewData = itemView.findViewById(R.id.textViewData);
            textViewLocal = itemView.findViewById(R.id.textViewLocal);
            textViewHorario = itemView.findViewById(R.id.textViewHorario);
        }
    }
}