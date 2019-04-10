package com.daninpr.minimarket;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MinimarketList extends AppCompatActivity {
    GridView gridView;
    ArrayList<Minimarket> list;
    MinimarketListAdapter adapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimarket_list_activity);

        gridView = findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new MinimarketListAdapter(this, R.layout.minimarket_items, list);
        gridView.setAdapter(adapter);

        //mengambil data dr sqlite
        Cursor cursor = Minimarket_MainActivity.sqLiteHelper.getData("SELECT * FROM MINIMARKET");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            String alamat = cursor.getString(2);
            byte[] img = cursor.getBlob(3);

            list.add(new Minimarket(id, nama, alamat, img));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Edit", "Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MinimarketList.this);

                dialog.setTitle("Pilih");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {

                            Cursor c = Minimarket_MainActivity.sqLiteHelper.getData("SELECT id FROM MINIMARKET");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogUpdate(MinimarketList.this, arrID.get(position));

                        } else {
                            Cursor c = Minimarket_MainActivity.sqLiteHelper.getData("SELECT id FROM MINIMARKET");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(MinimarketList.this, Detail_MainActivity.class);
                TextView nama = view.findViewById(R.id.txtNama);
                TextView alamat = view.findViewById(R.id.txtAlamat);
                intent.putExtra("message_nama", nama.getText().toString());
                intent.putExtra("message_alamat", alamat.getText().toString());

                ImageView gambar = view.findViewById(R.id.imgMarket);
                Drawable drawable=gambar.getDrawable();
                Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream arry = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, arry);
                byte[] gb = arry.toByteArray();
                intent.putExtra("image", gb);
                startActivity(intent);
            }
        });
    }

    ImageView imageViewMarket;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.edit_minimarket_activity);
        dialog.setTitle("Update");

        imageViewMarket = dialog.findViewById(R.id.imageViewMarket);
        final EditText edtNama =dialog.findViewById(R.id.edtNama);
        final EditText edtAlamat =  dialog.findViewById(R.id.edtAlamat);
        Button btnUpdate =dialog.findViewById(R.id.btnUpdate);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        MinimarketList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Minimarket_MainActivity.sqLiteHelper.updateData(
                            edtNama.getText().toString().trim(),
                            edtAlamat.getText().toString().trim(),
                            Minimarket_MainActivity.imageViewToByte(imageViewMarket),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update Berhasil!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateMinimarketList();
            }
        });
    }

    private void showDialogDelete(final int idMarket){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MinimarketList.this);

        dialogDelete.setTitle("Perhatian!!");
        dialogDelete.setMessage("Apakah kamu ingin menghapus data ini?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Minimarket_MainActivity.sqLiteHelper.deleteData(idMarket);
                    Toast.makeText(getApplicationContext(), "Hapus Berhasil!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateMinimarketList();
            }
        });

        dialogDelete.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateMinimarketList(){
        Cursor cursor = Minimarket_MainActivity.sqLiteHelper.getData("SELECT * FROM MINIMARKET");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            String alamat = cursor.getString(2);
            byte[] img = cursor.getBlob(3);

            list.add(new Minimarket(id, nama, alamat,img));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewMarket.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
