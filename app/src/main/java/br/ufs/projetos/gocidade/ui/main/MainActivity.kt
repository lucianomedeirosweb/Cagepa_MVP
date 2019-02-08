package br.ufs.projetos.gocidade.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import br.ufs.projetos.gocidade.R
import br.ufs.projetos.gocidade.ui.post.PostActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    lateinit var mOrigem : LatLng
    lateinit var mGoogleApiClient : GoogleApiClient

    val REQUEST_ERRO_PLAY_SERVICES = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mGoogleApiClient = GoogleApiClient.Builder (this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        //Create Map Fragment
        val fragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        //Config Map
        mapConfig(fragment)

        // Create Profile Fragment, where will be all the menu options
        val profileFragment = ProfileFragment ()


        // ******** Tabs config *****
        // ---- add tabs
        main_tabs.addTab(main_tabs.newTab().setIcon(R.drawable.ic_map_white_24dp))
        main_tabs.addTab(main_tabs.newTab().setIcon(R.drawable.ic_person_white_24dp))
        // ----- add Fragments
        val pageAdapter  = TabsPagerAdapter(supportFragmentManager)
       // pageAdapter.addFragment(fragment)
       // pageAdapter.addFragment(profileFragment)

        main_pager.adapter = pageAdapter
        main_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener (main_tabs))
        main_tabs.setOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(main_pager))

        btn_take_picture.setOnClickListener { startActivity(Intent (applicationContext, PostActivity :: class.java)) }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ERRO_PLAY_SERVICES && resultCode == Activity.RESULT_OK){
            mGoogleApiClient.connect()
        }
    }



    override fun onConnected(p0: Bundle?) {
        getLastLocation ()
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient.connect()
    }



    override fun onConnectionFailed(p0: ConnectionResult) {
        if(p0.hasResolution()){
            try{

            }catch (e : IntentSender.SendIntentException){
                e.printStackTrace()
            }
        }else {
            showErrorMessage (this, p0.errorCode)
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation () {

        val location : Location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient)

        if (location != null){
            mOrigem = LatLng(location.latitude, location.longitude)
        }
    }

    fun showErrorMessage (activity : FragmentActivity, ErrorCode : Int){
        val TAG = "DIALOG_ERRO_PLAY_SERVICE"
        if (supportFragmentManager.findFragmentByTag(TAG) == null){

        }

    }

    fun mapConfig (fragment : SupportMapFragment){
        fragment.getMapAsync { callback ->
            callback.mapType = GoogleMap.MAP_TYPE_NORMAL
            mOrigem = LatLng(-23.561706, -46.655981)
            callback.animateCamera(CameraUpdateFactory.newLatLng(mOrigem))
            callback.addMarker(MarkerOptions()
                    .position(mOrigem)
                    .title("Av. Paulista")
                    .snippet("São Paulo"))

            val auxPlace = LatLng(-23.88888, -46.77777)

            callback.addMarker(MarkerOptions()
                    .position(auxPlace)
                    .title("Av. Paulista")
                    .snippet("São Paulo"))


            callback.uiSettings.isMapToolbarEnabled = false
            callback.uiSettings.isZoomControlsEnabled = true
            callback.uiSettings.setAllGesturesEnabled(true)


        }
    }


}
