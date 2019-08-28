package com.devmasterteam.tasks.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.manager.PersonManager;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private PersonManager mPersonManager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Incializa as variáveis
        this.mContext = this;
        this.mPersonManager = new PersonManager(this.mContext);

        // Inicializa elementos
        this.mViewHolder.mImageBack = this.findViewById(R.id.image_toolbar_back);
        this.mViewHolder.mEditName = this.findViewById(R.id.edit_name);
        this.mViewHolder.mEditEmail = this.findViewById(R.id.edit_email);
        this.mViewHolder.mEditPassword = this.findViewById(R.id.edit_password);
        this.mViewHolder.mButtonSave = this.findViewById(R.id.button_save);

        // Inicializa eventos
        this.setListeners();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_save) {
            this.handleSave();
        } else if (id == R.id.image_toolbar_back) {
            super.onBackPressed();
        }
    }

    /**
     * Eventos
     */
    private void setListeners() {
        this.mViewHolder.mButtonSave.setOnClickListener(this);
        this.mViewHolder.mImageBack.setOnClickListener(this);
    }

    /**
     * Trata o registro do usuário
     */
    private void handleSave() {
        String email = this.mViewHolder.mEditEmail.getText().toString();
        String password = this.mViewHolder.mEditPassword.getText().toString();
        String name = this.mViewHolder.mEditName.getText().toString();

        // Cria usuário
        this.mPersonManager.create(email, password, name, registerListerner());
    }

    /**
     * Listener de registro
     */
    private OperationListener<Boolean> registerListerner() {
        return new OperationListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

            @Override
            public void onError(int error, String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        private ImageView mImageBack;
        private EditText mEditName;
        private EditText mEditEmail;
        private EditText mEditPassword;
        private Button mButtonSave;
    }
}
