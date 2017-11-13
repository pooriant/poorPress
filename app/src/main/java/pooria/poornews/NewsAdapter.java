package pooria.poornews;

import android.content.Context;

import android.graphics.Typeface;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by Po0RiA on 11/11/2017.
 * we Add a few Views for showing items on newsListItems And  replace items that has hidden from ui with new elements
 */

 class NewsAdapter extends ArrayAdapter<NewsItems> {


  NewsAdapter(Context context, List<NewsItems> newsItemsList) {
    super(context, 0, newsItemsList);
    //make Loader For Download Image From their Url

  }


  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View view = convertView;


    if (view == null) {
      view = LayoutInflater.from(getContext()).inflate(R.layout.news_frame_layout, parent, false);

    }



    NewsItems currentNewsItem = getItem(position);

    ImageView imageView = (ImageView) view.findViewById(R.id.image_adress_new);
    TextView timeView = (TextView) view.findViewById(R.id.time_and_date);
    TextView titleView = (TextView) view.findViewById(R.id.title_text_new);
    TextView sectionNameView=(TextView)view.findViewById(R.id.title_section_name);

    if (currentNewsItem.getImageAdress().isEmpty()) {
      imageView.setImageResource(R.mipmap.ic_launcher);
    } else {
      imageView.setImageBitmap(currentNewsItem.getBitmap());
    }

    Typeface mediumFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
    Typeface regularFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");



    sectionNameView.setText(currentNewsItem.getSectionName());
    sectionNameView.setTypeface(mediumFont);

    timeView.setText(currentNewsItem.getTime());
    timeView.setTypeface(regularFont);

    titleView.setText(currentNewsItem.getmNewsTitle());
    titleView.setTypeface(mediumFont);


    return view;
  }



}
