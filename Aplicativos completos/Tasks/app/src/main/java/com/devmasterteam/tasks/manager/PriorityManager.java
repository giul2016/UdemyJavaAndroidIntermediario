package com.devmasterteam.tasks.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.devmasterteam.tasks.business.PriorityBusiness;
import com.devmasterteam.tasks.entity.PriorityEntity;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.infra.operation.OperationResult;

import java.util.List;

public class PriorityManager {

    private PriorityBusiness mPriorityBusiness;

    /**
     * Construtor
     */
    public PriorityManager(Context context) {
        this.mPriorityBusiness = new PriorityBusiness(context);
    }

    /**
     * Faz a autenticação do usuário
     */
    public void getList(final OperationListener<List<PriorityEntity>> listener) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, OperationResult<List<PriorityEntity>>> task = new AsyncTask<Void, Void, OperationResult<List<PriorityEntity>>>() {
            @Override
            protected OperationResult<List<PriorityEntity>> doInBackground(Void... voids) {
                return mPriorityBusiness.getList();
            }

            @Override
            protected void onPostExecute(OperationResult<List<PriorityEntity>> result) {
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
