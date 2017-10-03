/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vielmond.meupeso.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 *
 * @author vielmond
 */
public class DBhelper extends SQLiteOpenHelper {

    //versao 1.0
    private Context myContext;
    private String DB_PATH = "";
    private SQLiteDatabase myDataBase;

    // DATABASE DB_NAME
    static final String DB_NAME = "dbmeupeso.db";
    static final int DB_VERSION = 1;

    // TABLE TABLE_LANCAMENTOS
    public static final String TABLE_LANCAMENTOS = "lancamentos";
    public static final String LANCAMENTO_ID = "_id";
    public static final String LANCAMENTO_PESO = "peso";
    public static final String LANCAMENTO_TIMESTAMP = "datahora_lancamento";

    public static final String TABLE_META = "metas";
    public static final String META_ID = "_id";
    public static final String META_VALOR = "valor";
    public static final String META_TIMESTAMP = "datahora_meta";

    // TABLE CREATE_METAS
    private static final String CREATE_METAS = "CREATE TABLE "
            + TABLE_META + "(" + META_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + META_VALOR + " DOUBLE,"
            + META_TIMESTAMP + " NOT NULL DEFAULT current_timestamp);";

    // TABLE CREATE_LANCAMENTOS
    private static final String CREATE_LANCAMENTOS = "CREATE TABLE "
            + TABLE_LANCAMENTOS + "(" + LANCAMENTO_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LANCAMENTO_PESO + " DOUBLE, "
            + LANCAMENTO_TIMESTAMP + " NOT NULL DEFAULT current_timestamp);";

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;

        DB_PATH = "/data/data/"
                + context.getApplicationContext().getPackageName()
                + "/databases/";
    }

    public DBhelper open() throws SQLException {
        myDataBase = getWritableDatabase();

        Log.d(TAG, "DbHelper Opening Version: " + this.myDataBase.getVersion());
        return this;
    }

    @Override
    public synchronized void close() {

        if (myDataBase != null) {
            myDataBase.close();
        }

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_METAS);
            db.execSQL(CREATE_LANCAMENTOS);
        } catch (SQLException error) {
            System.out.println(error);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            onCreate(db);
        } else {
            Log.d(TAG, "DB Atualizado!");
        }
    }

}
