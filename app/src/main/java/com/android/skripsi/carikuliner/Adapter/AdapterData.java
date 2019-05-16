package com.android.skripsi.carikuliner.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.skripsi.carikuliner.model.Alternatif;
import com.android.skripsi.carikuliner.R;
import com.android.skripsi.carikuliner.model.SelectedAlternatif;

import java.util.ArrayList;
import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.VHolder>{

    public interface OnCheckedItemListener {
        void onCheckedItem(String item);
        void onUncheckedItem(String item);
    }

    @NonNull
    private OnCheckedItemListener listener;

    List<Alternatif> list;
    public List<SelectedAlternatif> listChecked;
    public boolean isSelectedAll = false;
    public boolean isUnselectedAll = true;

    public SparseBooleanArray boolArray;

    public AdapterData(List<Alternatif> mData, @NonNull OnCheckedItemListener listener){
        list = mData;
        listChecked = new ArrayList<>();
        this.listener = listener;
        boolArray = new SparseBooleanArray();
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pilih_data, parent, false);
        VHolder vHolder = new VHolder(mView);
        return vHolder;
    }

    public void setSelectedAll(){
        Log.d("success", "All Selected");
        isSelectedAll = true;
        for (int i = 0; i < getItemCount(); i++){
            if(!list.get(i).isChecked()){
                listener.onCheckedItem(list.get(i).getId());
                list.get(i).setChecked(true);
            }
        }
        notifyDataSetChanged();
    }

    public void setUnselectedAll(){
        Log.d("success", "All Unselected");
        isSelectedAll = false;
        for (int i = 0; i < getItemCount(); i++){
            if(list.get(i).isChecked()){
                listener.onUncheckedItem(list.get(i).getId());
                list.get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final VHolder holder, final int position) {
        holder.txtNama.setText(list.get(position).getNamaTempat());
        holder.txtAlamat.setText(list.get(position).getAlamat());
        holder.cb.setOnCheckedChangeListener(null);
        holder.cb.setChecked(list.get(position).isChecked());
        ((VHolder) holder).setOnCheckedItemListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(list.get(position).isChecked()){
                    listener.onUncheckedItem(list.get(position).getId());
                    holder.cb.setChecked(false);
                    list.get(position).setChecked(false);
                    notifyItemChanged(position);
                    Log.d("Message Remove", String.valueOf(list.get(position).getId()) + " is removed");
                }
                else{
                    listener.onCheckedItem(list.get(position).getId());
                    holder.cb.setChecked(true);
                    list.get(position).setChecked(true);
                    notifyItemChanged(position);
                    Log.d("Message Added", String.valueOf(list.get(position).getId()) + " is added");
                }
            }
        });
        ((VHolder) holder).setOnCheckBoxClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(position).isChecked()){
                    listener.onUncheckedItem(list.get(position).getId());
                    holder.cb.setChecked(false);
                    list.get(position).setChecked(false);
                    notifyItemChanged(position);
                    Log.d("Message Remove", String.valueOf(list.get(position).getId()) + " is removed");
                }
                else{
                    listener.onCheckedItem(list.get(position).getId());
                    holder.cb.setChecked(true);
                    list.get(position).setChecked(true);
                    notifyItemChanged(position);
                    Log.d("Message Added", String.valueOf(list.get(position).getId()) + " is added");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        public TextView txtNama, txtAlamat;
        public CheckBox cb;

        public VHolder(View itemView) {
            super(itemView);
            txtNama = (TextView)itemView.findViewById(R.id.vNamaData);
            txtAlamat = (TextView)itemView.findViewById(R.id.vAlamatData);
            cb = itemView.findViewById(R.id.cbData);
        }

        public void setOnCheckedItemListener(CheckBox.OnCheckedChangeListener onChange){
            cb.setOnCheckedChangeListener(onChange);
        }

        public void setOnCheckBoxClickListener(View.OnClickListener onClick){
            itemView.setOnClickListener(onClick);
        }
    }
}
