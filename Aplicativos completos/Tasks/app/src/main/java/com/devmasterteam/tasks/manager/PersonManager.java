package com.devmasterteam.tasks.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.devmasterteam.tasks.business.PersonBusiness;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.infra.operation.OperationResult;

public class PersonManager {

    private PersonBusiness mPersonBusiness;

    /**
     * Construtor
     */
    public PersonManager (Context context) {
        this.mPersonBusiness = new PersonBusiness(context);
    }

    /**
     * Faz a criação do usuário
     */
    public void create(final String email, final String password, final String name, final OperationListener<Boolean> listener) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, OperationResult<Boolean>> task = new AsyncTask<Void, Void, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return mPersonBusiness.create(email, password, name);
            }

            @Override
            protected void onPostExecute(OperationResult<Boolean> result) {
                int error = result.getError();
                if (error != OperationResult.NO_ERROR) {
                    listener.onError(error, result.getErrorMessage());
                } else {
                    listener.onSuccess(result.getResult());
                }
            }
        };

        // Executa async
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * Faz a autenticação do usuário
     */
    public void login(final String email, final String password, final OperationListener<Boolean> listener) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, OperationResult<Boolean>> task = new AsyncTask<Void, Void, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return mPersonBusiness.login(email, password);
            }

            @Override
            protected void onPostExecute(OperationResult<Boolean> result) {
                int error = result.getError();
                if (error != OperationResult.NO_ERROR) {
                    listener.onError(error, result.getErrorMessage());
                } else {
                    listener.onSuccess(result.getResult());
                }
            }
        };

        // Executa async
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
