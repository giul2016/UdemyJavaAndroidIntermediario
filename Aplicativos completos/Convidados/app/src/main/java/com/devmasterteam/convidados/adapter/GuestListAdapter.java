package com.devmasterteam.convidados.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmasterteam.convidados.R;
import com.devmasterteam.convidados.entity.GuestEntity;
import com.devmasterteam.convidados.listener.OnGuestListener;
import com.devmasterteam.convidados.viewholder.GuestViewHolder;

import java.util.List;

public class GuestListAdapter extends RecyclerView.Adapter<GuestViewHolder> {

    // Lista de convidados
    private List<GuestEntity> mListGuests;
    private OnGuestListener mOnGuestListener;

    /**
     * Construtor
     */
    public GuestListAdapter(List<GuestEntity> guests, OnGuestListener listener) {
        this.mListGuests = guests;
        this.mOnGuestListener = listener;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Infla o layout da linha e faz uso na listagem
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View guestRow = inflater.inflate(R.layout.row_guest_list, parent, false);

        // Retorna linha
        return new GuestViewHolder(guestRow, this.mOnGuestListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        GuestEntity entity = this.mListGuests.get(position);
        holder.bindData(entity);
    }

    @Override
    public int getItemCount() {
        return this.mListGuests.size();
    }
}
