package com.example.thoitietapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androdocs.httprequest.HttpRequest;
import com.example.thoitietapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String CITY = "Ha Noi, VN";
    private final String API = "ddd63f3406c328817c023643a2b51b50";
    private TextView tvAddress;
    private TextView tvUpdateAt;
    private TextView tvStatus;
    private TextView tvTemp;
    private TextView tvTempMin;
    private TextView tvTempMax;
    private TextView tvSunrise;
    private TextView tvSunset;
    private TextView tvWind;
    private TextView tvPressure;
    private TextView tvHumidity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        Task task = new Task();
        task.execute();
    }

    public final void initWidgets() {
        this.tvAddress = findViewById(R.id.address);
        this.tvUpdateAt = this.findViewById(R.id.updated_at);
        this.tvStatus = this.findViewById(R.id.status);
        this.tvTemp = this.findViewById(R.id.temp);
        this.tvTempMin = this.findViewById(R.id.temp_min);
        this.tvTempMax = this.findViewById(R.id.temp_max);
        this.tvSunrise = this.findViewById(R.id.sunrise);
        this.tvSunset = this.findViewById(R.id.sunset);
        this.tvWind = this.findViewById(R.id.wind);
        this.tvPressure = this.findViewById(R.id.pressure);
        this.tvHumidity = this.findViewById(R.id.humidity);
    }

    public class Task extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            findViewById(R.id.loader).setVisibility(View.INVISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpRequest.excuteGet(
                    "https://api.openweathermap.org/data/2.5/weather?q="
                            + MainActivity.this.CITY + "&units=metric&lang=vi&appid="
                            + MainActivity.this.API);
        }

        @Override
        protected void onPostExecute(String data) {
            try {
                JSONObject js = new JSONObject(data);
                JSONObject main = js.getJSONObject("main");
                JSONObject system = js.getJSONObject("sys");
                JSONObject wind = js.getJSONObject("wind");
                JSONObject weather = js.getJSONArray("weather").getJSONObject(0);
                long updatedAt = js.getLong("dt");
                String updatedAtTxt = "Cập nhật: " + new SimpleDateFormat(
                        "dd/MM/yyyy hh:mm a", Locale.ENGLISH)
                        .format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Thấp nhất: " + main.getString("temp_min") + "°C";
                String tempMax = "Cao nhất: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");
                long sunrise = system.getLong("sunrise");
                long sunset = system.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");
                String firstChar = "" + weatherDescription.charAt(0);
                String address = js.getString("name") + ", "
                        + system.getString("country");

                tvAddress.setText(address);
                tvUpdateAt.setText(updatedAtTxt);
                tvStatus.setText(weatherDescription.replace(weatherDescription.charAt(0),
                        firstChar.toUpperCase().charAt(0)));
                tvTemp.setText(temp);
                tvTempMin.setText(tempMin);
                tvTempMax.setText(tempMax);
                tvSunrise.setText((new SimpleDateFormat("hh:mm",
                        Locale.ENGLISH).format(new Date(sunrise * 1000))) + " AM");
                tvSunset.setText((new SimpleDateFormat("hh:mm",
                        Locale.ENGLISH).format(new Date(sunset * 1000))) + " PM");
                tvWind.setText(windSpeed);
                tvPressure.setText(pressure);
                tvHumidity.setText(humidity + " %");
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }
        }
    }
}