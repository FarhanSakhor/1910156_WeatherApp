package com.example.a1910156_weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String cityInput="Dhaka";
    String apiInput= "7733400e03df3db253b6c1f5060ef9c0";

    TextView cityText, conditionText, temperatureText, humidityText, pressureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cityText= findViewById(R.id.city);
        conditionText= findViewById(R.id.condition);
        temperatureText= findViewById(R.id.temperature);
        humidityText= findViewById(R.id.humidity);
        pressureText= findViewById(R.id.pressure);

        new WeatherTask().execute();
    }

    public class WeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.main).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... strings) {
            String response= HttpRequest.executeGet("https://api.openweathermap.org/data/2.5/weather?q="+cityInput+"&appid="+apiInput);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject= new JSONObject(s);
                JSONObject main= jsonObject.getJSONObject("main");
                JSONObject weather= jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject sys= jsonObject.getJSONObject("sys");

                Long updatedAt= jsonObject.getLong("dt");
                String updatedAtText= "Updated at: "+ new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt* 1000));
                String temperature= main.getString("temp")+ "K";
                String humidity= main.getString("humidity");
                String pressure= main.getString("pressure");


                Long sunrise= sys.getLong("sunrise");
                Long sunset= sys.getLong("sunset");

                String condition= weather.getString("main");

                String address = jsonObject.getString("name") + ", " + sys.getString("country");

                temperatureText.setText(temperature);
                conditionText.setText(condition);
                cityText.setText(address);
                humidityText.setText(humidity);
                pressureText.setText(pressure);

                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.main).setVisibility(View.VISIBLE);
            }catch (JSONException e)
            {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }
        }
    }
}

