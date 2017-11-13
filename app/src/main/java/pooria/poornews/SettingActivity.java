package pooria.poornews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Po0RiA on 10/23/2017.
 * here , we can change Preference Items And Saving them immediately
 */

public class SettingActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.setting_layout);
  }


  //for known about preference changing when that  changed we implement preferenceFragment from onPreferenceChangeListener
  //white this method when user changed preference from list and before that saved this method will run and try to set that on ui

  public static class PrefrenceList extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.fragment_layout);

      //find prefrence from key
      Preference q = findPreference(getString(R.string.Setting_Search_By_Tag_Key));

      //send prefrence to helper method for apropriate prefrence for set summry on ui
      bindPreferenceSummaryToValue(q);


      Preference UserInputPrefrence = findPreference(getString(R.string.Search_Order_by_User_Input_Key));

      bindPreferenceSummaryToValue(UserInputPrefrence);
    }


    //with this method we set summry of prefrence that was changing ;
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

      //convert value to string format
      String stringValue = value.toString();
      //check prefrence form
      if (preference instanceof ListPreference) {
        //becuse listprefrence is a chaild of prefrence we can cast like this
        ListPreference listPreference = (ListPreference) preference;
        //get number of value state in list prefrence
        int prefIndex = listPreference.findIndexOfValue(stringValue);


        if (prefIndex >= 0) {

          //get names of list prefrence and put in a static array;
          CharSequence[] labels = listPreference.getEntries();
          //get name of exact value selected from listprefrence and set to ui
          preference.setSummary(labels[prefIndex]);

        }
      } else {

        preference.setSummary(stringValue);
      }
      return true;
    }


      //helper method for get preference value  and aproperiate that for set on ui
    private void bindPreferenceSummaryToValue(Preference preference) {
      //set change listener for preference to know every moment that item changed it must set that to ui immediately
      preference.setOnPreferenceChangeListener(this);

      //get context of preference from preference manager and put it into a instance of preference
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
      Log.e("prefrence", "changed: " + preferences);
      //get value of preference from key
      String preferenceString = preferences.getString(preference.getKey(), "");
      Log.e("prefrence", "value of :" + preferenceString);

      //send preference and extracted value to on prefrenceChange for set on ui

      onPreferenceChange(preference, preferenceString);
    }
  }


}

