package com.devmasterteam.tasks.business;

import android.content.Context;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.infra.exception.InternetNotAvailableException;
import com.devmasterteam.tasks.infra.security.SecurityPreferences;
import com.google.gson.Gson;

import java.util.AbstractMap;
import java.util.HashMap;

class BaseBusiness {

    private Context mContext;

    /**
     * Construtor
     */
    BaseBusiness(Context context) {
        this.mContext = context;
    }

    /**
     * Obtém os parâmetros do Header que são adiconados em cada requisição para garantir integridade
     * Parâmetros são armazenados uma única vez quando o login é feito ou perfil é criado
     */
    AbstractMap<String, String> getHeaderParameters() {

        // Carrega classe com o contexto
        SecurityPreferences preferences = new SecurityPreferences(this.mContext);

        // Monta hash
        AbstractMap<String, String> headerParameters = new HashMap<>();
        headerParameters.put(TaskConstants.HEADER.PERSON_KEY, preferences.getStoredString(TaskConstants.HEADER.PERSON_KEY));
        headerParameters.put(TaskConstants.HEADER.TOKEN_KEY, preferences.getStoredString(TaskConstants.HEADER.TOKEN_KEY));

        // Retorna cabeçalho de requisição
        return headerParameters;
    }

    /**
     * Obtém mensagem de erro a partir do Json
     */
    String getErrorMessage(String json) {
        return new Gson().fromJson(json, String.class);
    }

    /**
     * Trata código de erro
     */
    int handleResponseCode(int statusCode) {
        if (statusCode == TaskConstants.STATUS_CODE.NOT_FOUND) {
            return TaskConstants.STATUS_CODE.NOT_FOUND;
        } else if (statusCode == TaskConstants.STATUS_CODE.FORBIDDEN) {
            return TaskConstants.STATUS_CODE.FORBIDDEN;
        } else {
            return TaskConstants.STATUS_CODE.INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * Faz o tratamento da exceção que foi causada no código
     */
    String handleExceptionMessage(Exception e) {
        if (e instanceof InternetNotAvailableException) {
            return this.mContext.getString(R.string.INTERNET_NOT_AVAILABLE);
        } else {
            return this.mContext.getString(R.string.UNEXPECTED_ERROR);
        }
    }

    /**
     * Faz o tratamento da exceção que foi causada no código
     */
    int handleExceptionCode(Exception e) {
        if (e instanceof InternetNotAvailableException) {
            return TaskConstants.STATUS_CODE.INTERNET_NOT_AVAILABLE;
        } else {
            return TaskConstants.STATUS_CODE.INTERNAL_SERVER_ERROR;
        }
    }

}
