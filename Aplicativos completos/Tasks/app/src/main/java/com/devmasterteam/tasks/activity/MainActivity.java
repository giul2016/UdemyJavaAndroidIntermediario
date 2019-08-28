package com.devmasterteam.tasks.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.business.PersonBusiness;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entity.PersonEntity;
import com.devmasterteam.tasks.entity.PriorityEntity;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.manager.PriorityManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private PersonBusiness mPersonBusiness;
    private PriorityManager mPriorityManager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mContext = this;
        this.mPersonBusiness = new PersonBusiness(this.mContext);
        this.mPriorityManager = new PriorityManager(this.mContext);

        // Obtém elementos de interface
        this.mViewHolder.mFloatAddTask = findViewById(R.id.float_add_task);
        this.mViewHolder.mDrawerLayout = findViewById(R.id.drawer_layout);

        // Drawer - Navegação lateral
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.mViewHolder.mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.mViewHolder.mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Seleciona o primeiro item do menu
        navigationView.setCheckedItem(R.id.nav_all_tasks);

        // Eventos
        this.mViewHolder.mFloatAddTask.setOnClickListener(this);

        // Formata boas-vindas
        this.welcome();

        // Carga inicial de dados
        this.initialLoad();
    }

    @Override
    public void onBackPressed() {
        if (this.mViewHolder.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mViewHolder.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla o menu de opções - Canto direito superior
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Obtém o ID do menu selecionado
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            this.handleLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Obtém o ID do menu selecionado
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_all_tasks) {
            fragment = TaskListFragment.newInstance(TaskConstants.TASKFILTER.NO_FILTER);
        } else if (id == R.id.nav_next_seven_days) {
            fragment = TaskListFragment.newInstance(TaskConstants.TASKFILTER.NEXT_7_DAYS);
        } else if (id == R.id.nav_overdue) {
            fragment = TaskListFragment.newInstance(TaskConstants.TASKFILTER.OVERDUE);
        } else if (id == R.id.nav_logout) {
            this.handleLogout();
            return true;
        }

        // Insere fragment substituindo qualquer existente
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).commit();

        // Fecha a navegação
        this.mViewHolder.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.float_add_task) {
            startActivity(new Intent(this, TaskFormActivity.class));
        }
    }

    /**
     * Carrega dados do usuário logado
     */
    private void welcome() {
        PersonEntity entity = this.mPersonBusiness.getUserLogged();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        TextView name = header.findViewById(R.id.text_name);
        TextView email = header.findViewById(R.id.text_email);
        name.setText(entity.getName());
        email.setText(entity.getEmail());
    }

    /**
     * Incia a fragment padrão
     */
    private void startDefaultFragment() {
        Fragment fragment = TaskListFragment.newInstance(TaskConstants.TASKFILTER.NO_FILTER);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).commit();
    }

    /**
     * Faz logout do usuário
     */
    private void handleLogout() {

        // Limpa dados do usuário
        this.mPersonBusiness.clearData();

        // Inicia login novamente
        startActivity(new Intent(this, LoginActivity.class));

        // Impede que seja possível voltar
        finish();
    }

    /**
     * Carrega as prioridades
     */
    private void initialLoad() {
        this.mPriorityManager.getList(priorityListener());
    }

    /**
     * Listener para quando lista de prioridades é obtida
     */
    private OperationListener<List<PriorityEntity>> priorityListener() {
        return new OperationListener<List<PriorityEntity>>() {

            @Override
            public void onSuccess(List<PriorityEntity> result) {
                // Iniciliza a fragment default somente após ter os valores de prioridade
                startDefaultFragment();
            }

            @Override
            public void onError(int error, String errorMessage) {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }

        };
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        DrawerLayout mDrawerLayout;
        FloatingActionButton mFloatAddTask;
    }
}
