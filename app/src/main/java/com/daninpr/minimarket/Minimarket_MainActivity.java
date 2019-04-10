package com.daninpr.minimarket;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Minimarket_MainActivity extends AppCompatActivity {
    EditText edtNama, edtAlamat;
    Button btnChoose, btnAdd;
    ImageView imageView;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimarket_main);
        init();

        sqLiteHelper = new SQLiteHelper(this, "MiniMarketDatabase.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS MINIMARKET(Id INTEGER PRIMARY KEY AUTOINCREMENT, nama VARCHAR, alamat VARCHAR, img BLOB)");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        Minimarket_MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    sqLiteHelper.insertData(
                            edtNama.getText().toString().trim(),
                            edtAlamat.getText().toString().trim(),
                            imageViewToByte(imageView)
                    );
                    Toast.makeText(getApplicationContext(), "Berhasil Menambahkan Data!", Toast.LENGTH_SHORT).show();
                    edtNama.setText("");
                    edtAlamat.setText("");
                    imageView.setImageResource(R.drawable.gb);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    public static byte[] imageViewToByte(ImageView img){
        Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);


        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        edtNama = findViewById(R.id.edtNama);
        edtAlamat = findViewById(R.id.edtAlamat);
        btnChoose = findViewById(R.id.btnChoose);
        btnAdd = findViewById(R.id.btnAdd);
        imageView = findViewById(R.id.imageView);
    }
}
