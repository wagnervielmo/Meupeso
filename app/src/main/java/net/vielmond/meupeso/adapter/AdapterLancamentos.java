package net.vielmond.meupeso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.vielmond.meupeso.R;
import net.vielmond.meupeso.entidades.MeusDados;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by root on 02/10/17.
 */

public class AdapterLancamentos extends BaseAdapter {

    private Context context;
    private ArrayList<MeusDados> list;
    private Locale currentLocale;
    SimpleDateFormat formatdata = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public AdapterLancamentos(Context context, ArrayList<MeusDados> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MeusDados ent = list.get(position);
        View layout;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.lista_lancamentos, null);
        } else {
            layout = convertView;
        }

        ((TextView) layout.findViewById(R.id.descricao_lancamento_id)).setText(ent.getStrDataHora());
        ((TextView) layout.findViewById(R.id.peso_lancamento_id)).setText(String.valueOf(ent.getPeso())+" kg");

        return layout;
    }

    public String Converter(String mes) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM", new Locale("pt", "BR"));
            Date mesDate = sdf.parse(mes);
            sdf.applyPattern("MMMM");
            return sdf.format(mesDate).toUpperCase().substring(0, 3);
        } catch (ParseException e) {
            System.out.println("Contracheque - Error: " + e);
            return "";
        }
    }
}
