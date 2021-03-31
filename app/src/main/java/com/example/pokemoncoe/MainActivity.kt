package com.example.pokemoncoe

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task

const val TENTHMETER = 0.000001


class MainActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    var Mons : MutableList<WorldPokemon>? = null
    private var mHandler = Handler()
    private var mProjection : Projection? = null

    private var lastLoc : LatLng? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var reqCode = 101
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), reqCode)
        }
        val locationRequest = LocationRequest.create()?.apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                //if no location, return
                p0 ?: return
                //Graphical updates here.
                drawPlayer(p0)
                mProjection = mMap.projection
                lastLoc = LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude)


                super.onLocationResult(p0)
            }
        }

        startLocationUpdates(locationRequest!!, locationCallback)

        var runnable: Runnable = object : Runnable {
            override fun run(){
                //Gameplay updates here
                gameCode()
                mHandler.postDelayed(this, 1000)
            }
        }
        mHandler.postDelayed(runnable, 1000)


    }

    override fun onMapReady(googleMap: GoogleMap){
        mMap = googleMap
        val circleCallback = object : GoogleMap.OnCircleClickListener{
            override fun onCircleClick(p0: Circle?) {
                Log.d("Circles", "Circle clicked is ${p0!!.tag.toString()}" )
            }
        }
        mMap.setOnCircleClickListener(circleCallback)
    }

    var circle : Circle? = null
    fun drawPlayer(p0 : LocationResult){

        if(circle == null) {
            var cOpt = CircleOptions()
            cOpt.center(LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude))
            cOpt.radius(5.0)
            cOpt.strokeColor(Color.rgb(255, 69, 0))
            cOpt.fillColor(Color.rgb(255, 140, 0))
            circle = mMap.addCircle(cOpt)

            var cp = CameraPosition.builder()
            cp.tilt(45f)
            cp.zoom(20f)
            cp.bearing(0f)
            cp.target(LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude))

            var cu1 = CameraUpdateFactory.newCameraPosition(cp.build())
            mMap.animateCamera(cu1)

        } else {

            circle!!.center = LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude)
            var cp = CameraPosition.builder()
            cp.tilt(45f)
            cp.zoom(20f)
            cp.bearing(0f)
            cp.target(LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude))

            var cu1 = CameraUpdateFactory.newCameraPosition(cp.build())
            mMap.animateCamera(cu1)

        }
    }

    private fun startLocationUpdates(lr: LocationRequest, lc: LocationCallback) : Task<Void>{
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
        }
        return fusedLocationClient.requestLocationUpdates(lr, lc, Looper.getMainLooper())
    }

    override fun onResume() {

        super.onResume()
        val locationRequest = LocationRequest.create()?.apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                //if no location, return
                Log.d("GPS", "In callback.")
                p0 ?: return
                drawPlayer(p0)
                lastLoc = LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude)
                super.onLocationResult(p0)
            }
        }
        var t = startLocationUpdates(locationRequest!!, locationCallback)

    }

    fun gameCode(){
        if(lastLoc == null ){
            return;
        }
        Log.d("Gameplay", "In gameplay code")
        if (Mons == null){
            Mons = mutableListOf<WorldPokemon>()

            var cOpt1 = CircleOptions()
            var pkmnLoc1 = LatLng(lastLoc!!.latitude + TENTHMETER * 100, lastLoc!!.longitude + TENTHMETER*100)
            cOpt1.center(pkmnLoc1)
            cOpt1.radius(5.0)
            cOpt1.clickable(true)

            Mons!!.add(WorldPokemon(pkmnLoc1, cOpt1, 0))
            addPokemonToMap(Mons!!, mMap)
        }
    }
}