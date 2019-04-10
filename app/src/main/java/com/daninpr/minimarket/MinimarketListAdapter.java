package com.daninpr.minimarket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MinimarketListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Minimarket> minimarkets;

    public MinimarketListAdapter(Context context, int layout, ArrayList<Minimarket> minimarkets) {
        this.context = context;
        this.layout = layout;
        this.minimarkets = minimarkets;
    }

    @Override
    public int getCount() {
        return minimarkets.size();
    }

    @Override
    public Object getItem(int position) {
        return minimarkets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtNama, txtAlamat;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtNama = row.findViewById(R.id.txtNama);
            holder.txtAlamat = row.findViewById(R.id.txtAlamat);
            holder.imageView = row.findViewById(R.id.imgMarket);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Minimarket minimarket = minimarkets.get(position);

        holder.txtNama.setText(minimarket.getNama());
        holder.txtAlamat.setText(minimarket.getAlamat());

        byte[] marketImage = minimarket.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(marketImage, 0, marketImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;


    }
}
