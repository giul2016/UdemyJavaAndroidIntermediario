package com.devmasterteam.carros.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmasterteam.carros.R;
import com.devmasterteam.carros.entities.Car;
import com.devmasterteam.carros.listener.OnListClickInteractionListener;

/**
 * Responsável por fazer as manipulações de elementos de interface
 */
public class CarViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private ImageView mImgCarPicture;
    private TextView mTextCarModel;
    private TextView mTextViewDetails;


    /**
     * Construtor
     */
    public CarViewHolder(View itemView) {
        super(itemView);
        this.mImgCarPicture = (ImageView) itemView.findViewById(R.id.img_car_pic);
        this.mTextCarModel = (TextView) itemView.findViewById(R.id.text_car_model);
        this.mTextViewDetails = (TextView) itemView.findViewById(R.id.text_view_details);
    }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Car car, final OnListClickInteractionListener listener) {

        // Altera valor
        this.mTextCarModel.setText(car.model);
        this.mImgCarPicture.setImageDrawable(car.picture);

        // Adciona evento de click
        this.mTextViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(car.id);
            }
        });
    }

}
