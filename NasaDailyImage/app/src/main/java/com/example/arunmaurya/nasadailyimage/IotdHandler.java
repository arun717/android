package com.example.arunmaurya.nasadailyimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by Arun Maurya on 3/25/2018.
 */

public class IotdHandler extends DefaultHandler {

    private int title_count=0;
    private int date_count=0;
    private int image_count=0;
    private int description_count=0;
    private String url = "https://www.nasa.gov/rss/dyn/image_of_the_day.rss";
    private boolean inUrl = false;
    private boolean inTitle = false;
    private boolean inDescription = false;
    private boolean inItem = false;
    private boolean inDate = false;
    private Bitmap image = null;
    private boolean endparsing = false;
    // to capture image url
    private String imageUrl = null;

    private String title = null;
    private StringBuffer description = new StringBuffer();
    private String date = null;


    public void processFeed()
    {
        try{
            //added these two lines

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                reader.setContentHandler(this);
                //made this URL object
                URL urlObj = new URL(url);
                //InputStream inputStream = new URL(url).openStream();
                InputStream inputStream = urlObj.openConnection().getInputStream();
                //System.out.println("check this out man :"+inputStream);
                InputSource a = new InputSource(inputStream);
                //System.out.println("char stream data: "+a.getCharacterStream());
                //Log.d(TAG,inputStream.toString());
                reader.parse(a);
                //reader.parse(new InputSource(inputStream));
                //reader.parse(new InputSource(urlObj.openStream()));


        }
        catch (Exception e){
            System.out.println(new String("Exception GENERAL in processFeed"));
            e.printStackTrace();
        }
    }
    private Bitmap getBitmap(String url){

        try {

                //System.out.println("Code inside getBitmap function");
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                //System.out.println("input set in bitmap funct is ="+input.available());
                //BitmapFactory.Options options = new BitmapFactory.Options();
                BufferedInputStream bis = new BufferedInputStream(input, 8192);
                //System.out.println("bis value is "+bis);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                //System.out.println("image set in bitmap funct is ="+bitmap);
                input.close();
                return bitmap;
        }
        catch (IOException ioe){
            System.out.println(new String("IOException GENERAL in bitmap ioe"));
            ioe.printStackTrace();
            return null;
        }
        catch (Exception e){
            System.out.println(new String("Exception has occurred GENERAL in bitmap"));
            e.printStackTrace();
            return null;
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes)throws SAXException{
        try {
            if(endparsing)
                throw new MySAXTerminatorException();

            if (localName.startsWith("item")) {
                inItem = true;
                endparsing = true;
            }
            else if (inItem) {
                if (localName.equals("title"))
                    inTitle = true;
                else
                    inTitle = false;

                if (localName.equals("description"))
                    inDescription = true;
                else
                    inDescription = false;

                if (localName.equals("pubDate"))
                    inDate = true;
                else
                    inDate = false;

                if (localName.equals("enclosure")) {
                    //System.out.println(new String("inside startElement method for image"));
                    imageUrl = attributes.getValue("", "url");
                    //System.out.println(imageUrl);
                    inUrl = true;
                } else
                    inUrl = false;
            }

        }
        catch (Exception e)
        {
            System.out.println("Exception caught: \n stopping parser : ");
            e.printStackTrace();
        }

    }

    public void characters(char ch[], int start, int length){
        String chars = new String(ch).substring(start,start+length);
        System.out.println(chars);
        if(image_count==0) {
            if (inUrl && image == null) {

                //image = getBitmap("https://www.nasa.gov/sites/default/files/thumbnails/image/41864007272_5a80d2dc33_o.jpg");
//                if(imageUrl.equals("https://www.nasa.gov/sites/default/files/thumbnails/image/41864007272_5a80d2dc33_o.jpg"))
//                    System.out.println("same strings");
//                else {
//                    System.out.println("surprise surprise mf");
//                    System.out.println("the original is : "+"https://www.nasa.gov/sites/default/files/thumbnails/image/41864007272_5a80d2dc33_o.jpg");
//                    System.out.println("\nyours is : "+ imageUrl);
//                }
                imageUrl = imageUrl.replace("http","https");
                //System.out.println("final imageurl is :"+imageUrl);
                image = getBitmap(imageUrl);
                //System.out.println("after calling bitmap function");
                image_count++;
                //System.out.println("Image check ===>"+image);
            }
        }
        if(title_count==0) {
            if (inTitle && title == null) {
                title = chars;
                title_count++;
            }
        }
        if(description_count==0) {
            if (inDescription) {
                description.append(chars);
                description_count++;
            }
        }
        if(date_count==0) {
            if (inDate && date == null) {
                date = chars;
                date_count++;
            }
        }
    }



    public Bitmap getImage(){return image;}
    public String getTitle(){return title;}
    public StringBuffer getDescription() {return description;}
    public String getDate(){ return date;}

}
