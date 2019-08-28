package com.devmasterteam.componentes.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.devmasterteam.componentes.R;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        this.mViewHolder.mProgressBar = findViewById(R.id.progress_horizontal);
        this.mViewHolder.mImageCheck = findViewById(R.id.image_check);
        this.mViewHolder.mButtonImage = findViewById(R.id.button_image);
        this.mViewHolder.mSwitch = findViewById(R.id.switch_button);
        this.mViewHolder.mButtonGetSwitch = findViewById(R.id.button_get_switch);
        this.mViewHolder.mButtonSetSwitch = findViewById(R.id.button_set_switch);
        this.mViewHolder.mButtonGetCheckbox = findViewById(R.id.button_get_check);
        this.mViewHolder.mButtonSetCheckbox = findViewById(R.id.button_set_check);
        this.mViewHolder.mRadioYes = findViewById(R.id.radio_yes);
        this.mViewHolder.mRadioNo = findViewById(R.id.radio_no);
        this.mViewHolder.mRadioGroup = findViewById(R.id.radio_group);
        this.mViewHolder.mCheckBox = findViewById(R.id.check_button);

        this.mViewHolder.mProgressDialog = new ProgressDialog(this);

        this.mViewHolder.mProgressDialog.setTitle("Título");
        this.mViewHolder.mProgressDialog.setMessage("Minha mensagem");
        // this.mViewHolder.mProgressDialog.show();

        // this.mViewHolder.mProgressDialog.hide();
        // this.mViewHolder.mProgressDialog.dismiss();

        // Eventos
        this.setListeners();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_image) {
            this.mViewHolder.mImageCheck.setImageResource(R.drawable.ic_check);
        } else if (id == R.id.button_get_switch) {
            String value = String.valueOf(this.mViewHolder.mSwitch.isChecked());
            Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.button_set_switch) {
            this.mViewHolder.mSwitch.setChecked(false);
        }else if (id == R.id.button_get_check) {
            String value = String.valueOf(this.mViewHolder.mCheckBox.isChecked());
            Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.button_set_check) {
            this.mViewHolder.mCheckBox.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        if (id == R.id.switch_button) {
            Toast.makeText(this, "Switch changed", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.check_button) {
            Toast.makeText(this, "Check changed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int cheched) {
        int id = radioGroup.getId();
        if (id == R.id.radio_group) {
            String value;
            if (mViewHolder.mRadioYes.isChecked()) {
                value = "Radio On";
            } else {
                value = "Radio Off";
            }
            Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Atribui eventos aos elementos
     */
    private void setListeners() {
        this.mViewHolder.mButtonImage.setOnClickListener(this);
        this.mViewHolder.mButtonGetSwitch.setOnClickListener(this);
        this.mViewHolder.mButtonSetSwitch.setOnClickListener(this);
        this.mViewHolder.mButtonGetCheckbox.setOnClickListener(this);
        this.mViewHolder.mButtonSetCheckbox.setOnClickListener(this);

        this.mViewHolder.mSwitch.setOnCheckedChangeListener(this);
        this.mViewHolder.mCheckBox.setOnCheckedChangeListener(this);
        this.mViewHolder.mRadioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        ProgressBar mProgressBar;
        ProgressDialog mProgressDialog;
        ImageView mImageCheck;
        Switch mSwitch;
        Button mButtonImage;
        Button mButtonGetSwitch;
        Button mButtonSetSwitch;
        RadioButton mRadioYes;
        RadioButton mRadioNo;
        RadioGroup mRadioGroup;
        CheckBox mCheckBox;
        Button mButtonGetCheckbox;
        Button mButtonSetCheckbox;
    }
}
