package com.example.android.EarthquakeWatch;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides helper functions for sending the request to and receiving data from the given URL
 */
public final class QueryUtils {

    /**
     * creat url object from the given string URL
     * @param stringUrl
     * @return
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils.java", "Problem building the URL ", e);
        }
        return url;
    }

    /**
     *
     * @param requestUrl
     * @return a list of earthquake objects
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = "";
        try {
            // make http request to the URL
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem making the HTTP request.", e);
        }

        // extract properties: maginitude.. from json file
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        return earthquakes;
    }


    /**
     * make http request to the given URL, read data, and returns a json ouput data
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            // Obtain a new HttpURLConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // if the request is successful
            if (urlConnection.getResponseCode() == 200) {

                // Returns an input stream that reads from this open connection
                inputStream = urlConnection.getInputStream();

                // read stream
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("QueryUtils", "something wrong with request.", e);
        } finally {
            // disconnect
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            // close the stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * read the stream
     * @param inputStream
     * @return jason repsonse
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     *
     * @param earthquakeJSON
     * @return list of earthquakes from json response
     */
    public static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {

        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        List<Earthquake> earthquakes = new ArrayList<>();

        try {
            // create JSONObject
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // get a JSONArray of values with the name "features"
            // https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php for jason format
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for (int i = 0; i < earthquakeArray.length(); i++) {

                // get the json object at index i
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // get json object value with the name "properties"
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                // "geometry"
                JSONArray coordinates = currentEarthquake.getJSONObject("geometry").getJSONArray("coordinates");

                // fill the fields of earthquake
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                double longitude = coordinates.getDouble(0);
                double latitude = coordinates.getDouble(1);


                Earthquake earthquake = new Earthquake(magnitude, location, time, url, longitude, latitude);

                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }





}
