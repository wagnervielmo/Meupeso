package net.vielmond.meupeso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.vielmond.meupeso.dao.GraficosThread;
import net.vielmond.meupeso.dao.LancamentosDAO;
import net.vielmond.meupeso.dao.LancamentosThread;
import net.vielmond.meupeso.entidades.MeusDados;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private AlertDialog d = null;
    private LancamentosDAO db;
    private static SimpleDateFormat formatDt = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chamarAsyncTask();
        listaAsyncTask();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Adicionar");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                LayoutInflater inflateredit = getLayoutInflater();
                builder.setView(inflateredit.inflate(R.layout.add_peso, null))
                        .setPositiveButton(R.string.txt_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                MeusDados m = new MeusDados();
                                m.setStrDataHora(((TextView) d.findViewById(R.id.datahora_id)).getText().toString());
                                m.setPeso(Float.valueOf(((TextView) d.findViewById(R.id.peso_id)).getText().toString()));
                                    if (add(m)) {
                                        chamarAsyncTask();
                                        listaAsyncTask();
                                    } else {
                                        db.close();
                                    }
                                }

                        })
                        .setNegativeButton(R.string.txt_cancelar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                System.out.println(id);
                            }

                        });

                d = builder.show();

                ((TextView) d.findViewById(R.id.datahora_id)).setText(formatDt.format(new Date()));

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.atualizar_id) {
            chamarAsyncTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private boolean add(MeusDados l){
        try {
            db = new LancamentosDAO(this);
            db.open();
            if (db.add(l)) {
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }

        } catch (SQLException e){
            System.out.println("Erro: " + e);
            db.close();
            return false;
        }
    }

    public void chamarAsyncTask() {
        GraficosThread carregaDados = new GraficosThread(MainActivity.this, MainActivity.this);
        Log.i("AsyncTask", "AsyncTask senado chamado Thread: " + Thread.currentThread().getName());
        carregaDados.execute();
    }


    public void listaAsyncTask() {
        LancamentosThread carregaDados = new LancamentosThread(MainActivity.this, MainActivity.this);
        Log.i("AsyncTask", "AsyncTask senado chamado Thread: " + Thread.currentThread().getName());
        carregaDados.execute();
    }

}
