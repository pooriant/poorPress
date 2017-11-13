package pooria.poornews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItems>> {
  private static final String GUARDIAN_REQUEST_URL =

    "https://content.guardianapis.com/search?&show-fields=headline,thumbnail,trailText,short-url,bodyText,lastModified&show-tags=type&api-key=test";

  private static final String LOG_TAG = MainActivity.class.getName();
 private ProgressBar LoadingBar;
  private NewsAdapter mNewsAdapter;

  @Override


  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ListView listView = (ListView) findViewById(R.id.list_view);

   //define  a simple @ProgressBar for show a Loading Widget when user is waiting for load news
    LoadingBar = (ProgressBar) findViewById(R.id.Loadingbar);
    LoadingBar.setVisibility(View.VISIBLE);


    /* define a adapter and pass a blank List of @NewsItems  to load a empty page when user is running app at first time and before data are going to load*/
    mNewsAdapter = new NewsAdapter(MainActivity.this, new ArrayList<NewsItems>());


    listView.setAdapter(mNewsAdapter);



    //when user clicked on each news that news opens in detailActivity
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        NewsItems currentNewsItem = (NewsItems) parent.getAdapter().getItem(position);
        Log.i(LOG_TAG, "" + currentNewsItem.getWebUrl());

        Intent readArticle = new Intent(MainActivity.this, DetailsActivity.class);
        Bundle bundle = new Bundle();

        //pub details of news to bundle for passing between main and detailActivity
        bundle.putString(getString(R.string.Title_of_Article), currentNewsItem.getmNewsTitle());
        bundle.putString(getString(R.string.Main_Of_Article), currentNewsItem.getBodyText());
        bundle.putString(getString(R.string.Url_Adress_Of_Article), currentNewsItem.getWebUrl());
        bundle.putString(getString(R.string.Time_And_Date_Of_Article), currentNewsItem.getTime());
        bundle.putString("sectionName",currentNewsItem.getSectionName());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //prepare Bitmap for passing through intent
        currentNewsItem.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bundle.putByteArray("imG",byteArray);

        readArticle.putExtras(bundle);
        startActivity(readArticle);
      }
    });

/*
check internet connection. if is true we are  going to call Loader,else we are going to show a text to user about internet Problem
*/

    if (isOnline()) {
/*
  @int is number of Loader that we want to call
 * @Bundle if we want to add and pass some package for loader we add that here else we put null for that
 * @callback Interface the LoaderManager will call to report about
 * changes in the state of the loader.  Required
  */
      LoaderManager loaderManager = getLoaderManager();
      loaderManager.initLoader(0, null, this);
    } else {

      TextView textView = (TextView) findViewById(R.id.error_text);
      textView.setText(R.string.Internet_access_error);
      LoadingBar.setVisibility(View.GONE);
    }
  }

  public boolean isOnline() {
    ConnectivityManager cm =
      (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

//create main_menu at top of mAin Activity
    getMenuInflater().inflate(R.menu.main_menu, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
//if item of main_menu is like bottom go to SettingActivity
    int id = item.getItemId();
    if (id == R.id.setting) {
      Intent settingsIntent = new Intent(this, SettingActivity.class);
      startActivity(settingsIntent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

//if result of get loader manager will negative we are going here to make a new Loader


  @Override
  public Loader<List<NewsItems>> onCreateLoader(int id, Bundle args) {

//get preference for finding exist value and set to url
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    //get Value From Preference;
    String TagSelected = sharedPreferences.getString(getString(R.string.Setting_Search_By_Tag_Key), getString(R.string.Setting_Search_By_Tag_Default_Value));
    String UserInput = sharedPreferences.getString(getString(R.string.Search_Order_by_User_Input_Key), getString(R.string.Search_Order_by_User_Input_Default_Value));
    Log.e("Tag", "Selected Defult key is :  " + TagSelected);

    //helper class for building or manipulating URI Reference;

    Uri uri1 = Uri.parse(GUARDIAN_REQUEST_URL);
    Uri.Builder uriBuilder = uri1.buildUpon();
    uriBuilder.appendQueryParameter(getString(R.string.format_parameter_key), getString(R.string.format_parameter_Value));//(format,json);
    uriBuilder.appendQueryParameter(getString(R.string.page_Size_parameter_key),getString(R.string.page_Size_parameter_value));//(pageSize,10);
    uriBuilder.appendQueryParameter(getString(R.string.date_parameter_key),getString(R.string.date_parameter_value));//(from-date,2015-01-01);
    uriBuilder.appendQueryParameter(getString(R.string.Search_Order_by_User_Input_Key), UserInput);//(q,""); i set  empty value for default of user input to
    uriBuilder.appendQueryParameter(getString(R.string.Setting_Search_By_Tag_Key), TagSelected);//(tag,"user selected category");
    uriBuilder.appendQueryParameter(getString(R.string.order_by_parameter_key),getString(R.string.order_by_parameter_value));//(order-by,newest);

    Log.e(LOG_TAG, uriBuilder.toString());

    //we created new loader with 2 param fist is context in this case is MainActivity, secend is UrlAdress we sent trought loader
    //when this loader created automatic this method call QueryLoader.OnStardLoading();
    //for next step go to QueryLoader.OnstartLoading
/*
  we will make new Loader with bottom params
  @Context we pass this or Mainactivity.this
 * @String we will pass url adress to make a new Query
 */
    return new QueryLoader(this, uriBuilder.toString());
  }


  /*when we are here that is mean we have data maybe from old Loader Or from new Query
   in each of these options we have List with Full of data and we can set data To adapter*/

  @Override
  public void onLoadFinished(Loader<List<NewsItems>> loader, List<NewsItems> data) {


   mNewsAdapter.clear();

    LoadingBar.setVisibility(View.GONE);

    if (data != null && !data.isEmpty()) {
//if data is exist add list of data to adapter
      mNewsAdapter.addAll(data);
    } else {

      TextView textView = (TextView) findViewById(R.id.error_text);
      textView.setText(R.string.listISEmptyMassage);
    }


  }



  @Override
  public void onLoaderReset(Loader<List<NewsItems>> loader) {

    mNewsAdapter.clear();

  }




}
