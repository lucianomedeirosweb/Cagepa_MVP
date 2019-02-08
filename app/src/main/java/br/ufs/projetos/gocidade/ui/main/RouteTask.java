package br.ufs.projetos.gocidade.ui.main;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by samila on 12/08/17.
 */

public class RouteTask extends AsyncTaskLoader<List<LatLng>> {

    private List <LatLng> mRoute;
    private LatLng mOrigin;
    private LatLng mDestination;

    public RouteTask(Context context, LatLng origin, LatLng destination) {
        super(context);
        mOrigin = origin;
        mDestination = destination;
    }

    @Override
    protected void onStartLoading (){
        if(mRoute == null){
            forceLoad();
        }else {
            deliverResult(mRoute);
        }
    }

    @Override
    public List<LatLng> loadInBackground() {
        mRoute = RouteHttp.loadRoute(mOrigin, mDestination);
        return mRoute;
    }


}
