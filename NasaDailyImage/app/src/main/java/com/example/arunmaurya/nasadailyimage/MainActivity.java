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
        final IotdHandler2 handler = new IotdHandler2();
        //new myTask().execute();
        Thread t1 = new Thread(new Runnable()
        {
            public void run() {

                try {

                    handler.processFeed();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        IotdHandler2 handler = new IotdHandler2();
        @Override
        protected Void doInBackground(Void... params){
            try {
                handler.processFeed();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            resetDisplay(handler.getTitle(),handler.getDate(),handler.getImage(),handler.getDescription());
            super.onPostExecute(result);
        }

    }
}
