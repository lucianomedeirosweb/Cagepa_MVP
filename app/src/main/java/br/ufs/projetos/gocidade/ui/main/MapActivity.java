package br.ufs.projetos.gocidade.ui.main;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import br.ufs.projetos.gocidade.R;
import br.ufs.projetos.gocidade.util.ConfigConstants;


public class MapActivity extends AppCompatActivity implements  OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener,
        SearchView.OnQueryTextListener, View.OnClickListener {


    private LatLng mOrigem;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mFragment;
    private Handler mHandler;
    private boolean mShouldShowDialog;
    private int mAttempts;
    private MessageDialogFragment mAddressDialog;
    private TextView mProgressText;
    private LinearLayout mProgressLayout;
    private LoaderManager mLoaderManager;
    private LatLng mDestination;
    private SearchView mSearchView;

    private static final int REQUEST_PERMISSIONS = 3;
    private static final String TAG = "search_spot_map";

    private ArrayList<LatLng> mRoute;

    private static final String DIALOG_PARKING_INFO = "DialogParkingInfo";

    private Marker mCurrentLocationMarker;
    private Toolbar toolbar;
    private FloatingActionButton floatBtn;
    private FrameLayout mFrameLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.mtoolbar);
//        setActivityTitle(getString(R.string.app_name));
//        enableDrawer(true, true, false);
        setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

//        mFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.search_spot_map);

//        mAvailableparkings = new ArrayList<Parking>();
//        mSearchSpotPresenter = new SearchSpotPresenter(this);
//        mSearchSpotPresenter.loadAvailableParkings();

//        LatLng posicaoInicial = new LatLng(-10.9472468, -37.0730823);
//        LatLng posicaiFinal = new LatLng(-22.9068467,-43.1728965);
//        double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);
//        Log.i("SRBS","A Distancia é = "+formatNumber(distance) );
//        Log.i("SRBS", formatNumber(distance));
//

 //       nearParkings = new ArrayList<>();

        mLoaderManager = getSupportLoaderManager();
        mProgressText = (TextView) findViewById(R.id.search_spot_progress);
        mProgressLayout = (LinearLayout) findViewById(R.id.search_spot_llProgress);
        mFrameLayout = findViewById(R.id.camera_content);
        TabLayout tabLayout = findViewById(R.id.main_tabs);
        ViewPager viewPager = findViewById(R.id.main_pager);
        mShouldShowDialog = savedInstanceState == null;
        mHandler = new Handler();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

//        mPlaceEditor = (EditText) findViewById(R.id.search_spot_edit_place);
//        mSearchButton = (ImageButton) findViewById(R.id.search_spot_img_search);
//        mSearchButton.setOnClickListener(this);

        floatBtn =  findViewById(R.id.btn_take_picture);

        floatBtn.setOnClickListener(this);

        // ******** Tabs config *****
        // ---- add tabs
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_map_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_person_white_24dp));
        // ----- add Fragments
        TabsPagerAdapter pageAdapter  = new TabsPagerAdapter(getSupportFragmentManager());
        // pageAdapter.addFragment(fragment)
        // pageAdapter.addFragment(profileFragment)

        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener (tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putBoolean(ConfigConstants.EXTRA_DIALOG, mShouldShowDialog);
//        outState.putParcelable(ConfigConstants.EXTRA_ORIG, mOrigem);
//        outState.putParcelable(ConfigConstants.EXTRA_DEST, mDestination);
//        outState.putParcelableArrayList(ConfigConstants.EXTRA_ROUTE, mRoute);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAddressDialog != null) {
            mAddressDialog.dismiss();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        mShouldShowDialog = savedInstanceState.getBoolean(ConfigConstants.EXTRA_DIALOG, true);
//        mOrigem = savedInstanceState.getParcelable(ConfigConstants.EXTRA_ORIG);
//        mDestination = savedInstanceState.getParcelable(ConfigConstants.EXTRA_DEST);
//        mRoute = savedInstanceState.getParcelableArrayList(ConfigConstants.EXTRA_ROUTE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("SRBS", "Entrou em onMapReady");
//
//        if (googleMap == null){
//            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag(TAG);
//            mFragment = fragment;
//            fragment.getMapAsync(this);
//        }
        //
        googleMap.clear();

        if (mOrigem != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(mOrigem)
                    .title(getString(R.string.origin)));
        }

        if (mDestination != null) {
            //Lógica Aqui!!
            LatLng parkingCoord;


//            for (int i = 0; i < mAvailableparkings.size(); i++) {
//                parkingCoord = new LatLng(mAvailableparkings.get(i).getParkingLatitude(), mAvailableparkings.get(i).getParkingLongitude());
//               Log.i("SRBS", "Destination diferente de nulo e Parking = " + mAvailableparkings.get(i).getParkingName());
//                double distance = SphericalUtil.computeDistanceBetween(mDestination, parkingCoord);
//                if (distance <= 1000) {
//                    googleMap.addMarker(new MarkerOptions().position(parkingCoord)
//                            .icon(BitmapDescriptorFactory
//                                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
//                            .title(mAvailableparkings.get(i).getParkingName())
//                            .snippet(mAvailableparkings.get(i)
//                                    .getParkingAddress()))
//                            .setTag(mAvailableparkings.get(i).getParkingCNPJ());
//                    nearParkings.add(mAvailableparkings.get(i));
//                }
//            }

        }

        if (mOrigem != null) {
            if (mDestination != null) {
                LatLngBounds area = new LatLngBounds.Builder()
                        .include(mOrigem)
                        .include(mDestination)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(area, 50));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(mDestination)
                        .zoom(17).bearing(90)
                        .tilt(45)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(mOrigem, 17.0f)
                );
            }
        }


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 17.0f));

        // googleMap.addMarker(new MarkerOptions().position(mOrigem).icon(icon).title("LocalAtual"));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //googleMap.setMyLocationEnabled(true);

        //
        if (mRoute != null && mRoute.size() > 0) {

            Log.i("SRBS", "mRoute diferente de nulo " + mRoute.toString());

            //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.navigation);

            mCurrentLocationMarker = googleMap.addMarker(new MarkerOptions()
            .title("Local Atual").position(mOrigem));

            Log.i("AQUI", "Google: " + mGoogleApiClient.isConnected());


            initLocalDetection();

            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(mRoute)
                    .width(5)
                    .color(Color.RED)
                    .visible(true);

            googleMap.addPolyline(polylineOptions);

            Log.i("SRBS", "mRoute Desenhou Polyline");
            CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mOrigem)
                .zoom(50).bearing(90)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        } else {
            Log.i("SRBS", "mRoute é nulo e menor ou igual a zero ");

        }

        googleMap.setOnMarkerClickListener(this);
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(mOrigem)
//                .zoom(17).bearing(90)
//                .tilt(45)
//                .build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    private void loadRoute() {
        mRoute = null;
        mLoaderManager.initLoader(ConfigConstants.ROUTE_LOADER, null, mRouteCallback);
        Log.i("SRBS", "Chamou o LoaderRoute");
        showProgress(getString(R.string.msg_route));
    }

    LoaderManager.LoaderCallbacks<List<LatLng>> mRouteCallback = new LoaderManager.LoaderCallbacks<List<LatLng>>() {
        @Override
        public Loader<List<LatLng>> onCreateLoader(int id, Bundle args) {
            Log.i("SRBS", "OnCreateLoader Destination = " + mDestination);
            return new RouteTask(MapActivity.this, mOrigem, mDestination);
        }

        @Override
        public void onLoadFinished(final Loader<List<LatLng>> loader, final List<LatLng> data) {

            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    mRoute = new ArrayList<LatLng>(data);
                    Log.i("SRBS", "OnLoadFinished: Carregou a rota, size =  " + data.size());
                    mFragment.getMapAsync(MapActivity.this);
                    hideProgress();
                   mLoaderManager.destroyLoader(ConfigConstants.ROUTE_LOADER);

                }
            });
        }

        @Override
        public void onLoaderReset(Loader<List<LatLng>> loader) {

        }
    };


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("SRBS", "onConnected de Search");
        checkGPSStatus();
        if (isLoading(ConfigConstants.ADDRESS_LOADER) && mDestination == null) {
            mLoaderManager.initLoader(ConfigConstants.ADDRESS_LOADER, null, mSearchPlaceCallback);
            showProgress(getString(R.string.msg_progress));
        } else if ((isLoading((ConfigConstants.ROUTE_LOADER)) && mRoute == null)) {
            mLoaderManager.initLoader(ConfigConstants.ROUTE_LOADER, null, mRouteCallback);
            Log.i("SRBS", "Deu init no Route");
            showProgress(getString(R.string.msg_route));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("SRBS", "onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("SRBS", "onConnectionFailed");
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, ConfigConstants.REQUEST_PLAY_SERVICES_ERROR);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            showErrorMessage(this, connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.i("SRBS", "onStart");
    }

    @Override
    protected void onStop() {
        Log.i("SRBS", "onStop");

        mHandler.removeCallbacksAndMessages(null);
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("SRBS", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConfigConstants.REQUEST_PLAY_SERVICES_ERROR && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        } else if (requestCode == ConfigConstants.REQUEST_CHECK_GPS) {
            if (resultCode == RESULT_OK) {
                mAttempts = 0;
                mHandler.removeCallbacksAndMessages(null);
                getLastLocation();
            } else {
                Toast.makeText(this, R.string.error_gps, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void getLastLocation() {
        Log.i("SRBS", "getLastLocation");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("SRBS", "Não tem permissão");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Log.i("SRBS", "location" + location.toString());
        if (location != null) {
            mAttempts = 0;
            mOrigem = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("SRBS", "location n é nulo" + mOrigem.toString());
            mFragment.getMapAsync(this);

        } else if (mAttempts < 10) {
            Log.i("SRBS", "Location é nulo, Google está conectado?" + mGoogleApiClient.isConnected());
            mAttempts++;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLastLocation();
                }
            }, 2000);
        }
    }

    private void showErrorMessage(FragmentActivity activity, final int errorCode) {
        Log.i("SRBS", "ShowErrorMessage");
        final String TAG = "PLAY_SERVICES_ERROR_DIALOG";
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
           final DialogFragment errorFragment = new DialogFragment();

            errorFragment.show(activity.getSupportFragmentManager(), TAG);
        }
    }

    private void checkGPSStatus() {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        final LocationSettingsRequest.Builder locationSettingRequest = new LocationSettingsRequest.Builder();
        locationSettingRequest.setAlwaysShow(true);
        locationSettingRequest.addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(
                mGoogleApiClient,
                locationSettingRequest.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getLastLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        if (mShouldShowDialog) {
                            try {
                                status.startResolutionForResult(MapActivity.this, ConfigConstants.REQUEST_CHECK_GPS);
                                mShouldShowDialog = false;
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.wtf("SRBS", "Isso não deveria acontecer!");
                        break;


                }
            }
        });
    }

    private void searchAddress() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

        mLoaderManager.restartLoader(ConfigConstants.ADDRESS_LOADER, null, mSearchPlaceCallback);
        showProgress(getString(R.string.msg_progress));
    }

    private void showProgress(String text) {
        mProgressText.setText(text);
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressLayout.setVisibility(View.GONE);
    }

    private void showAddressList(final List<Address> foundAddress) {
        hideProgress();
    //    mSearchButton.setEnabled(true);

        if (foundAddress != null && foundAddress.size() > 0) {
            final String[] Addressesdescription = new String[foundAddress.size()];
            for (int i = 0; i < Addressesdescription.length; i++) {
                Address address = foundAddress.get(i);
                StringBuffer street = new StringBuffer();

                Log.i("SRBS", "complete address " + address.toString());

//                for (int j = 0; j < address.getMaxAddressLineIndex(); j++) {
//                    if (street.length() > 0) {
//                        street.append('\n');
//                    }
//                    street.append(address.getAddressLine(j));
//
//                }

                street.append(address.getAddressLine(0));

               // String country = address.getCountryName();
                String AddressDescription = String.format("%s", street);
                Log.i("SRBS", "Address description " + AddressDescription);
                Addressesdescription[i] = AddressDescription;
            }


            DialogInterface.OnClickListener selectClickAddress =
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Address selectedAddress = foundAddress.get(which);
                            mDestination = new LatLng(
                                    selectedAddress.getLatitude(),
                                    selectedAddress.getLongitude());

                            getLastLocation();
                            //loadRoute();
                            mFragment.getMapAsync(MapActivity.this);
                        }
                    };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.address_list_title))
                    .setItems(Addressesdescription, selectClickAddress);

            mAddressDialog = new MessageDialogFragment();
            mAddressDialog.setDialog(builder.create());
            mAddressDialog.show(getSupportFragmentManager(), "DIALOG_DESTINATION_ADDRESS");
        }
    }

    private boolean isLoading(int id) {
        Loader<?> loader = mLoaderManager.getLoader(id);
        if (loader != null && loader.isStarted()) {
            return true;
        }
        return false;
    }

    LoaderManager.LoaderCallbacks<List<Address>> mSearchPlaceCallback =
            new LoaderManager.LoaderCallbacks<List<Address>>() {
                @Override
                public Loader<List<Address>> onCreateLoader(int id, Bundle args) {
                    return new SearchPlaceTask(MapActivity.this, mSearchView.getQuery().toString());
                }

                @Override
                public void onLoadFinished(Loader<List<Address>> loader, final List<Address> data) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            showAddressList(data);
                        }
                    });
                }

                @Override
                public void onLoaderReset(Loader<List<Address>> loader) {

                }
            };


    @Override
    public void onLocationChanged(Location location) {
        if (mOrigem == null) {
            mOrigem = new LatLng(location.getLatitude(), location.getLongitude());
        }
        mCurrentLocationMarker.setPosition(
                new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void initLocalDetection() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //Carrega o arquivo de menu.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchview, menu);

        //Pega o Componente.
         mSearchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        //Define um texto de ajuda:
        mSearchView.setQueryHint("Digite seu destino!");

        // exemplos de utilização:
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
//        mSearchButton.setEnabled(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        searchAddress();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private boolean enabledPermissions (){
        List<String> permissions = new ArrayList<>();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (!permissions.isEmpty() && mShouldShowDialog){
                String [] array = new String[permissions.size()];
                permissions.toArray(array);
                ActivityCompat.requestPermissions(this, array, REQUEST_PERMISSIONS);
                mShouldShowDialog = false;
            }

            return  permissions.isEmpty();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean success = true;

            for (int i=0; i < permissions.length; i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    success = false;
                    break;
                }
            }
            mShouldShowDialog = true;
        if (!success){
            Toast.makeText(this, R.string.local_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void init (){
        SupportMapFragment fragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment == null){
            fragment = SupportMapFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map, fragment, TAG)
                    .commit();
        }
       // mGoogleApiClient.connect();
        Log.i("LifeCicle", "Chamou init " + mGoogleApiClient.isConnected());
        mFragment = fragment;
        mFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("SRBS", "onResume do Search");
        if(enabledPermissions()){
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_take_picture){
            CamPhotoFragment fragment = new CamPhotoFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.camera_content, fragment, "FragmentPhoto");
            mFrameLayout.setVisibility(View.VISIBLE);
        }
    }
}