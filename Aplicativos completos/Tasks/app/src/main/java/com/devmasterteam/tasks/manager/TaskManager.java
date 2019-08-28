package com.devmasterteam.tasks.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.devmasterteam.tasks.business.TaskBusiness;
import com.devmasterteam.tasks.entity.TaskEntity;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.infra.operation.OperationResult;

import java.util.List;

public class TaskManager {

    private TaskBusiness mTaskBusiness;

    /**
     * Construtor
     */
    public TaskManager(Context context) {
        this.mTaskBusiness = new TaskBusiness(context);
    }

    /**
     * Faz a criação do usuário
     */
    public void create(final TaskEntity entity, final OperationListener<Boolean> listener) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, OperationResult<Boolean>> task = new AsyncTask<Void, Void, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return mTaskBusiness.create(entity);
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
     * Faz a listagem de tarefas
     */
    public void getList(final int filter, final OperationListener<List<TaskEntity>> listener) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Integer, OperationResult<List<TaskEntity>>> task = new AsyncTask<Void, Integer, OperationResult<List<TaskEntity>>>() {
            @Override
            protected OperationResult<List<TaskEntity>> doInBackground(Void... voids) {
                return mTaskBusiness.getList(filter);
            }

            @Override
            protected void onPostExecute(OperationResult<List<TaskEntity>> result) {
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
     * Busca uma tarefa específica
     */
    public void get(final int taskId, final OperationListener<TaskEntity> listener) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Integer, OperationResult<TaskEntity>> task = new AsyncTask<Void, Integer, OperationResult<TaskEntity>>() {
            @Override
            protected OperationResult<TaskEntity> doInBackground(Void... voids) {
                return mTaskBusiness.get(taskId);
            }

            @Override
            protected void onPostExecute(OperationResult<TaskEntity> result) {
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
     * Faz a atualização da tarefa
     */
    public void update(final TaskEntity taskEntity, final OperationListener<Boolean> listener) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Integer, OperationResult<Boolean>> task = new AsyncTask<Void, Integer, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return mTaskBusiness.update(taskEntity);
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
     * Atualiza a tarefa como completa
     */
    public void complete(final int id, final Boolean complete, final OperationListener<Boolean> listener) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Integer, OperationResult<Boolean>> task = new AsyncTask<Void, Integer, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return mTaskBusiness.complete(id, complete);
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
     * Faz a remoção de tarefa
     */
    public void delete(final int taskId, final OperationListener<Boolean> listener) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Integer, OperationResult<Boolean>> task = new AsyncTask<Void, Integer, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return mTaskBusiness.delete(taskId);
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
