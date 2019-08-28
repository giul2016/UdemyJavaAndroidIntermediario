package com.devmasterteam.tasks.business;

import android.content.Context;
import android.content.Intent;

import com.devmasterteam.tasks.activity.MainActivity;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entity.APIResponseEntity;
import com.devmasterteam.tasks.entity.HeaderEntity;
import com.devmasterteam.tasks.entity.ParameterEntity;
import com.devmasterteam.tasks.entity.PersonEntity;
import com.devmasterteam.tasks.infra.builder.URL;
import com.devmasterteam.tasks.infra.operation.OperationResult;
import com.devmasterteam.tasks.infra.security.SecurityPreferences;
import com.devmasterteam.tasks.infra.util.FormatUrlParameters;
import com.devmasterteam.tasks.repository.ExternalRepository;
import com.google.gson.Gson;

import java.util.AbstractMap;
import java.util.HashMap;

public class PersonBusiness extends BaseBusiness {

    private ExternalRepository mExternalRepository;
    private Context mContext;

    /**
     * Construtor
     */
    public PersonBusiness(Context context) {
        super(context);
        this.mContext = context;
        this.mExternalRepository = new ExternalRepository(context);
    }

    /**
     * Faz a criação do usuário
     */
    public OperationResult<Boolean> create(String email, String password, String name) {

        OperationResult<Boolean> result = new OperationResult<>();

        try {

            // Monta URL do serviço que será chamado
            String url = new URL.Builder(TaskConstants.ENDPOINT.ROOT)
                    .addResource(TaskConstants.ENDPOINT.AUTHENTICATION_CREATE)
                    .build().getURL();

            // Adiciona parâmetros para requisição
            AbstractMap<String, String> parameters = new HashMap<>();
            parameters.put(TaskConstants.API_PARAMETER.EMAIL, email);
            parameters.put(TaskConstants.API_PARAMETER.PASSWORD, password);
            parameters.put(TaskConstants.API_PARAMETER.NAME, name);
            parameters.put(TaskConstants.API_PARAMETER.RECEIVE_NEWS, FormatUrlParameters.formatBoolean(true));

            // Cria objeto de requisição com parâmetros
            ParameterEntity params = new ParameterEntity(TaskConstants.METHOD.POST, url, (HashMap) parameters, null);

            // Executa
            APIResponseEntity response = this.mExternalRepository.execute(params);

            // Sucesso
            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {

                // Faz a conversão do json
                HeaderEntity headerEntity = new Gson().fromJson(response.getJson(), HeaderEntity.class);

                // Armazena dados da 'session' no SharedPreferences
                SecurityPreferences preferences = new SecurityPreferences(this.mContext);
                preferences.storeString(TaskConstants.HEADER.PERSON_KEY, headerEntity.personKey);
                preferences.storeString(TaskConstants.HEADER.TOKEN_KEY, headerEntity.token);
                preferences.storeString(TaskConstants.USER.NAME, headerEntity.name);
                preferences.storeString(TaskConstants.USER.EMAIL, email);

                // Sucesso
                result.setResult(true);
            } else {
                result.setError(super.handleResponseCode(response.getStatusCode()), super.getErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            result.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return result;
    }

    /**
     * Faz a autenticação do usuário
     */
    public OperationResult<Boolean> login(String email, String password) {

        OperationResult<Boolean> result = new OperationResult<>();

        try {

            // Monta URL do serviço que será chamado
            String url = new URL.Builder(TaskConstants.ENDPOINT.ROOT)
                    .addResource(TaskConstants.ENDPOINT.AUTHENTICATION_LOGIN)
                    .build().getURL();

            // Adiciona parâmetros para requisição
            AbstractMap<String, String> parameters = new HashMap<>();
            parameters.put(TaskConstants.API_PARAMETER.EMAIL, email);
            parameters.put(TaskConstants.API_PARAMETER.PASSWORD, password);

            // Cria objeto de requisição com parâmetros
            ParameterEntity params = new ParameterEntity(TaskConstants.METHOD.POST, url, (HashMap) parameters, null);

            // Executa
            APIResponseEntity response = this.mExternalRepository.execute(params);

            // Sucesso
            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {

                // Faz a conversão do json
                HeaderEntity headerEntity = new Gson().fromJson(response.getJson(), HeaderEntity.class);

                // Armazena dados da 'session' no SharedPreferences
                SecurityPreferences preferences = new SecurityPreferences(this.mContext);
                preferences.storeString(TaskConstants.HEADER.PERSON_KEY, headerEntity.personKey);
                preferences.storeString(TaskConstants.HEADER.TOKEN_KEY, headerEntity.token);
                preferences.storeString(TaskConstants.USER.NAME, headerEntity.name);
                preferences.storeString(TaskConstants.USER.EMAIL, email);

                // Sucesso
                result.setResult(true);
            } else {
                result.setError(super.handleResponseCode(response.getStatusCode()), super.getErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            result.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return result;
    }

    /**
     * Limpa dados do usuário
     */
    public void clearData() {
        SecurityPreferences preferences = new SecurityPreferences(this.mContext);
        preferences.removeStoredString(TaskConstants.HEADER.PERSON_KEY);
        preferences.removeStoredString(TaskConstants.HEADER.TOKEN_KEY);
        preferences.removeStoredString(TaskConstants.USER.NAME);
        preferences.removeStoredString(TaskConstants.USER.EMAIL);
    }

    /**
     * Verifica se usuário está logado
     */
    public boolean isUserLogged() {
        SecurityPreferences securityPreferences = new SecurityPreferences(this.mContext);
        String tokenKey = securityPreferences.getStoredString(TaskConstants.HEADER.TOKEN_KEY);
        String personKey = securityPreferences.getStoredString(TaskConstants.HEADER.PERSON_KEY);

        // Se token e person key forem diferentes de vazio, usuário está logado
        return (!"".equals(tokenKey) && !"".equals(personKey));
    }

    /**
     * Carrega dados do usuário logado
     */
    public PersonEntity getUserLogged() {
        SecurityPreferences pref = new SecurityPreferences(this.mContext);

        PersonEntity entity = new PersonEntity();
        entity.setName(pref.getStoredString(TaskConstants.USER.NAME));
        entity.setEmail(pref.getStoredString(TaskConstants.USER.EMAIL));

        return entity;
    }

}
