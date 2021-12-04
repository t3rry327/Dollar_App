package dev.t3rry327.enigma.dolarapp.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dev.t3rry327.enigma.dolarapp.R;
import dev.t3rry327.enigma.dolarapp.Util;
import dev.t3rry327.enigma.dolarapp.adapter.CurrencyAdapter;
import dev.t3rry327.enigma.dolarapp.databinding.AllDollarFragmentBinding;
import dev.t3rry327.enigma.dolarapp.object.Currency;
import dev.t3rry327.enigma.dolarapp.storage.DBHandler;

public class AllDollarFragment extends Fragment {

    private int flag = 0;
    private AllDollarFragmentBinding binding;
    private List<Currency> currencyList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static final String URL_DATA = "https://www.dolarsi.com/api/api.php?type=valoresprincipales";
    private DBHandler dbHandler;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState

    ) {
        binding = AllDollarFragmentBinding.inflate(inflater, container, false);
        currencyList = new ArrayList<>();
        dbHandler = new DBHandler(getContext());
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialising the calendar
        final Calendar calendar = Calendar.getInstance();

        // initialising the layout
        TextView textView = getView().findViewById(R.id.datetext);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);


        recyclerView = getView().findViewById(R.id.all_dollar_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        if (Util.internetConnection(getContext())) {
            fetchCurrenciesFromNetwork();
        } else {
            fetchCurrenciesFromSQLite(null);
        }

        Button button = view.findViewById(R.id.datebutton);
        button.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(getContext(), (view1, year1, month1, dayOfMonth) -> {
                textView.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.set(year1, month1, dayOfMonth);
                fetchCurrenciesFromSQLite(dateFormat.format(c.getTime()));
            }, year, month, day);
            datePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePicker.show();
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fetchCurrenciesFromSQLite(String date) {
        currencyList.clear();
        currencyList = dbHandler.getCurrencies(date);
        if (flag == 0) {
            adapter.notifyDataSetChanged();
            flag++;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fetchCurrenciesFromNetwork() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA
                , response -> {
            progressDialog.dismiss();
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
                adapter = new CurrencyAdapter(currencyList);
                recyclerView.setAdapter(adapter);
                dbHandler.addNewCurrency(currencyList);

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }, error -> Toast.makeText(getContext(), "Error" + error.toString(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}