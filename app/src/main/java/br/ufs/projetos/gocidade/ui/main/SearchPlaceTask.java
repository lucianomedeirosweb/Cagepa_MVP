package br.ufs.projetos.gocidade.ui.main;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;
import java.util.Locale;

/**
 * Created by samila on 11/08/17.
 */

public class SearchPlaceTask extends AsyncTaskLoader<List<Address>> {

    private Context mContext;
    private String mPlace;
    private List<Address> mFoundAddresses;


    public SearchPlaceTask(Context context, String place) {
        super(context);
        mContext = context;
        mPlace = place;
    }

    @Override
    protected void onStartLoading() {
        if(mFoundAddresses == null){
            forceLoad();
        }else{
            deliverResult(mFoundAddresses);
        }
    }

    @Override
    public List<Address> loadInBackground() {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        try{
            mFoundAddresses = geocoder.getFromLocationName(mPlace, 10);
        }catch (Exception e){
            e.printStackTrace();
        }

        return mFoundAddresses;
    }
}
