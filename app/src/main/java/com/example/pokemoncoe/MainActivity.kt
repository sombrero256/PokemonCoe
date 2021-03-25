package com.example.pokemoncoe

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import org.w3c.dom.Text


class MainActivity : FragmentActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap

    var mPoly : Polygon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var reqCode = 101
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), reqCode)
        }

        val locationRequest = LocationRequest.create()?.apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        var circle : Circle? = null
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                //if no location, return
                Log.d("GPS", "In callback.")
                p0 ?: return
                for (location in p0.locations){
                    //ui code goes here.
                        if(circle == null) {
                            Log.d("Callback", "Above circle code")
                            var cOpt = CircleOptions()
                            cOpt.center(LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude))
                            cOpt.radius(200.0)
                            cOpt.fillColor(Color.CYAN)
                            circle = mMap.addCircle(cOpt)
                        } else {
                            circle!!.center = LatLng(p0.lastLocation.latitude, p0.lastLocation.longitude)
                        }

                }
                super.onLocationResult(p0)
            }
        }

        startLocationUpdates(locationRequest!!, locationCallback)
    }

    override fun onMapReady(googleMap: GoogleMap){
        mMap = googleMap
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
            MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney"))
    }


    private fun startLocationUpdates(lr: LocationRequest, lc: LocationCallback) : Task<Void>{
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)

        }
        return fusedLocationClient.requestLocationUpdates(lr, lc, Looper.getMainLooper())

    }

}