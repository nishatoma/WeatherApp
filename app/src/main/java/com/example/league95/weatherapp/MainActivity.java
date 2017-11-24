package com.example.league95.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    //Button
    Button button;
    //Edit Text
    EditText editText;
    //Text View
    TextView textView;

    //find weather
    public void findWeather(View view)
    {



        try {
            //We need to remove keyboard on phone screen when we tap it!
            InputMethodManager methodManager = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);
            String url;
            if (editText.getText().toString() != "" && !editText.getText().toString().isEmpty() && editText.getText().toString().length() > 1) {
                url = "http://api.openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=b70a80a636028a627853571d49b8465b";
                DownloadTask downloadTask = new DownloadTask();
                String encodedCityName;
                encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
                downloadTask.execute(url);
            } else
            {
                Toast.makeText(getApplicationContext(), "Enter a valid city name!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Enter a valid city name!", Toast.LENGTH_SHORT).show();
        }
    }

    //Donwload Task
    public class DownloadTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {

            StringBuilder sb = new StringBuilder();
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                char dataContent;
                while (data != -1)
                {
                    dataContent = (char) data;
                    sb.append(dataContent);
                    data = inputStreamReader.read();
                }

                return sb.toString();

            } catch (MalformedURLException e)
            {
                Toast.makeText(getApplicationContext(), "Enter a valid city name!", Toast.LENGTH_SHORT).show();
            } catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "Enter a valid city name!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        // now post execute


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Start parsing JSON
            //We can use this method to display answer to the screen
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherContent = jsonObject.getString("weather");
                Log.i("Weather content", weatherContent);

                JSONArray jsonArray = new JSONArray(weatherContent);


                //Result String
                String resultView = "";
                for (int i = 0; i < jsonArray.length(); i++)
                {

                    JSONObject miniJson = jsonArray.getJSONObject(i);
                    String main;
                    String desc;

                    main = miniJson.getString("main");
                    desc = miniJson.getString("description");


                    if (main != "" && desc != "")
                    {
                        resultView += main + ": " + desc + "\r\n";

                    }
                }
                if (resultView != "" || !editText.getText().toString().isEmpty())
                {
                    textView.setText(resultView.toString());
                } else
                {
                    Toast.makeText(getApplicationContext(), "Enter a valid city name!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Enter a valid city name!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find weather button
        button = findViewById(R.id.button);
        //Our text field
        editText = (EditText) findViewById(R.id.cityName);
        //Text view
        textView = findViewById(R.id.answerView);

    }





}
