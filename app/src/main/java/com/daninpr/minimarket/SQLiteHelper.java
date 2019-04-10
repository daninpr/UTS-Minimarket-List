package com.daninpr.minimarket;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String nama, String alamat, byte[] img){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO MINIMARKET VALUES (NULL, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, nama);
        statement.bindString(2, alamat);
        statement.bindBlob(3, img);

        statement.executeInsert();
    }

    public void updateData(String nama, String alamat, byte[] img, int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE MINIMARKET SET nama = ?, alamat = ?, img = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, nama);
        statement.bindString(2, alamat);
        statement.bindBlob(3, img);
        statement.bindDouble(4, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM MINIMARKET WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
