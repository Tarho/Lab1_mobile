package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.R; // Make sure to import R from your package

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageView imageView;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextText);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        constraintLayout = findViewById(R.id.constraintLayout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                new SentimentAnalysisTask().execute(text);
            }
        });
    }

    private class SentimentAnalysisTask extends AsyncTask<String, Void, JSONObject> {

        private static final String API_KEY = "QMzsk8smVZvvg5AKOuLqGm9RBTsfkPBS";
        private static final String API_URL = "https://api.apilayer.com/sentiment/analysis";

        @Override
        protected JSONObject doInBackground(String... strings) {
            String text = strings[0];
            JSONObject sentimentResult = null;

            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("apikey", API_KEY);
                connection.setDoOutput(true);

                String requestBody = "{\"text\": \"" + text + "\"}";

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    sentimentResult = new JSONObject(response.toString());
                }

                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return sentimentResult;
        }

        @Override
        protected void onPostExecute(JSONObject sentimentResult) {
            if (sentimentResult != null) {
                try {
                    String sentiment = sentimentResult.getString("sentiment");
                    double score = sentimentResult.getDouble("score");

                    if (sentiment.equals("positive")) {
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.green));
                        imageView.setImageResource(R.drawable.happy_icon);
                    } else if (sentiment.equals("negative")) {
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.red));
                        imageView.setImageResource(R.drawable.sad_icon);
                    } else {
                        // Neutral sentiment or unsupported sentiment types
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));
                        imageView.setImageResource(R.drawable.neutral_icon);
                    }

                    Log.d("SentimentScore", "Sentiment: " + sentiment + ", Score: " + score);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
