package com.devmasterteam.convidados.business;

import android.content.Context;

import com.devmasterteam.convidados.R;
import com.devmasterteam.convidados.entity.GuestCountEntity;
import com.devmasterteam.convidados.entity.GuestEntity;
import com.devmasterteam.convidados.repository.GuestRepository;
import com.devmasterteam.convidados.util.ValidationException;

import java.util.List;

public class GuestBusiness {

    // Acesso a dados
    private GuestRepository mGuestRepository;
    private Context mContext;

    // Construtor
    public GuestBusiness(Context context) {
        this.mContext = context;
        this.mGuestRepository = GuestRepository.getInstance(context);
    }

    public List<GuestEntity> getInvited() {
        return this.mGuestRepository.getInvited();
    }

    public List<GuestEntity> getAbsent() {
        return this.mGuestRepository.getAbsent();
    }

    public List<GuestEntity> getConfirmed() {
        return this.mGuestRepository.getConfirmed();
    }

    /**
     * Carrega convidado específico
     */
    public GuestEntity load(int guestId) {
        return this.mGuestRepository.load(guestId);
    }

    /**
     * Carrega quantidade de convidados
     */
    public GuestCountEntity loadGuestsCount() {
        return this.mGuestRepository.loadGuestsCount();
    }

    /**
     * Remove convidado específico
     */
    public boolean remove(int guestId) {
        return this.mGuestRepository.remove(guestId);
    }

    /**
     * Insere convidado
     */
    public Boolean insert(GuestEntity guestEntity) throws ValidationException {

        validate(guestEntity);
        return this.mGuestRepository.insert(guestEntity);

    }

    /**
     * Atualiza convidado
     */
    public Boolean update(GuestEntity guestEntity) throws ValidationException {
        validate(guestEntity);
        return this.mGuestRepository.update(guestEntity);
    }

    /**
     * Faz a validação de dados
     */
    private void validate(GuestEntity entity) throws ValidationException {
        if ("".equals(entity.getName())) {
            throw new ValidationException(this.mContext.getString(R.string.nome_validacao));
        }
    }

}
