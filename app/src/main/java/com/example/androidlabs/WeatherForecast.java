package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    ProgressBar progressBar;
    ImageView currImage ;
    TextView currTemperatureTextView;
    TextView minTemperatureTextView;
    TextView maxTemperatureTextView;
    TextView UVTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        currImage = findViewById(R.id.currentWeather);
        currTemperatureTextView = findViewById(R.id.currTemperature);
        minTemperatureTextView = findViewById(R.id.minTemperature);
        maxTemperatureTextView = findViewById(R.id.maxTemperature);
        UVTextView = findViewById(R.id.UV);

        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric",
                "https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");

    }


    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private double UV;
        private String min;
        private String max;
        private String currTemperature;
        private Bitmap currWeatherImage;

        @Override
        protected String doInBackground(String... args) {
            String iconName = "" ;
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");


                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            //If you get here, then you are pointing to a <temperature start tag
                            currTemperature = xpp.getAttributeValue(null,    "value");
                            publishProgress(25);
                            max = xpp.getAttributeValue(null,    "max");
                            publishProgress(50);
                            min = xpp.getAttributeValue(null,    "min");
                            publishProgress(75);

                        }

                        if(xpp.getName().equals("weather"))
                        {
                            iconName  = xpp.getAttributeValue(null,"icon");
                        }

                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
            }
            catch (Exception e)
            {
            }

            //fetch temperature
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[1]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                UV = uvReport.getDouble("value");
            }
            catch (Exception e)
            {
            }

            //fetch UV
            if(!fileExistance(iconName + ".png")){
                String urlString = "http://openweathermap.org/img/w/" + iconName + ".png";
                try{
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        currWeatherImage = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("Download",iconName + ".png");

                try{
                    FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                    currWeatherImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    publishProgress(100);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                FileInputStream fis = null;
                try {    fis = openFileInput(iconName + ".png");   }
                catch (FileNotFoundException e) {    e.printStackTrace();  }
                currWeatherImage = BitmapFactory.decodeStream(fis);
                publishProgress(100);
                Log.i("Load From File",iconName + ".png");
            }



            return "Done";
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }


        public void onProgressUpdate(Integer ... args)
        {
        super.onProgressUpdate(args);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);

        }

        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            currTemperatureTextView.append(currTemperature);
            minTemperatureTextView.append(min);
            maxTemperatureTextView.append(max);
            UVTextView.append(UV+"");
            currImage.setImageBitmap(currWeatherImage);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}