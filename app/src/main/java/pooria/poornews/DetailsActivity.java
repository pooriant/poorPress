package pooria.poornews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;



public class DetailsActivity extends AppCompatActivity {
  ShareActionProvider mShareActionProvider;
String urlAdress;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.details_layout);

    final Intent inputIntent = getIntent();
    Bundle inputBundle = inputIntent.getExtras();

    if (!inputBundle.isEmpty()) {

      String titlesArticle = inputBundle.getString(getString(R.string.Title_of_Article));
      String mainsArticle = inputBundle.getString(getString(R.string.Main_Of_Article));
      String timeAndDateArticle = inputBundle.getString(getString(R.string.Time_And_Date_Of_Article));
      urlAdress=inputBundle.getString(getString(R.string.Url_Adress_Of_Article));
      String sectionName = inputBundle.getString("sectionName");
      byte[] byteArray = getIntent().getByteArrayExtra("imG");
      Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

      ImageView pictureArticle = (ImageView) findViewById(R.id.image_detail_new);
      pictureArticle.setImageBitmap(bmp);



      TextView timeView = (TextView) findViewById(R.id.time_and_date_new);
      TextView titleView = (TextView) findViewById(R.id.title_news_new);
      TextView bodyView = (TextView) findViewById(R.id.text_full_new);
      TextView sectionname = (TextView) findViewById(R.id.section_name);


      Typeface mediunFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
      Typeface regularFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

      titleView.setText(titlesArticle);
      titleView.setTypeface(mediunFont);
      bodyView.setText(mainsArticle);
      bodyView.setTypeface(regularFont);

      timeView.setText(timeAndDateArticle);
      timeView.setTypeface(regularFont);

      sectionname.setText(sectionName);
      sectionname.setTypeface(regularFont);
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.shared_menu, menu);
    MenuItem item = menu.findItem(R.id.menu_item_share);

    mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

    Intent myShareIntent = new Intent(Intent.ACTION_SEND);

    myShareIntent.setType("text/plain");
    myShareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_Of_Share_News));
    myShareIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.title_Of_Share_News));


    setSharedIntent(myShareIntent);

    return super.onCreateOptionsMenu(menu);
  }

  private void setSharedIntent(Intent sharedIntent) {
    if (mShareActionProvider != null) {
      mShareActionProvider.setShareIntent(sharedIntent);
    }


  }


}
