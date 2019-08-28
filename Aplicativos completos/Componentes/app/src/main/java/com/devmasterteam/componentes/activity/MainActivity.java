package com.devmasterteam.componentes.activity;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devmasterteam.componentes.R;
import com.devmasterteam.componentes.data.SpinnerMock;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa elementos de interface
        this.mViewHolder.mButtonToast = findViewById(R.id.button_toast_me);
        this.mViewHolder.mButtonSnack = findViewById(R.id.button_snack_me);
        this.mViewHolder.mButtonGetSpinner = findViewById(R.id.button_get_spinner);
        this.mViewHolder.mButtonSetSpinner = findViewById(R.id.button_set_spinner);
        this.mViewHolder.mButtonGetSeekbar = findViewById(R.id.button_get_seek);
        this.mViewHolder.mButtonSetSeekbar = findViewById(R.id.button_set_seek);
        this.mViewHolder.mSpinnerDynamic = findViewById(R.id.spinner_dynamic);
        this.mViewHolder.mSeekBar = findViewById(R.id.seekbar_seek);
        this.mViewHolder.mTextSeekValue = findViewById(R.id.text_seek_value);
        this.mViewHolder.mConstraintLayout = findViewById(R.id.contraint_root_layout);

        // Carrega valores no spinner dinâmico
        this.loadSpinner();

        // Inicializa eventos aos elementos
        this.setListeners();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_toast_me) {

            Toast toast = Toast.makeText(this, "Toast me!", Toast.LENGTH_LONG);

            // Inflamos um layout criado especificamente para a toast
            LayoutInflater inflater = getLayoutInflater();
            View toastLayout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.constraint_root_layout));
            toast.setView(toastLayout);

            TextView textView = toast.getView().findViewById(R.id.text_message);
            textView.setText("Customizar mais ainda");
            // textView.setTextColor(Color.YELLOW);

            toast.show();

        } else if (id == R.id.button_snack_me) {

            Snackbar snackbar = Snackbar.make(this.mViewHolder.mConstraintLayout, "Snack me!", Snackbar.LENGTH_LONG);

            snackbar.setActionTextColor(Color.BLUE);
            snackbar.setAction("DESFAZER", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(mViewHolder.mConstraintLayout, "Message is restored!", Snackbar.LENGTH_SHORT).show();
                }
            });

            // Mudando a cor do plano de fundo
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

            // Mudando a cor do texto
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

            // Deprecated
            // getResources().getColor(R.color.colorPrimary);

            snackbar.show();

        } else if (id == R.id.button_get_spinner) {

            // String value = String.valueOf(this.mViewHolder.mSpinnerDynamic.getSelectedItem().toString());
            String value = String.valueOf(this.mViewHolder.mSpinnerDynamic.getSelectedItemId());
            // String value = String.valueOf(this.mViewHolder.mSpinnerDynamic.getSelectedItemPosition());
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();

        } else if (id == R.id.button_set_spinner) {

            // Atribuição é feita somente por posição do elemento dentro do spinner
            this.mViewHolder.mSpinnerDynamic.setSelection(3);

        } else if (id == R.id.button_get_seek) {

            String value = String.valueOf(this.mViewHolder.mSeekBar.getProgress());
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();

        } else if (id == R.id.button_set_seek) {

            this.mViewHolder.mSeekBar.setProgress(10);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String value = adapterView.getItemAtPosition(position).toString();
        Toast.makeText(this, value, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "NOTHING", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.seekbar_seek) {
            this.mViewHolder.mTextSeekValue.setText(String.valueOf(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /**
     * Atribui eventos aos elementos
     */
    private void setListeners() {

        // Botões
        this.mViewHolder.mButtonToast.setOnClickListener(this);
        this.mViewHolder.mButtonSnack.setOnClickListener(this);
        this.mViewHolder.mButtonGetSpinner.setOnClickListener(this);
        this.mViewHolder.mButtonSetSpinner.setOnClickListener(this);
        this.mViewHolder.mButtonGetSeekbar.setOnClickListener(this);
        this.mViewHolder.mButtonSetSeekbar.setOnClickListener(this);

        // Spinner
        this.mViewHolder.mSpinnerDynamic.setOnItemSelectedListener(this);

        // Seekbar
        this.mViewHolder.mSeekBar.setOnSeekBarChangeListener(this);

    }

    /**
     * Carrega spinner dinâmico
     */
    private void loadSpinner() {

        // Carrega dados
        List<String> lst = SpinnerMock.getList();

        // Cria o adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lst);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Atribui o adapter ao spinner
        this.mViewHolder.mSpinnerDynamic.setAdapter(adapter);
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        Button mButtonToast;
        Button mButtonSnack;
        Spinner mSpinnerDynamic;
        Button mButtonGetSpinner;
        Button mButtonSetSpinner;
        Button mButtonGetSeekbar;
        Button mButtonSetSeekbar;
        SeekBar mSeekBar;
        TextView mTextSeekValue;
        ConstraintLayout mConstraintLayout;
    }

}
