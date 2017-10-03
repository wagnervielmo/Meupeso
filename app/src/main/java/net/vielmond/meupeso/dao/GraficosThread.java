package net.vielmond.meupeso.dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import net.vielmond.meupeso.R;
import net.vielmond.meupeso.entidades.MeusDados;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 05/07/17.
 */

public class GraficosThread extends AsyncTask<String, Void, ArrayList> {

    private Context context;
    private Activity activity;
    private Integer flagException = 0;
    private ProgressDialog load;
    private ArrayList<MeusDados> lista = new ArrayList<>();
    private LancamentosDAO dbl;
    private LineChart mChart;

    public GraficosThread(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        Log.i("AsyncTask", "Exibindo ProgressDialog na tela Thread: " + Thread.currentThread().getName());
        load = ProgressDialog.show(context, null,
                "Carregando dados ...");

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

                mChart = (LineChart) activity.findViewById(R.id.chart1);
                setupChart(mChart, getData(), mColors[2]);

            } catch (Exception ex) {
                //
            }
        } else {
            if (flagException == 1) {
                Log.i("AsyncTask", "Erro: " + Thread.currentThread().getName());
                load.dismiss();
                MessageBox("IOException - Entre em contato com o desenvolvedor");
            } else if (flagException == 2) {
                Log.i("AsyncTask", "Erro: " + Thread.currentThread().getName());
                load.dismiss();
                MessageBox("NullPointerException - Entre em contato com o desenvolvedor");
            } else if (flagException == 3) {
                Log.i("AsyncTask", "Erro: " + Thread.currentThread().getName());
                load.dismiss();
                MessageBox("JSONException - Verifique sua internet.");
            } else {
                load.dismiss();
                MessageBox("Login Incorreto - Confira os dados digitados");
            }
        }

        Log.i("AsyncTask", "Tirando ProgressDialog da tela Thread: " + Thread.currentThread().getName());
        load.dismiss();

    }

    public void MessageBox(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.i("LOG MESSAGE", "Mostrou a mensagem");
    }


    private void setData(ArrayList<MeusDados> values) {

        ArrayList<Entry> vl = new ArrayList<Entry>();
        for(MeusDados fin: values){
            Double v = Double.valueOf(fin.getPeso());
            vl.add(new Entry(fin.getId(), v.floatValue(), fin.getPeso()));
            //values.add(new Entry(i, val, activity.getResources().getDrawable(R.mipmap.star)));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(vl);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(vl, "DataSet 1");

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(2f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }

    private void setupChart(LineChart chart, LineData data, int color) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleColorHole(color);

        // no description text
        chart.getDescription().setEnabled(false);

        // mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(false);

        // animate calls invalidate()...
        chart.animateX(2500);
    }

    private LineData getData() {

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for(MeusDados fin: lista) {
            Double v = Double.valueOf(fin.getPeso());
            yVals.add(new Entry(fin.getId(), v.floatValue()));
        }
        //for (int i = 0; i < count; i++) {
            //float val = (float) (Math.random() * range) + 3;
          //  yVals.add(new Entry(i, val));
        //}

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);

        // create a data object with the datasets
        LineData data = new LineData(set1);

        return data;
    }

    private int[] mColors = new int[] {
            Color.rgb(200, 200, 200),
            Color.rgb(240, 240, 30),
            Color.rgb(89, 199, 250),
            Color.rgb(250, 104, 104)
    };

}
