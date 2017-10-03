package net.vielmond.meupeso.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import net.vielmond.meupeso.db.DBhelper;
import net.vielmond.meupeso.entidades.MeusDados;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 29/09/17.
 */

public class LancamentosDAO implements Serializable {

    private DBhelper dbhelper;
    private final Context ourcontext;
    private SQLiteDatabase database;
    private ArrayList<MeusDados> list;

    public LancamentosDAO(Context c) {
        ourcontext = c;
    }

    public LancamentosDAO open() throws SQLException {

        dbhelper = new DBhelper(ourcontext);
        database = dbhelper.getWritableDatabase();

        return this;
    }

    public void close() {
        dbhelper.close();
    }


    public Cursor listar(MeusDados l) {
        Cursor c = null;
        try {
            c = database.rawQuery("SELECT "
            + DBhelper.LANCAMENTO_ID
            + "," + DBhelper.LANCAMENTO_PESO
            + "," + DBhelper.LANCAMENTO_TIMESTAMP
            + " FROM " + DBhelper.TABLE_LANCAMENTOS
            + " ORDER BY " + DBhelper.LANCAMENTO_ID, null);
            if (c != null) {
                c.moveToFirst();
            }
        } catch (SQLException e) {
            System.out.println("error: " + e);
            return c;
        }
        return c;
    }


    public boolean add(MeusDados l) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBhelper.LANCAMENTO_PESO, l.getPeso());
            database.insert(DBhelper.TABLE_LANCAMENTOS, null, cv);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean update(MeusDados l) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBhelper.LANCAMENTO_PESO, l.getPeso());
            database.update(DBhelper.TABLE_LANCAMENTOS, cv,
                    DBhelper.LANCAMENTO_ID + " = " + l.getId(), null);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    public boolean deletar(MeusDados l) {
        try {
            database.delete(DBhelper.TABLE_LANCAMENTOS, DBhelper.LANCAMENTO_ID + " = " + l.getId(), null);
        } catch (SQLException e) {
            System.err.println("Erro: " + e);
            return false;
        }
        return true;
    }

    public ArrayList<MeusDados> getList() {
        try {

            Cursor c = database.rawQuery("SELECT "
                    + DBhelper.LANCAMENTO_ID
                    + "," + DBhelper.LANCAMENTO_PESO
                    + "," + DBhelper.LANCAMENTO_TIMESTAMP
                    + " FROM " + DBhelper.TABLE_LANCAMENTOS
                    + " ORDER BY " + DBhelper.LANCAMENTO_ID + " ASC ", null);

            list = new ArrayList<>();
            if (c != null) {
                while (c.moveToNext()) {
                    MeusDados m = new MeusDados();
                    m.setId(c.getInt(c.getColumnIndex(DBhelper.LANCAMENTO_ID)));
                    m.setPeso(c.getFloat(c.getColumnIndex(DBhelper.LANCAMENTO_PESO)));
                    m.setStrDataHora(c.getString(c.getColumnIndex(DBhelper.LANCAMENTO_TIMESTAMP)));
                    list.add(m);
                }
            }

        }catch (SQLException e){
            System.out.println(e);
        }
        return list;
    }

    public void setList(ArrayList<MeusDados> list) {
        this.list = list;
    }

}
