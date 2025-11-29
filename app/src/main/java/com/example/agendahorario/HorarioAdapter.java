package com.example.agendahorario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder> {

    private List<String> horarios;
    private OnHorarioClickListener listener;

    public interface OnHorarioClickListener {
        void onHorarioClick(String horario);
    }

    public HorarioAdapter(List<String> horarios, OnHorarioClickListener listener) {
        this.horarios = horarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder holder, int position) {
        String horario = horarios.get(position);
        holder.bind(horario, listener);
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    static class HorarioViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewHorario;

        public HorarioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHorario = itemView.findViewById(R.id.textViewHorario);
        }

        public void bind(final String horario, final OnHorarioClickListener listener) {
            textViewHorario.setText(horario);
            itemView.setOnClickListener(v -> listener.onHorarioClick(horario));
        }
    }
}