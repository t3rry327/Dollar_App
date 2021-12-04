package dev.t3rry327.enigma.dolarapp.service;

import android.app.ProgressDialog;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.t3rry327.enigma.dolarapp.Util;
import dev.t3rry327.enigma.dolarapp.adapter.CurrencyAdapter;
import dev.t3rry327.enigma.dolarapp.object.Currency;
import dev.t3rry327.enigma.dolarapp.storage.DBHandler;


public class CurrencyFetcherService extends JobService {


    private static final String URL_DATA = "https://www.dolarsi.com/api/api.php?type=valoresprincipales";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onStartJob(JobParameters params) {
        Util.scheduleJob(getApplicationContext());

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fetchCurrenciesFromNetwork() {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        List<Currency> currencyList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA
                , response -> {
            try {

                JSONArray array = new JSONArray(response);

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jo = array.getJSONObject(i);
                    JSONObject x = jo.getJSONObject("casa");
                    if (!(x.getString("nombre").equals("Bitcoin") || x.getString("nombre").equals("Dolar") || x.getString("nombre").equals("Argentina"))) {
                        Currency currency = new Currency(x.getString("nombre"), x.getString("compra"),
                                x.getString("venta"));
                        currencyList.add(currency);
                    }


                }
                dbHandler.addNewCurrency(currencyList);

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }, error -> Log.i("Error",error.toString()));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


}
