package com.devmasterteam.relogiodecabeceira;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final ViewHolder mViewHolder = new ViewHolder();
    private final Handler mHandler = new Handler();
    private Runnable mRunner;
    private boolean mTickerStopped = false;
    private Boolean mBatteryOn = true;

    // Método para recebimento de evento de alteração de bateria
    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mViewHolder.mTextBattery.setText(String.format("%s%%", String.valueOf(level)));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.registerReceiver(this.mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        // Remove a barra superior
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Faz com que o dispositivo não bloqueie desde que permaneça nessa activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializa componentes
        this.mViewHolder.mTextHourMinute = this.findViewById(R.id.text_hour_minute);
        this.mViewHolder.mTextSeconds = this.findViewById(R.id.text_second);
        this.mViewHolder.mTextBattery = this.findViewById(R.id.text_battery);
        this.mViewHolder.mCheckBattery = this.findViewById(R.id.checkbox_battery);
        this.mViewHolder.mImageOptions = this.findViewById(R.id.image_options);
        this.mViewHolder.mImageHideOptions = this.findViewById(R.id.image_close);
        this.mViewHolder.mLinearLayoutOption = this.findViewById(R.id.linear_option);

        // Ações botões da tela
        this.setListeners();

        // Faz com que o elemento animado fique deslocado na actvitity e assim não visível
        this.mViewHolder.mLinearLayoutOption.animate().translationY(400);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mTickerStopped = false;
        this.startBedside();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mTickerStopped = true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.checkbox_battery) {
            this.toggleBatteryLevel();
        } else if (id == R.id.image_options) {
            this.mViewHolder.mLinearLayoutOption.setVisibility(View.VISIBLE);

            this.mViewHolder.mLinearLayoutOption.animate()
                    .translationY(0)
                    .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        } else if (id == R.id.image_close) {
            this.mViewHolder.mLinearLayoutOption.animate()
                    .translationY(this.mViewHolder.mLinearLayoutOption.getMeasuredHeight())
                    .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        }
    }

    /**
     * Mostra ou remove o nível da bateria
     */
    private void toggleBatteryLevel() {
        if (this.mBatteryOn) {
            this.mBatteryOn = false;
            this.mViewHolder.mTextBattery.setVisibility(View.GONE);
        } else {
            this.mBatteryOn = true;
            this.mViewHolder.mTextBattery.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Inicializa o relógio de cabeceira
     */
    private void startBedside() {

        // Inicializa o calendário
        final Calendar calendar = Calendar.getInstance();

        this.mRunner = new Runnable() {
            @Override
            public void run() {

                if (mTickerStopped)
                    return;

                // Obtém o tempo do celular e atribui ao calendário criado
                calendar.setTimeInMillis(System.currentTimeMillis());

                // Converte
                String hourMinutesFormat = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                String secondsFormat = String.format("%02d", calendar.get(Calendar.SECOND));

                // Atribui
                mViewHolder.mTextHourMinute.setText(hourMinutesFormat);
                mViewHolder.mTextSeconds.setText(secondsFormat);

                // Cálculo de quando a thread vai rodar novamente
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - (now % 1000));
                mHandler.postAtTime(mRunner, next);
            }
        };
        this.mRunner.run();
    }

    /**
     * Ações botões na tela
     */
    private void setListeners() {
        this.mViewHolder.mCheckBattery.setOnClickListener(this);
        this.mViewHolder.mImageOptions.setOnClickListener(this);
        this.mViewHolder.mImageHideOptions.setOnClickListener(this);
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        TextView mTextHourMinute;
        TextView mTextSeconds;
        TextView mTextBattery;
        CheckBox mCheckBattery;
        ImageView mImageOptions;
        ImageView mImageHideOptions;
        LinearLayout mLinearLayoutOption;
    }

}