package com.devmasterteam.convidados.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.devmasterteam.convidados.R;
import com.devmasterteam.convidados.business.GuestBusiness;
import com.devmasterteam.convidados.constants.GuestConstants;
import com.devmasterteam.convidados.entity.GuestEntity;
import com.devmasterteam.convidados.repository.GuestRepository;
import com.devmasterteam.convidados.util.ValidationException;

public class GuestFormActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private GuestBusiness mGuestBusiness;
    private int mGuestId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_form);

        // Inicializa variáveis
        this.mGuestBusiness = new GuestBusiness(this);

        // Obtém elementos
        this.mViewHolder.mEditName = this.findViewById(R.id.edit_name);
        this.mViewHolder.mButtonSave = this.findViewById(R.id.button_save);
        this.mViewHolder.mRadioNotConfirmed = this.findViewById(R.id.radio_not_confirmed);
        this.mViewHolder.mRadioPresent = this.findViewById(R.id.radio_present);
        this.mViewHolder.mRadioAbsent = this.findViewById(R.id.radio_absent);

        // Eventos
        this.mViewHolder.mButtonSave.setOnClickListener(this);

        // Carrega dados de edição caso exista
        this.loadDataFromActivity();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_save) {
            this.handleSave();
        }
    }

    /**
     * Carrega dados de edição caso exista
     */
    private void loadDataFromActivity() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mGuestId = extras.getInt(GuestConstants.BundleConstants.GUEST_ID);

            // Carrega convidado para edição
            GuestEntity guestEntity = this.mGuestBusiness.load(this.mGuestId);

            this.mViewHolder.mEditName.setText(guestEntity.getName());

            if (guestEntity.getConfirmed() == GuestConstants.CONFIRMATION.NOT_CONFIRMED) {
                this.mViewHolder.mRadioNotConfirmed.setChecked(true);
            } else if (guestEntity.getConfirmed() == GuestConstants.CONFIRMATION.PRESENT) {
                this.mViewHolder.mRadioPresent.setChecked(true);
            } else {
                this.mViewHolder.mRadioAbsent.setChecked(true);
            }
        } else {
            this.mViewHolder.mRadioNotConfirmed.setChecked(true);
        }
    }

    /**
     * Trata clique para salvar
     */
    private void handleSave() {

        // Inicializa convidado
        GuestEntity guestEntity = new GuestEntity();

        // Atribui valores
        guestEntity.setName(this.mViewHolder.mEditName.getText().toString());

        if (this.mViewHolder.mRadioPresent.isChecked()) {
            guestEntity.setConfirmed(GuestConstants.CONFIRMATION.PRESENT);
        } else if (this.mViewHolder.mRadioAbsent.isChecked()) {
            guestEntity.setConfirmed(GuestConstants.CONFIRMATION.ABSENT);
        } else {
            guestEntity.setConfirmed(GuestConstants.CONFIRMATION.NOT_CONFIRMED);
        }

        // Insere novo convidado
        try {
            boolean isSaved;
            if (this.mGuestId == 0) {
                isSaved = this.mGuestBusiness.insert(guestEntity);
            } else {
                guestEntity.setId(this.mGuestId);
                isSaved = this.mGuestBusiness.update(guestEntity);
            }

            if (isSaved) {

                // Notifica que convidado foi salvo / atualizado e fecha o formulário
                if (this.mGuestId == 0) {
                    Toast.makeText(this, R.string.convidado_salvo_com_sucesso, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.convidado_atualizado_com_sucesso, Toast.LENGTH_LONG).show();
                }

                finish();
            } else {
                Toast.makeText(this, R.string.erro_inesperado, Toast.LENGTH_SHORT).show();
            }

        } catch (ValidationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText mEditName;
        RadioButton mRadioNotConfirmed;
        RadioButton mRadioPresent;
        RadioButton mRadioAbsent;
        Button mButtonSave;
    }

}
