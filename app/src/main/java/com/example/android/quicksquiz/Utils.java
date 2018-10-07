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
        } finally {
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

    private static ArrayList<Post> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        try {
            ArrayList<Post> JsonArray = new ArrayList<Post>();
            JSONObject baseJSONOject = new JSONObject(newsJSON);
            JSONObject responseJSONObject = new JSONObject(baseJSONOject.getString("response"));
            JSONArray resultJSONArray = responseJSONObject.getJSONArray("results");
            if (resultJSONArray.length() > 0) {
                for (int i = 0; i < resultJSONArray.length(); i++) {
                    JSONObject details = resultJSONArray.getJSONObject(i);
                    String section = details.getString("sectionName");
                    Log.v(LOG_TAG, "Section is " + section);
                    String dateAndTime = details.getString("webPublicationDate");
                    Log.v(LOG_TAG, "Date & Time is " + dateAndTime);
                    String[] arrSplit = dateAndTime.split("T");
                    String date = arrSplit[0];
                    Log.v(LOG_TAG, "date is " + date);
                    String timeParsed = arrSplit[1];
                    String time = timeParsed.substring(0, 5);
                    Log.v(LOG_TAG, "Time is " + time);

                    String postTitle = details.getString("webTitle");
                    Log.v(LOG_TAG, "postTitle is " + postTitle);
                    String url = details.getString("webUrl");
                    Log.v(LOG_TAG, "URL is " + url);
                    String thumbnail="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARMAAAC3CAMAAAAGjUrGAAAAY1BMVEUzptYyptU1p9Y0p9YwpdUxpdU2p9Y3qNY0ptYvpdUxptX///84qNc2qNb4/P5OsNul1eu+4PCFxeNHrtlVs9v0+v2az+giotXF4/Jdtt2Sy+Z3weKy2u7X7PZ8w+LO6PTk8vmrMKyAAAAKLElEQVR4nOXdC1ciORAF4F6RRy/gY9BhnNHV//8rFxWwO6nHrUolgJPdOWfOWUD59t50i91JNx2PfjSu85E8fvrvx/hHGl3QEL/I5/eRfndT4h2M3mH2hN3oIkiaiDhVHChjE43EHJJIEJUFRekVFMGkPCTxIohKMcrIJJakjois4kERTUJJ6omIKmR/jCgDk0iSuiKSSgAKY1JGUl9EVSlB+TIxxUQ84MDvakGOYhUIBTGxk5SI0BpmmToopEldEt3D4FID5WASRGLmmKTD6gKjyFMKYSI3J4hExhBkHColKISJmwQFkTlImDooTHu6JiQOEBNLBEpiYmgOSwKJ2EBSl1YomUk4SZFHwlKKgrWnszfHRBIhMmSpiTIwEZtTlpIoEK+KD6WzNudUIoCKC4U0qUUSLqKrMCjWoMgmSHNEEuBtXg1GqUoMSleHBAnJFTvaovhNHCQeDgRGUqFNbCgdSMJMJh4RBERhKUMxmDQhwUFkFiuKJSigiZUkBkRi4aNShjIysU8mtpD4QHiWEhQ5KKyJ3hwTSYmIqAKhWKaUgUlIc2qJMCo2FDgonImrOQxJhAitUgXlaGKOCUwSJUKqcJOKuT2AyXmSGKJSEpTO1xyUJFaEVAlCMZsAMWlE4kdJ28MHpatIgr3H2XC4VAwoUFBcJjEkM3Z4UOztYYPSnYaE98BgXChoUBQTrDkESSGIyhKBwgWlk89NoJiYSGAQRQVBcQaFMKlJYhMRVZB51haUgUnhbGIgsYtILE4UPSiiSSyJU4RXAVBcQclMlJiQzUFICkQMKCFB6QJiApCUibAqOoonKEYTvTkOkvV+OFRcKHxQ9ibBMbGJrKnhR0GmFCAoNpNIEtJDdoFQbEEBTFqRiCACSwCKGJR4E1Bk8Mbn2VBUHCjm8ogm5TGRRHKP3MWMEhGUTiABTMwkOkjCAqBEBsVoYm+OW2TIYkTBgiIejjuYJDXRm8OIQCCSihnFGBTcxBwTmgQX+VJRUdL2yEHRZlnBxBaTKiScSmhQRJOS6mgkThEvSlF5YBNjTMJIDioKijkoLpPImJSI+FBKykObBMekkITsj9QeNShyeTqKxGxSmYSMSmlQHCZqdYSYxJN8osDtMQYlKQ9pEhoTiGS1sqM0MDFWB42JQrJKRjUUQ3k4E7E6hphIJKmH5qKhGINiMgmMiUDCiQgqKYocFHd5Aky05hzf0mz4pIU4ribryWjMDiYSSlB5upyksDo8yebh1jseNrP4oASbOEjm/d3yxjuWdz3SHkNQhPIQJlp14JisMxP/2JukKBdhwsekiomMgptMy03gmMyjTUxBcU4oXUaSTycdbiLE5GBy8/7P8V/1r7s/I5O5GJSQ8thN0OpkZyafJr821mPO78wECkqkiW06wauzN7nfdrbR/xibWILS0MQVk6OJdAJLDMqEDwo+ofCTLGDSVTRZLOwm87jyiCbe6YStDvFzTm4y2c43/bXHBAlKUxNfTHKT6e3r4+Pb795qIgbFNKGAJnHTSUKySk26p8/D7LOMQpusW5rE5ISISW7ysj8f+zM5PsZjgpcnM6EPPHpOpCnWUJ1VarJ4OJyjPvfDhwEmYHlME0qBiTTFytVJTbqng8lrP34gZBJdnhOYrPKc3GY5Wa8nOcp5mNSYTnKTVf+6N3naP3ayefvTZyiUiVSeMzbJqpOZXD08fpD83L/dyfptudxkSbkwE1N1UpN3hOe3x9en/btdrd+PQ4/rVYJiNTEdeMiD8SlNdofnbd/vD8Sr/vPQ/LI7W+nngAkwoZyTCTmdJCZJt+b9r/3scr+9vesvyQQ77AAmGcn98SO1X4+7KaaRCTmhdLbTWKfJSjPpn5ej8TCRTebf3yT7qPalT022ggl34FFOZONMsEOxyaT/uUzHV3suwQTLyfgdZCZjkt8Zya49C82EK8+Fm/T97m/9fwTJV3vqmXAnbXEmM7tJf3+36vs/FMlyefis6RJMuPnEbvJxSvLMkCyX88XFmITlZHBKQo6X7cWbWOeT9JSEa89fZAL89vjmdnIhJtx8Yjo/oU5J8vG6/VbnJ4rJljolyceP/u8xAUmWN5sO+XkHNsmqI5g0/rmYPktj2nOqn4vbfn7CnaXR7fkmJvLnbJMnw8V9j5uz/0wp5LPHjWXMry/ZBP/cfvExVuxYDEarz+2jTHy/3znM1eyK1YvR/9/tWfwuQ/k9IGAi/B5wcD3bAzPk69nw6pz770aPvy+2Xve4TK97vGQT5roC56h+XYE0n4RNssz1JxEmJ7/+JPg6pXiTK8FEiQloUvV6tuL7MizVaXrtlrM8M/MV1Nn9O6bqlJrEXLylBGW28I33LzQgaXV9bIvrqOVbAPmRvoohJmdmEodCmMxAE9cU2/IeBCeKKSaVTKrdv+NDyV7BEJNQkyr3eblQKJJW93k1uB/Qg6KRnNH9gCVBMaAQT7bEJPC+0aIJBQgKjKKTnNAk5j50owr1PIUk+j70GuXhUXQVjqTlegVnta4Fs7BFbExM61oElufbrH9SuNgH2B7GhX+oRlJhnZya6ykJKF8wyoO0ycS08JZwdsKYRC4eBKEAIyOptsZUnbXIvs/6bGe8jh9AUmcdv8igVFjvEV+xrnB5tqg1MKF1QeNCUhgT97qgoUEpWSzVSlJanXbrDHtRqN40XGf4bNejVkkCl9Q1mZiD8j3WLS/bBkFHsakwIsCi/+l3GmhSYR+E9RpkOT7QQRK7D0KD/TLWCAsv0n6/jCb7qqzXosvgvxJPVkiAmOgb8FTff4d4X/n2O4kFB3Ka/Xfa7dOUEgAgISTAhl7lJgX7eZk8gjY5E2NyHbXv20RFkfd9AzRYEldMlOpc+P6AOokjJpeyjyRKErRnYqv9RuNFABJPTOz70pJBqbsJK/NyDhJ4X9qG+xdXE/Ft6kyTKCYn3+ca3dIZI8FiErcf+gRFMagIL4KQlOyHXiMo4ibxEIv4AhEkXEw+TZSg6ChUUmQVEUZ7JkWCNQeJiWoCtseFQskgT0m/Dk3ijsnepLg9BSjmUUACxcRpwgSlhUr6FVASPCaMiQeFucfvQkgIEykoZ4SSvThMYojJ0cTcHhwlTIURIUiKYjLlTFztYW+RrSPChaSMZGAS0h7+vuEKIkYSOCa8ia89zOGnVIUVwUhsMRmYVEdxs5CvVUIixwQ1MaDI993HgNhJDDEZmsSihKhwr8GLECRqc5KYiCZZe2worArmwj+7kESLycjEExQaRYmK6iI+URAJIZl205oosgpFoz5+EU1iNSlAwVUsQxThSIwx2ZngKMyUwqHEq8giUSSpid4eC0qsyqIGCWNibA+F0kBFE4kj+TCpinJQKWJZeEQIErE5XhMPylHFy6KLeEnImHya1Eb5UjGzLACQYBLCxI0CqhhcFn4RM0lm0gBlzKK6jB4sv66bhInJwURuD4yiqIxZGJn0McpL0t8HQYI258ukEUrOIg711aJIRib/Az06cdabbxtGAAAAAElFTkSuQmCC";

                    try
                    {
                        //JSONObject fieldsJSONObject= new JSONObject(details.getString("fields"));
                        JSONObject fieldsJSONObject=details.getJSONObject("fields");
                        Log.v(LOG_TAG, "Field is fields" );
                        thumbnail=fieldsJSONObject.getString("thumbnail");
                        Log.v(LOG_TAG, "thumbnail is " + thumbnail);
                    }catch(Exception e){
                        thumbnail="data:image/png;base64,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";
                    }


                    JSONArray tagsJSONArray = details.getJSONArray("tags");

                    String author = "";
                    if (tagsJSONArray.length() > 0) {

                        JSONObject tagDetails = (JSONObject) tagsJSONArray.get(0);

                        author=tagDetails.getString("webTitle");



                    }
                    Log.v(LOG_TAG, "Author is " + author);
                    JsonArray.add(new Post(postTitle, date, time, author, url, section,thumbnail));
                }
                return JsonArray;
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the  JSON results", e);
        }
        return null;


    }
}
