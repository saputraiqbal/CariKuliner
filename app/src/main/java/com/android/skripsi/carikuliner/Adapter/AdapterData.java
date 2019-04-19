package com.android.skripsi.carikuliner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.skripsi.carikuliner.model.Alternatif;
import com.android.skripsi.carikuliner.R;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.VHolder>{

    List<Alternatif> list;

    public AdapterData(List<Alternatif> mData){
        list = mData;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pilih_data, parent, false);
        VHolder vHolder = new VHolder(mView);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(VHolder holder, int position) {
        holder.txtNama.setText(list.get(position).getNamaTempat());
        holder.txtAlamat.setText(list.get(position).getAlamat());
    }

    @Override
    public int getItemCount() {
        return list.size();
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
