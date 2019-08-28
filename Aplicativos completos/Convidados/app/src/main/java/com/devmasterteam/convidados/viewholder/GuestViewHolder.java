package com.devmasterteam.convidados.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmasterteam.convidados.R;
import com.devmasterteam.convidados.entity.GuestEntity;
import com.devmasterteam.convidados.listener.OnGuestListener;

public class GuestViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextName;
    private ImageView mImageDelete;
    private Context mContext;
    private OnGuestListener mOnGuestListener;

    /**
     * Construtor
     */
    public GuestViewHolder(View itemView, OnGuestListener listener) {

        // Construtor pai
        super(itemView);

        this.mOnGuestListener = listener;
        this.mContext = itemView.getContext();

        // Mapeia os elementos de interface
        this.mTextName = itemView.findViewById(R.id.text_name);
        this.mImageDelete = itemView.findViewById(R.id.image_delete);
    }

    /**
     * Atribui valores aos elementos de interface
     */
    public void bindData(final GuestEntity entity) {
        this.mTextName.setText(entity.getName());

        // Evento de click para edição
        this.mTextName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnGuestListener.onListClick(entity.getId());
            }
        });

        this.mImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.remocao_convidado)
                        .setMessage(R.string.deseja_remover)
                        .setPositiveButton(R.string.remover, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mOnGuestListener.onDeleteClick(entity.getId());
                            }
                        })
                        .setNeutralButton(R.string.cancelar, null).show();
            }
        });

        this.mTextName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.remocao_convidado)
                        .setMessage(R.string.deseja_remover)
                        .setPositiveButton(R.string.remover, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mOnGuestListener.onDeleteClick(entity.getId());
                            }
                        })
                        .setNeutralButton(R.string.cancelar, null).show();
                return true;
            }
        });

    }

}
