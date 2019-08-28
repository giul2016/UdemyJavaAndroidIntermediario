package com.devmasterteam.tasks.business;

import android.content.Context;

import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entity.APIResponseEntity;
import com.devmasterteam.tasks.entity.ParameterEntity;
import com.devmasterteam.tasks.entity.PriorityEntity;
import com.devmasterteam.tasks.infra.builder.URL;
import com.devmasterteam.tasks.infra.operation.OperationResult;
import com.devmasterteam.tasks.repository.ExternalRepository;
import com.devmasterteam.tasks.repository.PriorityCache;
import com.devmasterteam.tasks.repository.PriorityRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

public class PriorityBusiness extends BaseBusiness {

    private ExternalRepository mExternalRepository;
    private PriorityRepository mPriorityRepository;

    /**
     * Construtor
     */
    public PriorityBusiness(Context context) {
        super(context);
        this.mExternalRepository = new ExternalRepository(context);
        this.mPriorityRepository = PriorityRepository.getInstance(context);
    }

    /**
     * Faz a listagem de prioridade
     */
    public OperationResult<List<PriorityEntity>> getList() {

        // Retorno
        OperationResult<List<PriorityEntity>> operationResult = new OperationResult<>();

        try {

            // Monta query
            String url = new URL.Builder(TaskConstants.ENDPOINT.ROOT)
                    .addResource(TaskConstants.ENDPOINT.PRIORITY_GET)
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
                List<PriorityEntity> list = new Gson().fromJson(response.getJson(), new TypeToken<List<PriorityEntity>>() {
                }.getType());
                operationResult.setResult(list);

                // 1 - Atualiza o cache
                PriorityCache.setCache(list);

                // 2 - Limpa o banco de dados
                this.clear();

                // 3 - Atualiza as informações
                this.insert(list);

            } else {
                operationResult.setError(super.handleResponseCode(response.getStatusCode()), super.getErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

    /**
     * Limpa todos os registros do banco
     */
    public void clear() {
        this.mPriorityRepository.clear();
    }

    /**
     * Faz inserção em massa de prioridades
     */
    public void insert(List<PriorityEntity> list) {
        this.mPriorityRepository.insert(list);
    }

    /**
     * Carrega lista de prioridades do banco de dados local
     */
    public List<PriorityEntity> getListLocal() {
        return this.mPriorityRepository.getList();
    }

}
