package com.example.benny.yrapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity {



  static final String url1 = "http://www.yr.no/sted/Norge";
  private String url2 = "/varsel.xml";
  private String url3 = "/Hordaland/Bergen/";
  private HandleXML obj;
  private City city;



    private ListView cityListView;
    private Toolbar toolbar;
   private String currentCity;

    //Widgets
    private TextView tempView;
    private ImageView weatherIcon;
    private TextView pressView;
    private TextView humView;
    private TextView windView;
    private EditText byen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tempView = (TextView) findViewById(R.id.temp);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        pressView = (TextView) findViewById(R.id.pressure);
        humView = (TextView) findViewById(R.id.hum);
        windView = (TextView) findViewById(R.id.wind);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Dialog d = createDialog();
            d.show();
        }

        return super.onOptionsItemSelected(item);
    }



    // lager dialogboksen før bysøket
    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.select_city_dialog, null);
        builder.setView(v);

        final EditText et = (EditText) v.findViewById(R.id.ptnEdit);
        cityListView = (ListView) v.findViewById(R.id.cityList);
        cityListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       //  currentCity = (City) parent.getItemAtPosition(position);

            }
        });



        /*
        // setter en textchangedlistener på bysøket
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 3) {
                    // Bysøket starter å søke gjennom kjente steder i listen
                    weatherclient.searchCity(s.toString(), new WeatherClient.CityEventListener() {

                        //Konstruerer adapteren
                        @Override
                        public void onCityListRetrieved(List<City> cities) {
                            CityAdapter ca = new CityAdapter(this, cities);
                            cityListView.setAdapter(ca);

                        }




                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        */






        //lager dialog med accept knapp
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Oppdaterer tollbaren


                // Starter XMLparseren
                String url = et.getText().toString();
                String finalUrl = url1 + url3+ url + url2;
                //et.setText(finalUrl);
                obj = new HandleXML(finalUrl);
                obj.fetchXML();
                while(obj.parsingComplete);
                // country.setText(obj.getCountry());
                tempView.setText(obj.getTempView());
                // precipitation.setText(obj.getHumidity());
                pressView.setText(obj.getPressView());
                windView.setText(obj.getWindView());


                // ikon under temperaturmåleren
                getWeatherDrawable(Float.parseFloat(obj.getWeatherIcon()));
                //kjører setToolbarColor
                setToolbarColor(Float.parseFloat(obj.getTempView()));
                toolbar.setTitle(et.getText() + "," + obj.getSymbolName());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();


    }


    //Skifter fargen på Toolbaren utifra hvilken temperatur det er
    private void setToolbarColor(float temp) {
        int color = -1;

        if (temp < -10)
            color = getResources().getColor(R.color.primary_indigo);
        else if (temp >=-10 && temp <=-5)
            color = getResources().getColor(R.color.primary_blue);
        else if (temp >-5 && temp < 5)
            color = getResources().getColor(R.color.primary_light_blue);
        else if (temp >= 5 && temp < 10)
            color = getResources().getColor(R.color.primary_teal);
        else if (temp >= 10 && temp < 15)
            color = getResources().getColor(R.color.primary_light_green);
        else if (temp >= 15 && temp < 20)
            color = getResources().getColor(R.color.primary_green);
        else if (temp >= 20 && temp < 25)
            color = getResources().getColor(R.color.primary_lime);
        else if (temp >= 25 && temp < 28)
            color = getResources().getColor(R.color.primary_yellow);
        else if (temp >= 28 && temp < 32)
            color = getResources().getColor(R.color.primary_amber);
        else if (temp >= 32 && temp < 35)
            color = getResources().getColor(R.color.primary_orange);
        else if (temp >= 35)
            color = getResources().getColor(R.color.primary_red);

        toolbar.setBackgroundColor(color);

    }

    //Setter inn værikon
    private void getWeatherDrawable( float bilde ){
        Drawable weatherPicture = getResources().getDrawable( R.drawable.w01d );

        if(bilde == 3)
            weatherPicture = getResources().getDrawable(R.drawable.w02d);
        else if (bilde == 2)
            weatherPicture = getResources().getDrawable(R.drawable.w03d);
            weatherIcon.setImageDrawable(weatherPicture);
    }






/*

    // This is the City Adapter used to fill the listview when user searchs for the city
    class CityAdapter extends ArrayAdapter<City> {

        private List<City> cityList;
        private Context ctx;

        public CityAdapter(Context ctx, List<City> cityList) {
            super(ctx, R.layout.city_row);
            this.cityList = cityList;
            this.ctx = ctx;
        }

        @Override
        public City getItem(int position) {
            if (cityList != null)
                return cityList.get(position);
            return null;
        }

        @Override
        public int getCount() {
            if (cityList == null)
                return 0;

            return cityList.size();
        }

        @Override
        public long getItemId(int position) {
            if (cityList == null)
                return -1;

            return cityList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.city_row, null, false);
            }

            TextView tv = (TextView) v.findViewById(R.id.descrCity);

            tv.setText(cityList.get(position).getName() + "," + cityList.get(position).getCountry());

            return v;
        }

    }

*/


    /*
    class CityAdapter extends ArrayAdapter<String> {
        private List<City> cityList;
        private Context ctx;


        public CityAdapter(Context ctx, List<City> cityList,int resId) {
            super(ctx, R.layout.city_row);
            InputStream inputStream = ctx.getResources().openRawResource(resId);
            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader bufferedreader = new BufferedReader(inputreader);
            String line;
            this.cityList = cityList;
            this.ctx = ctx;

            try
            {
                while (( line = bufferedreader.readLine()) != null)
                {
                    cityList.add(line);
                }
            }
            catch (IOException e)
            {
                return null;
            }
            return cityList;
        }

        @Override
        public City getItem(int position) {
            if (cityList != null)
                return cityList.get(position);
            return null;
        }

        @Override
        public int getCount() {
            if (cityList == null)
                return 0;

            return cityList.size();
        }

        @Override
        public long getItemId(int position) {
            if (cityList == null)
                return -1;

            return cityList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.city_row, null, false);
            }

            TextView tv = (TextView) v.findViewById(R.id.descrCity);

            tv.setText(cityList.get(position).getCityName()); // + "," + cityList.get(position).getCountry());

            return v;

        }

    }


        // legge til listen
        public static String readTextFile(Context ctx, int resId)
        {
            InputStream inputStream = ctx.getResources().openRawResource(resId);

            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader bufferedreader = new BufferedReader(inputreader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            try
            {
                while (( line = bufferedreader.readLine()) != null)
                {
                    stringBuilder.append(line);
                    stringBuilder.append('\n');
                }
            }
            catch (IOException e)
            {
                return null;
            }
            return stringBuilder.toString();
        }



*/

//Location









    }
