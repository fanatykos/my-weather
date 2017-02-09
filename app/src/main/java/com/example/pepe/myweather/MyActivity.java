package com.example.pepe.myweather;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyActivity extends AppCompatActivity {
    Typeface weatherFont;
    TextView currentTemp;
    TextView cityField;
    TextView currentPressure;
    EditText currentCity;
    Button setCityButton;
    Handler handler;
    TextView weatherIcon;
    CityPreference prefs;
    TextView lastUpdateTime;
    Timer repeatTask = new Timer();
    WeatherIconAssembler weatherIconAssembler;
    int repeatInterval = 600000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");
        createObjects();

        try {
            updateWeatherData(URLEncoder.encode(prefs.getCity(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        repeatTask.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateWeatherData(URLEncoder.encode(prefs.getCity(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, 0, repeatInterval);

        setCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCity.getText().length() > 0) {
                    prefs.setCity(String.valueOf(currentCity.getText()));
                }
                currentCity.getText().clear();
                try {
                    updateWeatherData(URLEncoder.encode(prefs.getCity(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (repeatTask != null) {
            repeatTask.cancel();
        }
    }

    private void createObjects() {
        handler = new Handler();
        currentTemp = (TextView) findViewById(R.id.updateTemperatureField);
        currentPressure = (TextView) findViewById(R.id.updatePressure);
        currentCity = (EditText) findViewById(R.id.changeCityText);
        setCityButton = (Button) findViewById(R.id.changeCityButton);
        cityField = (TextView) findViewById(R.id.cityField);
        weatherIcon = (TextView) findViewById(R.id.weatherIcon);
        lastUpdateTime = (TextView) findViewById(R.id.updatedTime);
        weatherIcon.setTypeface(weatherFont);
        weatherIconAssembler = new WeatherIconAssembler();
        prefs = new CityPreference(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            StringBuilder author = new StringBuilder();
            author.append(getString(R.string.author));
            author.append("\n");
            author.append(getString(R.string.e_mail));
            Toast.makeText(getApplicationContext(), author.toString(),
                    Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(getApplicationContext(), city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        try {
            JSONObject main = json.getJSONObject("main");
            JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
            Log.e("myweather", json.toString());
            cityField.setText(json.getString("name") + ", " + json.getJSONObject("sys").getString("country"));
            currentTemp.setText(String.format("%.2f", main.getDouble("temp")) + " ℃");
            currentPressure.setText(main.getString("pressure") + " hPa");
            weatherIconAssembler.setWeatherIcon(weather.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000, weatherIcon);
            DateFormat df = DateFormat.getDateTimeInstance();
            lastUpdateTime.setText(df.format(new Date(json.getLong("dt") * 1000)));
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

}
