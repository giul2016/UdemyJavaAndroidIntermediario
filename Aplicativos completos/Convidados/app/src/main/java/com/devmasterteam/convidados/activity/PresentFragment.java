package com.devmasterteam.convidados.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devmasterteam.convidados.R;
import com.devmasterteam.convidados.adapter.GuestListAdapter;
import com.devmasterteam.convidados.business.GuestBusiness;
import com.devmasterteam.convidados.constants.GuestConstants;
import com.devmasterteam.convidados.entity.GuestEntity;
import com.devmasterteam.convidados.listener.OnGuestListener;

import java.util.List;

public class PresentFragment extends Fragment {

    private ViewHolder mViewHolder = new ViewHolder();
    private OnGuestListener mOnGuestListener;
    private GuestBusiness mGuestBusiness;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_present, container, false);
        this.mContext = view.getContext();

        // Variáveis
        this.mGuestBusiness = new GuestBusiness(this.mContext);

        // 1 - Obter a recyclerview & definir um layout
        this.mViewHolder.mRecyclerView = view.findViewById(R.id.recyler_all_present);
        this.mViewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));

        // Ações na listagem
        this.mOnGuestListener = new OnGuestListener() {
            @Override
            public void onListClick(int guestId) {
                Bundle bundle = new Bundle();
                bundle.putInt(GuestConstants.BundleConstants.GUEST_ID, guestId);

                Intent intent = new Intent(mContext, GuestFormActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }

            @Override
            public void onDeleteClick(int guestId) {
                // Faz a remoção do banco de dados
                mGuestBusiness.remove(guestId);

                // Notifica que convidado foi salvo
                Toast.makeText(mContext, R.string.removido_com_sucesso, Toast.LENGTH_LONG).show();

                // Atualiza listagem e dashboard
                loadGuests();
            }
        };

        // Retorna view de interface
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Atualiza listagem
        this.loadGuests();
    }

    /**
     * Atualiza listagem
     */
    private void loadGuests() {
        List<GuestEntity> mListGuests = this.mGuestBusiness.getConfirmed();

        // 2 - Definir adapter passando listagem de convidados
        GuestListAdapter mGuestListAdapter = new GuestListAdapter(mListGuests, mOnGuestListener);
        this.mViewHolder.mRecyclerView.setAdapter(mGuestListAdapter);

        // Notifica a Recycler que o conjunto de dados foi alterado
        mGuestListAdapter.notifyDataSetChanged();
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        RecyclerView mRecyclerView;
    }
}
