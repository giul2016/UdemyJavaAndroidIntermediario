package com.devmasterteam.tasks.infra.operation;

public interface OperationListener<T> {

    void onSuccess(T result);

    void onError(int error, String message);

}
