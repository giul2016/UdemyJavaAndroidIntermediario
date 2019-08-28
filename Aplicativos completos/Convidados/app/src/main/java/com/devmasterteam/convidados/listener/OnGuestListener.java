package com.devmasterteam.convidados.listener;

public interface OnGuestListener {

    /**
     * Click para edição
     */
    void onListClick(int guestId);

    /**
     * Click para remoção
     */
    void onDeleteClick(int guestId);

}
