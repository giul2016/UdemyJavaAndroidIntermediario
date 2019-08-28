package com.devmasterteam.tasks.business;

import android.content.Context;

import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entity.APIResponseEntity;
import com.devmasterteam.tasks.entity.ParameterEntity;
import com.devmasterteam.tasks.entity.TaskEntity;
import com.devmasterteam.tasks.infra.builder.URL;
import com.devmasterteam.tasks.infra.operation.OperationResult;
import com.devmasterteam.tasks.infra.util.FormatUrlParameters;
import com.devmasterteam.tasks.repository.ExternalRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

public class TaskBusiness extends BaseBusiness {

    private ExternalRepository mExternalRepository;
    private Context mContext;

    /**
     * Construtor
     */
    public TaskBusiness(Context context) {
        super(context);
        this.mContext = context;
        this.mExternalRepository = new ExternalRepository(context);
    }

    /**
     * Faz a listagem de tarefas
     */
    public OperationResult<List<TaskEntity>> getList(int filter) {

        // Retorno
        OperationResult<List<TaskEntity>> operationResult = new OperationResult<>();

        try {

            // Monta query
            String url;
            URL.Builder urlBuilder = new URL.Builder(TaskConstants.ENDPOINT.ROOT);

            if (filter == TaskConstants.TASKFILTER.NO_FILTER) {
                url = urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_GET_LIST_NO_FILTER).build().getURL();
            } else if (filter == TaskConstants.TASKFILTER.OVERDUE) {
                url = urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_GET_LIST_OVERDUE).build().getURL();
            } else {
                url = urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_GET_LIST_NEXT_7_DAYS).build().getURL();
            }

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParameters();

            // Cria objeto de requisição com parâmetros
            ParameterEntity params = new ParameterEntity(TaskConstants.METHOD.GET, url, null, (HashMap) headerParameters);

            // Executa
            APIResponseEntity response = this.mExternalRepository.execute(params);

            // Sucesso
            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {

                // Faz a conversão do json
                List<TaskEntity> list = new Gson().fromJson(response.getJson(), new TypeToken<List<TaskEntity>>() {
                }.getType());

                // Sucesso
                operationResult.setResult(list);

            } else {
                operationResult.setError(super.handleResponseCode(response.getStatusCode()), super.getErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

    /**
     * Obtém tarefa única
     */
    public OperationResult<TaskEntity> get(int taskId) {

        // Retorno
        OperationResult<TaskEntity> operationResult = new OperationResult<>();

        try {

            // Monta query
            String url = new URL.Builder(TaskConstants.ENDPOINT.ROOT)
                    .addResource(TaskConstants.ENDPOINT.TASK_GET_SPECIFIC)
                    .addResource(String.valueOf(taskId))
                    .build().getURL();

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParameters();

            // Cria objeto de requisição com parâmetros
            ParameterEntity params = new ParameterEntity(TaskConstants.METHOD.GET, url, null, (HashMap) headerParameters);

            // Executa
            APIResponseEntity response = this.mExternalRepository.execute(params);

            // Sucesso
            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {

                // Faz a conversão do json
                TaskEntity taskEntity = new Gson().fromJson(response.getJson(), TaskEntity.class);

                // Sucesso
                operationResult.setResult(taskEntity);

            } else {
                operationResult.setError(super.handleResponseCode(response.getStatusCode()), super.getErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

    /**
     * Faz a inserção da tarefa
     */
    public OperationResult<Boolean> create(TaskEntity taskEntity) {

        // Retorno
        OperationResult<Boolean> operationResult = new OperationResult<>();

        try {

            // Monta query
            String url = new URL.Builder(TaskConstants.ENDPOINT.ROOT)
                    .addResource(TaskConstants.ENDPOINT.TASK_INSERT)
                    .build().getURL();

            AbstractMap<String, String> parameters = new HashMap<>();
            parameters.put(TaskConstants.API_PARAMETER.DESCRIPTION, taskEntity.getDescription());
            parameters.put(TaskConstants.API_PARAMETER.PRIORITY_ID, String.valueOf(taskEntity.getPriorityId()));
            parameters.put(TaskConstants.API_PARAMETER.DUE_DATE, FormatUrlParameters.formatDate(taskEntity.getDueDate()));
            parameters.put(TaskConstants.API_PARAMETER.COMPLETE, FormatUrlParameters.formatBoolean(taskEntity.getComplete()));

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParameters();

            // Cria objeto de requisição com parâmetros
            ParameterEntity params = new ParameterEntity(TaskConstants.METHOD.POST, url, (HashMap) parameters, (HashMap) headerParameters);

            // Executa
            APIResponseEntity response = this.mExternalRepository.execute(params);

            // Sucesso
            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                operationResult.setResult(new Gson().fromJson(response.getJson(), Boolean.class));
            } else {
                operationResult.setError(super.handleResponseCode(response.getStatusCode()), super.getErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

    /**
     * Faz a atualização da tarefa
     */
    public OperationResult<Boolean> update(TaskEntity taskEntity) {

        // Retorno
        OperationResult<Boolean> operationResult = new OperationResult<>();

        try {

            // Monta query
            String url = new URL.Builder(TaskConstants.ENDPOINT.ROOT)
                    .addResource(TaskConstants.ENDPOINT.TASK_UPDATE)
                    .build().getURL();

            AbstractMap<String, String> parameters = new HashMap<>();
            parameters.put(TaskConstants.API_PARAMETER.ID, String.valueOf(taskEntity.getId()));
            parameters.put(TaskConstants.API_PARAMETER.DESCRIPTION, taskEntity.getDescription());
            parameters.put(TaskConstants.API_PARAMETER.PRIORITY_ID, String.valueOf(taskEntity.getPriorityId()));
            parameters.put(TaskConstants.API_PARAMETER.DUE_DATE, FormatUrlParameters.formatDate(taskEntity.getDueDate()));
            parameters.put(TaskConstants.API_PARAMETER.COMPLETE, FormatUrlParameters.formatBoolean(taskEntity.getComplete()));

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParameters();

            // Cria objeto de requisição com parâmetros
            ParameterEntity params = new ParameterEntity(TaskConstants.METHOD.PUT, url, (HashMap) parameters, (HashMap) headerParameters);

            // Executa
            APIResponseEntity response = this.mExternalRepository.execute(params);

            // Sucesso
            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                operationResult.setResult(new Gson().fromJson(response.getJson(), Boolean.class));
            } else {
                operationResult.setError(super.handleResponseCode(response.getStatusCode()), super.getErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

    /**
     * Marca a tarefa como completa ou não
     */
    public OperationResult<Boolean> complete(int id, Boolean complete) {

        // Retorno
        OperationResult<Boolean> operationResult = new OperationResult<>();

        try {

            // Monta query
            String url;
            URL.Builder urlBuilder = new URL.Builder(TaskConstants.ENDPOINT.ROOT);

            if (complete) {
                url = urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_COMPLETE).build().getURL();
            } else {
                url = urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_UNCOMPLETE).build().getURL();
            }

            AbstractMap<String, String> parameters = new HashMap<>();
            parameters.put(TaskConstants.API_PARAMETER.ID, String.valueOf(id));

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParameters();

            // Cria objeto de requisição com parâmetros
            ParameterEntity params = new ParameterEntity(TaskConstants.METHOD.PUT, url, (HashMap) parameters, (HashMap) headerParameters);

            // Executa
            APIResponseEntity response = this.mExternalRepository.execute(params);

            // Sucesso
            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                operationResult.setResult(new Gson().fromJson(response.getJson(), Boolean.class));
            } else {
                operationResult.setError(super.handleResponseCode(response.getStatusCode()), super.getErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

    /**
     * Faz a remoção de tarefa
     */
    public OperationResult<Boolean> delete(int taskId) {

        // Retorno
        OperationResult<Boolean> operationResult = new OperationResult<>();

        try {

            // Monta query
            String url = new URL.Builder(TaskConstants.ENDPOINT.ROOT)
                    .addResource(TaskConstants.ENDPOINT.TASK_DELETE)
                    .build().getURL();

            AbstractMap<String, String> parameters = new HashMap<>();
            parameters.put(TaskConstants.API_PARAMETER.ID, String.valueOf(taskId));

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParameters();

            // Cria objeto de requisição com parâmetros
            ParameterEntity params = new ParameterEntity(TaskConstants.METHOD.DELETE, url, (HashMap) parameters, (HashMap) headerParameters);

            // Executa
            APIResponseEntity response = this.mExternalRepository.execute(params);

            // Sucesso
            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                operationResult.setResult(new Gson().fromJson(response.getJson(), Boolean.class));
            } else {
                operationResult.setError(super.handleResponseCode(response.getStatusCode()), super.getErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

}
