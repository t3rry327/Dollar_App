package dev.t3rry327.enigma.dolarapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import dev.t3rry327.enigma.dolarapp.Util;
import dev.t3rry327.enigma.dolarapp.object.Currency;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "currencydata";
    private static final String TABLE_NAME = "historic_data";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String SELLPRICE_COL = "sellprice";
    private static final String BUYPRICE_COL = "buyprice";
    private static final String TIMESTAMP_COL = "timestamp";
    private static final int DB_VERSION = 1;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + BUYPRICE_COL + " INT,"
                + SELLPRICE_COL + " INT,"
                + TIMESTAMP_COL + " TIMESTAMP)";

        db.execSQL(query);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addNewCurrency(List<Currency> currencyList) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < currencyList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(NAME_COL, currencyList.get(i).getCurrencyName());
            values.put(BUYPRICE_COL, currencyList.get(i).getCurrencyBuyPrice());
            values.put(SELLPRICE_COL, currencyList.get(i).getCurrencySellPrice());
            values.put(TIMESTAMP_COL, Util.getCurrentTimeStamp());

            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }


    public List<Currency> getCurrencies(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        List<Currency> currencyList = new ArrayList<>();
        if (date == null) {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM 'historic_data' ORDER by timestamp LIMIT 6", null);
            if (cursor.moveToFirst()) {
                do {
                    currencyList.add(new Currency(cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)));
                } while (cursor.moveToNext());
            }
            sqLiteDatabase.close();
            return currencyList;
        } else {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM 'historic_data' WHERE timestamp = ? LIMIT 6", new String[] { date }, null);
            if (cursor.moveToFirst()) {
                do {
                    currencyList.add(new Currency(cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)));
                } while (cursor.moveToNext());
            }
            sqLiteDatabase.close();
            return currencyList;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

