package pooria.poornews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Po0RiA on 10/17/2017.
 * This Class is a Instance of AsyncTaskLoader That get a <List<NewsITems>> as input and Full it with Data that come from Server On backGround
 * then Return a List<NewsItems> as outPut
 */

class QueryLoader extends AsyncTaskLoader<List<NewsItems>> {
  private String mUrlAdress;

   QueryLoader(Context context, String UrlAdress) {
    super(context);
  this.mUrlAdress=UrlAdress;
  }


  //when  loader is Create next this method will run
  //after this method LoadInBackground called

  @Override
  protected void onStartLoading() {
    onForceLoad();

  }

//after OnStratLoading this method create query and send data to QueryNews to get List with full of data ;
//result of this method return back to OnLoadFinished
  @Override
  public List<NewsItems> loadInBackground() {
    return QueryNews.queryListMaker(mUrlAdress);
    //for next step please go to OnLoadFinish in MAin Activity
  }


}
