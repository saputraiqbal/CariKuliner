package com.android.skripsi.carikuliner.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.skripsi.carikuliner.RecommendationActivity;
import com.android.skripsi.carikuliner.model.Kategori;
import com.android.skripsi.carikuliner.R;

import java.util.List;

public class AdapterKategori extends RecyclerView.Adapter<AdapterKategori.VHolder>{

    List<Kategori> list;

    public SparseBooleanArray boolArray;

    public AdapterKategori(List<Kategori> mData){
        list = mData;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pilih_kategori, parent, false);
        VHolder vHolder = new VHolder(mView);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final VHolder holder, final int position) {
        holder.txtKategori.setText(list.get(position).getKategori());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = list.get(position).getId();
                final Context ctx = v.getContext();
                SharedPreferences shares = ctx.getSharedPreferences("value_stores", 0);
                SharedPreferences.Editor editor = shares.edit();
                editor.putString("cat", category);
                editor.commit();
                Log.d("category", "Category " + list.get(position).getKategori() + " has chosen");
                Intent toResult = new Intent(ctx, RecommendationActivity.class);
                ctx.startActivity(toResult);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        public TextView txtKategori;

        public VHolder(View itemView) {
            super(itemView);
            txtKategori = (TextView)itemView.findViewById(R.id.vKategori);
        }
    }
}
