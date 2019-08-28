package com.devmasterteam.tasks.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.business.PriorityBusiness;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entity.PriorityEntity;
import com.devmasterteam.tasks.entity.TaskEntity;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.manager.TaskManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskFormActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private TaskManager mTaskManager;
    private ViewHolder mViewHolder = new ViewHolder();
    private List<PriorityEntity> mPriorityEntityList;
    private List<Integer> mPriorityEntityListId;
    private PriorityBusiness mPriorityBusiness;
    private int mTaskId = 0;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        // Inicializa variáveis
        this.mViewHolder.mImageToolbarBack = this.findViewById(R.id.image_toolbar_back);
        this.mViewHolder.mTextToolbar = this.findViewById(R.id.text_toolbar);
        this.mViewHolder.mEditDescription = this.findViewById(R.id.edit_description);
        this.mViewHolder.mCheckComplete = this.findViewById(R.id.check_complete);
        this.mViewHolder.mSpinnerPriority = this.findViewById(R.id.spinner_priority);
        this.mViewHolder.mButtonDate = this.findViewById(R.id.button_date);
        this.mViewHolder.mButtonSave = this.findViewById(R.id.button_save);
        this.mViewHolder.mProgressDialog = new ProgressDialog(this);

        this.mPriorityBusiness = new PriorityBusiness(this);
        this.mTaskManager = new TaskManager(this);
        this.mPriorityEntityList = new ArrayList<>();
        this.mPriorityEntityListId = new ArrayList<>();
        this.mContext = this;

        // Atribui eventos
        this.mViewHolder.mButtonSave.setOnClickListener(this);
        this.mViewHolder.mButtonDate.setOnClickListener(this);
        this.mViewHolder.mImageToolbarBack.setOnClickListener(this);

        // Carrega valores
        this.loadPriorities();

        // Carrega dados passados para a activity
        this.loadDataFromActivity();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_save) {
            this.handleSave();
        } else if (id == R.id.button_date) {
            this.showDateDialog();
        } else if (id == R.id.image_toolbar_back) {
            this.onBackPressed();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        String strDate = SIMPLE_DATE_FORMAT.format(calendar.getTime());
        this.mViewHolder.mButtonDate.setText(strDate);
    }

    /**
     * Trata click
     */
    private void handleSave() {
        // Loading
        this.showLoading(true, getString(R.string.salvando), getString(R.string.salvando_tarefa));

        try {
            TaskEntity taskEntity = new TaskEntity();

            taskEntity.setId(this.mTaskId);
            taskEntity.setDescription(this.mViewHolder.mEditDescription.getText().toString());
            taskEntity.setPriorityId(this.mPriorityEntityListId.get(this.mViewHolder.mSpinnerPriority.getSelectedItemPosition()));
            taskEntity.setComplete(this.mViewHolder.mCheckComplete.isChecked());

            if (!"".equals(this.mViewHolder.mButtonDate.getText()))
                taskEntity.setDueDate(SIMPLE_DATE_FORMAT.parse(this.mViewHolder.mButtonDate.getText().toString()));

            if (this.mTaskId == 0) {
                this.mTaskManager.create(taskEntity, taskSavedListener());
            } else {
                this.mTaskManager.update(taskEntity, taskSavedListener());
            }

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.UNEXPECTED_ERROR), Toast.LENGTH_LONG).show();
            this.showLoading(false, "", "");
        }
    }

    /**
     * Carrega dados de edição
     */
    private void loadDataFromActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASK_ID, 0);

            // Carrega tarefa
            if (this.mTaskId != 0) {
                this.mViewHolder.mTextToolbar.setText(R.string.atualizar_tarefa);
                this.mViewHolder.mButtonSave.setText(R.string.atualizar_tarefa);
                this.mTaskManager.get(this.mTaskId, taskLoadedListener());
            }

        }
    }

    /**
     * Carrega prioridades
     */
    private void loadPriorities() {

        // Lista de prioridades do banco de dados local
        this.mPriorityEntityList = this.mPriorityBusiness.getListLocal();

        List<String> list = new ArrayList<>();
        for (PriorityEntity priorityEntity : this.mPriorityEntityList) {
            list.add(priorityEntity.getDescription());
            this.mPriorityEntityListId.add(priorityEntity.getId());
        }

        // Cria adapter e usa no elemento
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        this.mViewHolder.mSpinnerPriority.setAdapter(adapter);

    }

    /**
     * Mostra datepicker de seleção
     */
    private void showDateDialog() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, this, year, month, day).show();
    }

    /**
     * Mostra ou esconde loading de frases enquanto consulta no servidor é feita
     */
    private void showLoading(Boolean show, String title, String message) {
        if (show) {
            this.mViewHolder.mProgressDialog.setTitle(title);
            this.mViewHolder.mProgressDialog.setMessage(message);
            this.mViewHolder.mProgressDialog.show();
        } else {
            this.mViewHolder.mProgressDialog.hide();
            this.mViewHolder.mProgressDialog.dismiss();
        }
    }

    /**
     * Obtém o indexo do valor carregado
     */
    private int getIndex(int priorityId) {
        int index = 0;
        for (int i = 0; i < this.mPriorityEntityList.size(); i++) {
            if (this.mPriorityEntityList.get(i).getId() == priorityId) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Listener quando a tarefa é carregada da API
     */
    private OperationListener<TaskEntity> taskLoadedListener() {
        return new OperationListener<TaskEntity>() {

            @Override
            public void onSuccess(TaskEntity result) {
                mViewHolder.mEditDescription.setText(result.getDescription());
                mViewHolder.mButtonDate.setText(SIMPLE_DATE_FORMAT.format(result.getDueDate()));
                mViewHolder.mCheckComplete.setChecked(result.getComplete());
                mViewHolder.mSpinnerPriority.setSelection(getIndex(result.getPriorityId()));
            }

            @Override
            public void onError(int error, String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * Listener quando a tarefa é salva
     */
    private OperationListener<Boolean> taskSavedListener() {
        return new OperationListener<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {

                // Esconde mensagem
                showLoading(false, "", "");

                // Notifica o usuário
                if (mTaskId != 0) {
                    Toast.makeText(mContext, R.string.tarefa_atualizada_com_sucesso, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.tarefa_incluida_com_sucesso, Toast.LENGTH_LONG).show();
                }

                // Finaliza activity
                finish();
            }

            @Override
            public void onError(int error, String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                showLoading(false, "", "");
            }
        };
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        private ImageView mImageToolbarBack;
        private TextView mTextToolbar;
        private EditText mEditDescription;
        private CheckBox mCheckComplete;
        private Spinner mSpinnerPriority;
        private Button mButtonDate;
        private Button mButtonSave;
        private ProgressDialog mProgressDialog;
    }
}
