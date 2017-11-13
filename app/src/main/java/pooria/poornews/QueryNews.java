package pooria.poornews;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Po0RiA on 10/13/2017.
 * it is a helper method,white this method we get new Data From  www.guardian.com in a Json Respnse and Make List of Data From that
 */

//helper method for get new data from Guardian.com and show to UI
class QueryNews {


  private static final String LOG_TAG = QueryNews.class.getName();

  /**
   * this method get {@link String mAdress} and give us a {@link List<NewsItems>newsListExtracted}
   */
  static List<NewsItems> queryListMaker(String mAdress) {
    Log.i(LOG_TAG, "StartQuery");
    //we make a list for put in final list items ;
    List<NewsItems> newsListExtracted = new ArrayList<>();

    try {
      //try to make Url from String Url Adress
      URL url = makeUrlAdress(mAdress);
      //try to make json response From Stream;
      String jsonResponse = makeJsonResponseFromUrl(url);
      //make list with JsonResponse

      newsListExtracted = makeListFromJsonResponse(jsonResponse);

    } catch (IOException e) {
      e.printStackTrace();
    }

    return newsListExtracted;
  }

  private static URL makeUrlAdress(String urlString) throws MalformedURLException {

    if (urlString != null) {
      return new URL(urlString);
    } else {
      Log.e(LOG_TAG, "Error Acour middle of Making Url");
      return null;

    }
  }


  private static String makeJsonResponseFromUrl(URL url) throws IOException {
    String jsonResponse = "";
    if (url == null) {


      return jsonResponse;
    }
    InputStream inputStream = null;

    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
    httpURLConnection.setRequestMethod("GET");
    httpURLConnection.setConnectTimeout(15000);
    httpURLConnection.setReadTimeout(10000);
    httpURLConnection.connect();
    if (httpURLConnection.getResponseCode() == 200) {

      //get result of code in form of byte;
      inputStream = httpURLConnection.getInputStream();
      jsonResponse = readFromStream(inputStream);

    }

    if (httpURLConnection != null) {
      httpURLConnection.disconnect();
    }
    if (inputStream != null) {
      inputStream.close();
    }


    return jsonResponse;
  }


  /**
   * @param inputStream we send {@param InputSream} to this method to convert Server REsponse to String which contain Server Json Response;
   * @return jsonRespone with whole of Server Response;
   * @throws IOException for Readline beacuse that maybe are wrong
   */
  private static String readFromStream(InputStream inputStream) throws IOException {
    StringBuilder outPut = new StringBuilder();
    if (inputStream != null) {
      //decode result of stream in form of readable character

      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
      //we use buffer reader for make output like array:
      //when we use buffer to decode every each character and make lines next send to
      //if we dont use buffer for every char we must send new request query to translate next byte

      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      //read every character and add to string

      String line = bufferedReader.readLine();
      //until line is not null add line index to string builder

      while (line != null) {
        outPut.append(line);
        //after that read line again until finished read character

        line = bufferedReader.readLine();
      }
    }

    return outPut.toString();

  }


  private static List<NewsItems> makeListFromJsonResponse(String jsonResponse) {

    List<NewsItems> newsItemses = new ArrayList<>();
    try {
      JSONObject jsonObject = new JSONObject(jsonResponse);

      JSONObject response = jsonObject.getJSONObject("response");
      JSONArray results = response.getJSONArray("results");

      Bitmap bitmap ;
      for (int i = 0; results.length() > i; i++) {
        JSONObject objectIndex = results.getJSONObject(i);
        String sectionName = objectIndex.getString("sectionName");

        String mTitle = objectIndex.getString("webTitle");

        Log.e(LOG_TAG, "" + sectionName);
        String webUrl = objectIndex.getString("webUrl");
        JSONObject objectFileds = objectIndex.getJSONObject("fields");

        String bodyText = objectFileds.getString("bodyText");


        String imageAdress = objectFileds.getString("thumbnail");


        bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageAdress).getContent());

        String time = objectFileds.getString("lastModified");

        String newTime = timeAndDateFormater(time);
        Log.i(LOG_TAG, newTime);
        newsItemses.add(new NewsItems(mTitle, imageAdress, bodyText, webUrl, newTime, bitmap, sectionName));

      }


    } catch (JSONException e) {
      //if occour any error when app is trying to execute any statment  in "try" block
      //with catch block app doesn't crash and we can find error massage with blow Log
      Log.e(LOG_TAG, "Error when it trying to parse the jsonResponse" + e);
    } catch (ParseException | MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }


    return newsItemses;


  }

  private static String timeAndDateFormater(String oldDateAndTime) throws ParseException {
    String[] datePart = oldDateAndTime.split("T");
    String[] timePart = datePart[1].split("Z");

    //prepare time with new format
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:m:s", Locale.ENGLISH);
    Date TimeOld = timeFormat.parse(timePart[0]);
    SimpleDateFormat TimeNewFormat = new SimpleDateFormat("kk:mm", Locale.ENGLISH);
    TimeNewFormat.format(TimeOld);
    //prepare date with new format
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date date = fmt.parse(datePart[0]);
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
    fmtOut.format(date);
    return fmtOut.format(date) + "," + TimeNewFormat.format(TimeOld);

  }

}


