package com.daninpr.minimarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;

public class Detail_MainActivity extends AppCompatActivity {
    String nama, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_main);

        TextView text_nama = findViewById(R.id.tv_nama);
        TextView text_alamat = findViewById(R.id.tv_alamat);
        ImageView image = findViewById(R.id.hasil_gambar);

        Bundle extras = getIntent().getExtras();
        byte[] gb = extras.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(gb, 0, gb.length);
        image.setImageBitmap(bmp);

        Intent intent = getIntent();
        nama = intent.getStringExtra("message_nama");
        alamat = intent.getStringExtra("message_alamat");

        text_nama.setText(String.format("Nama       : " + nama));
        text_alamat.setText(String.format("Alamat     : " + alamat));
        Linkify.addLinks(text_alamat, Linkify.WEB_URLS);
    }
}
