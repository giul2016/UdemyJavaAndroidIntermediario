package com.devmasterteam.tasks.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.entity.TaskEntity;
import com.devmasterteam.tasks.listener.OnTaskListListener;
import com.devmasterteam.tasks.viewholder.TaskViewHolder;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<TaskEntity> mList;
    private OnTaskListListener mOnTaskListListener;

    /**
     * Construtor
     */
    public TaskListAdapter(List<TaskEntity> taskList, OnTaskListListener listener) {
        this.mList = taskList;
        this.mOnTaskListListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Obtém o contexto da aplicação
        Context context = parent.getContext();

        // Infla o layout da linha e faz uso na listagem
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_task_list, parent, false);

        // Retorna o elemento de interface pronto para ser usado
        return new TaskViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskEntity entity = this.mList.get(position);
        holder.bindData(entity, this.mOnTaskListListener);
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }
}
