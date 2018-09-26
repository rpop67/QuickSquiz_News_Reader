package com.example.android.quicksquiz;

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

/**
 * Created by Akanksha_Rajwar on 25-09-2018.
 */

public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();

    public static ArrayList<Post> fetchJSONData(String requestUrl) {
        URL url = createURL(requestUrl);

        //perform hhtp request and reeive json data back

        String jsonResponse = null;
        try {
            jsonResponse = makehttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Post> finalPost = extractFeatureFromJson(jsonResponse);
        return finalPost;


    }

    public static URL createURL(String stringUrl)

    {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;

    }

    public static String makehttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return null;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;


    }

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

    private static ArrayList<Post> extractFeatureFromJson(String newsJSON){

        if(TextUtils.isEmpty(newsJSON))
        {
            return null;
        }
        try
        {
            ArrayList<Post> JsonArray=new ArrayList<Post>();
            JSONObject baseJSONOject= new JSONObject(newsJSON);
            JSONObject responseJSONObject=new JSONObject(baseJSONOject.getString("response"));
            JSONArray resultJSONArray= responseJSONObject.getJSONArray("results");
            if(resultJSONArray.length()>0)
            {
                for(int i=0;i<resultJSONArray.length();i++)
                {
                    JSONObject details=resultJSONArray.getJSONObject(i);
                    String section=details.getString("sectionName");
                    Log.v(LOG_TAG,"Section is "+section);
                    String dateAndTime=details.getString("webPublicationDate");
                    Log.v(LOG_TAG,"Date & Time is "+dateAndTime);
                    String[] arrSplit = dateAndTime.split("T");
                    String date=arrSplit[0];
                    Log.v(LOG_TAG,"date is "+date);
                    String timeParsed=arrSplit[1];
                    String time=timeParsed.substring(0,5);
                    Log.v(LOG_TAG,"Time is "+time);

                    String postTitle=details.getString("webTitle");
                    Log.v(LOG_TAG,"postTitle is "+postTitle);
                    String url=details.getString("webUrl");
                    Log.v(LOG_TAG,"URL is "+url);
                    JSONArray tagsJSONArray=details.getJSONArray("tags");

                    String author="";
                    if(tagsJSONArray.length()>0)
                    {
                        JSONObject tagDetails = (JSONObject) tagsJSONArray.get(0);
                      String first=tagDetails.getString("firstName");
                      String last=tagDetails.getString("lastName");
                      author=first+" "+last;

                    }
                    Log.v(LOG_TAG,"Author is "+author);
                    JsonArray.add (new Post(postTitle,date,time,author,url,section));


               }
               return JsonArray;
            }

        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the  JSON results", e);
        }
        return null;



        }
}
