package com.devmasterteam.tasks.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.adapter.TaskListAdapter;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entity.TaskEntity;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.listener.OnTaskListListener;
import com.devmasterteam.tasks.manager.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment {

    private ViewHolder mViewHolder = new ViewHolder();
    private OnTaskListListener mOnTaskListListener;
    private List<TaskEntity> mTaskEntityList;
    private TaskListAdapter mTaskListAdapter;
    private TaskManager mTaskManager;
    private Context mContext;
    private int mFilter;

    public static TaskListFragment newInstance(int filter) {
        TaskListFragment fragment = new TaskListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(TaskConstants.TASKFILTER.FILTER_KEY, filter);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mFilter = getArguments().getInt(TaskConstants.TASKFILTER.FILTER_KEY, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Infla o layout
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Incializa as variáveis
        this.mContext = view.getContext();
        this.mTaskManager = new TaskManager(this.mContext);

        // Inicializa elementos
        this.mViewHolder.mRecylerTaskList = view.findViewById(R.id.recycler_task_list);
        this.mViewHolder.mTextMessage = view.findViewById(R.id.text_message);

        // 2 - Definir adapter passando listagem de itens
        this.mTaskEntityList = new ArrayList<>();
        this.mTaskListAdapter = new TaskListAdapter(this.mTaskEntityList, this.mOnTaskListListener);
        this.mViewHolder.mRecylerTaskList.setAdapter(mTaskListAdapter);

        // 3 - Definir um layout
        this.mViewHolder.mRecylerTaskList.setLayoutManager(new LinearLayoutManager(this.mContext));

        // Inicializa listener
        this.createInteractionListener();

        // Retorna view
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.loadTasks();
    }

    /**
     * Carrega tarefas
     */
    private void loadTasks() {
        this.mTaskEntityList = new ArrayList<>();
        this.mTaskManager.getList(this.mFilter, tasksLoaded());
    }

    /**
     * Interação com a listagem de tarefas
     */
    private void createInteractionListener() {
        this.mOnTaskListListener = new OnTaskListListener() {
            @Override
            public void onListClick(int taskId) {
                Bundle bundle = new Bundle();
                bundle.putInt(TaskConstants.BUNDLE.TASK_ID, taskId);

                Intent intent = new Intent(mContext, TaskFormActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int taskId) {
                mTaskManager.delete(taskId, taskDeletedListener());
            }

            @Override
            public void onCompleteClick(int taskId) {
                mTaskManager.complete(taskId, true, taskUpdatedListener());
            }

            @Override
            public void onUncompleteClick(int taskId) {
                mTaskManager.complete(taskId, false, taskUpdatedListener());
            }
        };
    }

    /**
     * Listener para quando uma tarefa é removida
     */
    private OperationListener<Boolean> taskDeletedListener() {
        return new OperationListener<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                Toast.makeText(mContext, R.string.tarefa_removida_com_sucesso, Toast.LENGTH_LONG).show();
                loadTasks();
            }

            @Override
            public void onError(int error, String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }

        };
    }

    /**
     * Listener para quando uma tarefa é completa ou não completa
     */
    private OperationListener<Boolean> taskUpdatedListener() {
        return new OperationListener<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                loadTasks();
            }

            @Override
            public void onError(int error, String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }

        };
    }

    /**
     * Listener para quando a listagem de tarefas seja obtida
     */
    private OperationListener<List<TaskEntity>> tasksLoaded() {
        return new OperationListener<List<TaskEntity>>() {

            @Override
            public void onSuccess(List<TaskEntity> result) {
                mTaskEntityList.addAll(result);
                mTaskListAdapter = new TaskListAdapter(mTaskEntityList, mOnTaskListListener);
                mViewHolder.mRecylerTaskList.setAdapter(mTaskListAdapter);
                mTaskListAdapter.notifyDataSetChanged();

                // Trata lista vazia
                if (result.size() == 0) {
                    mViewHolder.mTextMessage.setVisibility(View.VISIBLE);
                    mViewHolder.mRecylerTaskList.setVisibility(View.GONE);
                } else {
                    mViewHolder.mTextMessage.setVisibility(View.GONE);
                    mViewHolder.mRecylerTaskList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(int error, String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }

        };
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        private RecyclerView mRecylerTaskList;
        private TextView mTextMessage;
    }

}
