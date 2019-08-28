package com.devmasterteam.tasks.infra.operation;

public class OperationResult<T> {

    public static final int NO_ERROR = -1;

    private T anonimousType;
    private int mError = NO_ERROR;
    private String errorMessage = "";

    public int getError() {
        return mError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Seta o que ocorreu de errado na requisição e mostra mensagem da API
     */
    public void setError(int error, String errorMessage) {
        this.mError = error;
        this.errorMessage = errorMessage;
    }

    public T getResult() {
        return anonimousType;
    }

    public void setResult(T result) {
        anonimousType = result;
    }
}