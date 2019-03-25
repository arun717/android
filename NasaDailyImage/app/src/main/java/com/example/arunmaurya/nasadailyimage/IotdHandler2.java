package com.example.arunmaurya.nasadailyimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.net.URI;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 * Created by Arun Maurya on 3/24/2019.
 */

public class IotdHandler2 {

    private Bitmap image = null;
    private String imageUrl = null;
    private String title = null;
    private StringBuffer description = new StringBuffer();
    private String date = null;
    private String url = "https://www.nasa.gov/rss/dyn/image_of_the_day.rss";

    public void processFeed()throws Exception {



                    try {
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document doc = db.parse(new URL(url).openStream());

                        Element docEle = doc.getDocumentElement();
                        NodeList itemList = docEle.getElementsByTagName("item");
                        int count = 0;
                        while (count == 0) {
                            Node node = itemList.item(0);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {

                                System.out
                                        .println("=====================");
                                Element e = (Element) node;
                                NodeList nodeList = e.getElementsByTagName("title");
                                title = nodeList.item(0).getChildNodes().item(0)
                                        .getNodeValue();
                                System.out.println("Title: "
                                        + title);

                                nodeList = e.getElementsByTagName("description");
                                description = new StringBuffer(nodeList.item(0).getChildNodes().item(0)
                                        .getNodeValue());
                                System.out.println("Description: "
                                        + description);

                                nodeList = e.getElementsByTagName("pubDate");
                                date = nodeList.item(0).getChildNodes().item(0)
                                        .getNodeValue();
                                System.out.println("pubDate: "
                                        + date);

                                nodeList = e.getElementsByTagName("enclosure");
                                imageUrl = nodeList.item(0).getAttributes().getNamedItem("url").getNodeValue();
                                System.out.println("image url: "
                                        + imageUrl);

                                imageUrl = imageUrl.replace("http", "https");
                                System.out.println("final imageurl is :" + imageUrl);
                                image = getBitmap(imageUrl);


                                count++;
                            }
                        }


                    } catch (Exception e) {
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


    public Bitmap getImage(){return image;}
    public String getTitle(){return title;}
    public StringBuffer getDescription() {return description;}
    public String getDate(){ return date;}

}
