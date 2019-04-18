package com.android.skripsi.carikuliner.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.skripsi.carikuliner.Model.PilihData;
import com.android.skripsi.carikuliner.R;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.VHolder>{

    List<PilihData> mData;

    public AdapterData(List<PilihData> mData){
        mData = mData;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pilih_data, parent, false);
        VHolder vHolder = new VHolder(mView);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(VHolder holder, int position) {
        holder.txtNama.setText(mData.get(position).getNamaTempat());
        holder.txtAlamat.setText(mData.get(position).getAlamat());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        public TextView txtNama, txtAlamat;

        public VHolder(View itemView) {
            super(itemView);
            txtNama = (TextView)itemView.findViewById(R.id.vNamaData);
            txtAlamat = (TextView)itemView.findViewById(R.id.vAlamatData);
        }
    }

}
