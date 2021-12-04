package dev.t3rry327.enigma.dolarapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.t3rry327.enigma.dolarapp.R;
import dev.t3rry327.enigma.dolarapp.object.Currency;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    private List<Currency> currencyList;

    public CurrencyAdapter(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }


    @NonNull
    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dollar_box_blue, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.ViewHolder holder, int position) {
        holder.dollarName.setText(currencyList.get(position).getCurrencyName());
        holder.dollarBuyPrice.setText(currencyList.get(position).getCurrencyBuyPrice());
        holder.dollarSellPrice.setText(currencyList.get(position).getCurrencySellPrice());
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dollarName;
        private TextView dollarBuyPrice;
        private TextView dollarSellPrice;

        public ViewHolder(View v) {
            super(v);
            dollarName = (TextView) v.findViewById(R.id.dollar_type);
            dollarBuyPrice = (TextView) v.findViewById(R.id.dollar_price_buy);
            dollarSellPrice = (TextView) v.findViewById(R.id.dollar_price_sell);
        }
    }
}
