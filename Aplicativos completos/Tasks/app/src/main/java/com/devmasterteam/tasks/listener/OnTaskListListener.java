package com.devmasterteam.tasks.listener;

public interface OnTaskListListener {

    /**
     * Click para edição
     */
    void onListClick(int taskId);

    /**
     * Remoção
     */
    void onDeleteClick(int taskId);

    /**
     * Completa tarefa
     */
    void onCompleteClick(int taskId);

    /**
     * Descompleta tarefa
     */
    void onUncompleteClick(int taskId);

}
