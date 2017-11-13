package pooria.poornews;

import android.graphics.Bitmap;

/**
 * Created by Po0RiA on 10/13/2017.
 * we made class for news block
 */

 class NewsItems {

  private String mNewsTitle;
  private String bodyText;
  private String webUrl;
  private String time;
  private String imageAdress;
private  Bitmap bitmap;

   String getSectionName() {
    return sectionName;
  }

  private String sectionName;


   NewsItems(String mNewsTitle,String imageAdress,String bodyText,String webUrl,String time,Bitmap bitmap,String sectionName) {
    this.mNewsTitle = mNewsTitle;
    this.imageAdress=imageAdress;
    this.bodyText =bodyText;
    this.webUrl=webUrl;
    this.time=time;
     this.bitmap=bitmap;
     this.sectionName=sectionName;
  }




  String getmNewsTitle() {
    return mNewsTitle;
  }



  String getBodyText() {
    return bodyText;
  }


   String getWebUrl() {
    return webUrl;
  }

   String getTime() {
    return time;
  }

  Bitmap getBitmap() {
    return bitmap;
  }

  String getImageAdress() {
    return imageAdress;
  }






}
