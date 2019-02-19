package com.example.arunmaurya.nasadailyimage;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IotdHandler handler = new IotdHandler();
        //new myTask().execute();
        handler.processFeed();
        resetDisplay(handler.getTitle(), handler.getDate(), handler.getImage(), handler.getDescription());
    }
    public void resetDisplay(String title, String date, Bitmap image, StringBuffer description)
    {
        TextView titleView = (TextView) findViewById(R.id.imageTitle);
        titleView.setText(title);

        TextView dateView = (TextView)findViewById(R.id.imageDate);
        dateView.setText(date);

        ImageView imageView = (ImageView)findViewById(R.id.imageDisplay);
        imageView.setImageBitmap(image);

        TextView descriptionView = (TextView)findViewById(R.id.imageDescription);
        descriptionView.setText(description);

    }


    private class myTask extends AsyncTask <Void,Void,Void>{
        IotdHandler handler = new IotdHandler();
        @Override
        protected Void doInBackground(Void... params){
            handler.processFeed();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            resetDisplay(handler.getTitle(),handler.getDate(),handler.getImage(),handler.getDescription());
            super.onPostExecute(result);
        }

    }
}
