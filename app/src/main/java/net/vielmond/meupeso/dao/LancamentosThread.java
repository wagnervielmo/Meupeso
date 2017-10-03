package net.vielmond.meupeso.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import net.vielmond.meupeso.R;
import net.vielmond.meupeso.adapter.AdapterLancamentos;
import net.vielmond.meupeso.entidades.MeusDados;
import java.util.ArrayList;

/**
 * Created by root on 02/10/17.
 */

public class LancamentosThread extends AsyncTask<String, Void, ArrayList> {

    private Context context;
    private Activity activity;
    private Integer flagException = 0;
    private ProgressDialog load;
    private ArrayList<MeusDados> lista = new ArrayList<>();
    private LancamentosDAO dbl;
    private ListView listView;

    public LancamentosThread(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        Log.i("AsyncTask", "Exibindo ProgressDialog na tela Thread: " + Thread.currentThread().getName());
        load = ProgressDialog.show(context, null,
                "Carregando lista ...");

    }

    @Override
    protected ArrayList doInBackground(String... strings) {

        try {
            dbl = new LancamentosDAO(activity);
            dbl.open();
            lista = dbl.getList();
            dbl.close();
        } catch (Exception ex) {
            System.err.println("IOException: " + ex);
            flagException = 1;
        }

        return lista;
    }

    @Override
    protected void onPostExecute(ArrayList array) {
        if (array != null) {
            Log.i("AsyncTask", "Executa o carregamento de dados: " + Thread.currentThread().getName());
            try {

                listView = (ListView) activity.findViewById(R.id.listview_lancamentos_id);
                listView.setAdapter(new AdapterLancamentos(activity, array));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        final MeusDados itemValue = (MeusDados) listView.getItemAtPosition(position);

                        CharSequence opt[] = new CharSequence[]{"Marcar", "Valor", "Deletar"};

                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Opções");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setItems(opt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:

                                        break;
                                    case 1:
                                        break;
                                    default:

                                }

                            }
                        });

                        builder.setNegativeButton(R.string.txt_cancelar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Snackbar.make(view, "Cancelado.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });



            } catch (Exception ex) {
                //
            }
        } else {
            if(flagException == 1){
                Log.i("AsyncTask", "Erro: " + Thread.currentThread().getName());
                load.dismiss();
                messageBox("IOException - Entre em contato com o desenvolvedor");
            } else if(flagException == 2){
                Log.i("AsyncTask", "Erro: " + Thread.currentThread().getName());
                load.dismiss();
                messageBox("NullPointerException - Entre em contato com o desenvolvedor");
            } else if(flagException == 3){
                Log.i("AsyncTask", "Erro: " + Thread.currentThread().getName());
                load.dismiss();
                messageBox("JSONException - Verifique sua internet.");
            } else {
                load.dismiss();
                messageBox("Login Incorreto - Confira os dados digitados");
            }
        }

        Log.i("AsyncTask", "Tirando ProgressDialog da tela Thread: " + Thread.currentThread().getName());
        load.dismiss();

    }

    public void messageBox(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.i("LOG MESSAGE", "Mostrou a mensagem");
    }

}
