package com.android.skripsi.carikuliner.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.skripsi.carikuliner.DetailActivity;
import com.android.skripsi.carikuliner.R;
import com.android.skripsi.carikuliner.model.Rekomendasi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class AdapterResult extends RecyclerView.Adapter<AdapterResult.VHolder> {

    List<Rekomendasi> listRekomendasi;

    public AdapterResult(List<Rekomendasi> listRekomendasi) {
        this.listRekomendasi = listRekomendasi;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result, parent, false);
        VHolder vHolder = new VHolder(mView);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        final Rekomendasi rekomendasi = listRekomendasi.get(position);
        Log.d("whatisit", rekomendasi.getNamaTempat());
        holder.resultName.setText(rekomendasi.getNamaTempat());
        DecimalFormat format = new DecimalFormat("#.##");
        format.setRoundingMode(RoundingMode.CEILING);
        String jarak = format.format(rekomendasi.getJarak()) + " km dari posisimu saat ini";
        holder.resultDistance.setText(jarak);
        Glide.with(holder.itemView.getContext())
                .load(applyBadges(position))
                .apply(RequestOptions.overrideOf(50,50))
                .into(holder.imgBadges);
        final String uriLocs = "http://maps.google.com/maps?daddr=" + rekomendasi.getLatTempat() + "," + rekomendasi.getLonTempat();
        holder.goToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Context ctx = v.getContext();
                Log.d("uriLocs", uriLocs);
                Intent toMaps = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uriLocs));
                if (toMaps.resolveActivity(ctx.getPackageManager()) != null){
                    ctx.startActivity(toMaps);
                }else{
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
                    dialogBuilder.setMessage("Aplikasi Google Maps belum terpasang pada perangkat Anda. Mohon pasang terlebih dahulu untuk mendapatkan petunjuk arah.")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity)ctx).finish();
                                }
                            }).show();
                }
            }
        });
        holder.goToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = v.getContext();
                Intent toDetail = new Intent(ctx, DetailActivity.class);
                toDetail.putExtra("id", rekomendasi.getId());
                toDetail.putExtra("jarak", rekomendasi.getJarak());
                ctx.startActivity(toDetail);
            }
        });
    }

    //apply badges each iterator
    private int applyBadges(int pos){
        switch (pos){
            case 0:
                return R.drawable.ic_badges_gold;
            case 1:
                return R.drawable.ic_badges_silver;
            case 2:
                return R.drawable.ic_badges_bronze;
                default: return 0;
        }
    }

    @Override
    public int getItemCount() {
        return listRekomendasi.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        public TextView resultName, resultDistance;
        public Button goToMaps, goToDetail;
        public ImageView imgBadges;

        public VHolder(View itemView) {
            super(itemView);
            resultName = itemView.findViewById(R.id.txtNamaResult);
            resultDistance = itemView.findViewById(R.id.txtJarakResult);
            imgBadges = itemView.findViewById(R.id.imgBadges);
            goToMaps = itemView.findViewById(R.id.btnToGMaps);
            goToDetail = itemView.findViewById(R.id.btnToDetail);
        }
    }
}
