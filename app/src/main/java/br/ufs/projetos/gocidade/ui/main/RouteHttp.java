package br.ufs.projetos.gocidade.ui.main;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by samila on 12/08/17.
 */

public class RouteHttp {

    public static List<LatLng> loadRoute (LatLng orig, LatLng dest) {

       List<LatLng> positions = new ArrayList<LatLng>();

        try {

            String routeUrl = String.format(Locale.US, "http://maps.googleapis.com/maps/api/directions/json?" + "origin=%f,%f&destination=%f,%f&" + "sensor=true&mode=driving",
                    orig.latitude, orig.longitude,
                    dest.latitude, dest.longitude);

            URL url = new URL(routeUrl);
            Log.i("SRBS", "URL: " + url.toString() );
            String result = bytesToString(url.openConnection().getInputStream());

            JSONObject json = new JSONObject(result);
            JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
            JSONObject  leg = jsonRoute.getJSONArray("legs").getJSONObject(0);

            JSONArray steps = leg.getJSONArray("steps");
            final int numSteps = steps.length();
            Log.i("SRBS", "numSteps: " + numSteps );

            JSONObject step;

            for(int i = 0; i < numSteps; i++ ){
                step = steps.getJSONObject(i);
                String dots = step.getJSONObject("polyline").getString("points");
                positions.addAll(PolyUtil.decode(dots));
                Log.i("SRBS", "Positions: " + positions.get(0) );
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return positions;
    }

    private static String bytesToString (InputStream is) throws IOException{
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bigBuffer = new ByteArrayOutputStream();
        int readedBytes;
        while ((readedBytes = is.read(buffer)) != -1){
            bigBuffer.write(buffer, 0, readedBytes);
        }

        return new String(bigBuffer.toByteArray(), "UTF-8");

    }
}
