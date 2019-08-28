package com.devmasterteam.tasks.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.business.PersonBusiness;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.manager.PersonManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private PersonBusiness mPersonBusiness;
    private PersonManager mPersonManager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Incializa as variáveis
        this.mContext = this;
        this.mPersonManager = new PersonManager(this.mContext);
        this.mPersonBusiness = new PersonBusiness(this.mContext);

        // Inicializa elementos
        this.mViewHolder.mEditEmail = this.findViewById(R.id.edit_email);
        this.mViewHolder.mEditPassword = this.findViewById(R.id.edit_password);
        this.mViewHolder.mButtonLogin = this.findViewById(R.id.button_login);
        this.mViewHolder.mTextRegister = this.findViewById(R.id.text_register);

        // Inicializa eventos
        this.setListeners();

        // Verifica se usuário está logado
        this.verifyLoggedUser();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_login) {
            this.handleLogin();
        } else if (id == R.id.text_register) {
            startActivity(new Intent(this.mContext, RegisterActivity.class));
        }
    }

    /**
     * Inicializa os eventos de click
     */
    private void setListeners() {
        this.mViewHolder.mButtonLogin.setOnClickListener(this);
        this.mViewHolder.mTextRegister.setOnClickListener(this);
    }

    /**
     * Autentica usuário
     */
    private void handleLogin() {
        String email = this.mViewHolder.mEditEmail.getText().toString();
        String password = this.mViewHolder.mEditPassword.getText().toString();

        // Cria usuário
        this.mPersonManager.login(email, password, loginListener());
    }

    /**
     * Verifica se usuário está logado
     */
    private void verifyLoggedUser() {
        if (this.mPersonBusiness.isUserLogged()) {
            // Inicializa tela de principal e impede que o usuário volte a essa tela
            startActivity(new Intent(this.mContext, MainActivity.class));
            finish();
        }
    }

    /**
     * Listener de login
     */
    private OperationListener<Boolean> loginListener() {
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
        private EditText mEditEmail;
        private EditText mEditPassword;
        private Button mButtonLogin;
        private TextView mTextRegister;
    }
}
