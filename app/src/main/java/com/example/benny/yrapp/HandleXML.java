package com.example.benny.yrapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by benny on 09/03/2015.
 */
public class HandleXML {

    private String tempView = "tempView";
    private String weatherIcon = "weatherIcon";
    private String symbolName = "symbolName";
    private String pressView = "pressView";
    private String humView = "humView";
    private String windView = "windView";
    private String urlString = null;
    private String city = "city";
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    public HandleXML(String url){
        this.urlString = url;

    }

    // get and setters

    public String getTempView() {
        return tempView;
    }

    public void setTempView(String tempView) {
        this.tempView = tempView;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getPressView() {
        return pressView;
    }

    public void setPressView(String pressView) {
        this.pressView = pressView;
    }

    public String getHumView() {
        return humView;
    }

    public void setHumView(String humView) {
        this.humView = humView;
    }

    public String getWindView() {
        return windView;
    }

    public void setWindView(String windView) {
        this.windView = windView;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSymbolName(){return symbolName;}

    public void setSymbolName(String symbolName){this.symbolName = symbolName;}





    public void parseXMLAndStoreIt(XmlPullParser myParser){
        int event;
        String text=null;
        try{
            event = myParser.getEventType();
            // starter å parse xml
            while(event != XmlPullParser.END_DOCUMENT){
                String name=myParser.getName();
                switch(event){
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("city")){
                            city = text;
                        }
                        else if (name.equals("precipitation")){
                            humView = myParser.getAttributeValue(null, "value");
                        }

                        else if (name.equals("pressure")){
                            pressView = myParser.getAttributeValue(null, "value");
                        }
                        else if (name.equals("temperature")){
                            tempView = myParser.getAttributeValue(null, "value");
                        }

                        else if (name.equals("windSpeed")){
                            windView = myParser.getAttributeValue(null, "mps");
                        }

                        else if(name.equals("symbol")){
                            symbolName = myParser.getAttributeValue(null, "name");
                        }

                        else if (name.equals("symbol")){
                            weatherIcon = myParser.getAttributeValue(null, "number");
                        }


                        else{

                        }
                        break;
                }
                event = myParser.next();
            }

            parsingComplete = false;
        } catch (Exception e){
            e.printStackTrace();
        }
    }










    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection)
                            url.openConnection();
                    conn.setReadTimeout(10000 );
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);
                    parseXMLAndStoreIt(myparser);
                    stream.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}
