package com.example.pokemoncoe

import android.graphics.Point
import android.graphics.drawable.Drawable
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class WorldPokemon {
    var mLatLng : LatLng? = null
    lateinit var mCircleOptions : CircleOptions
    //This will be null until addPokemonToMap has been run on this 'mon
    var mCircle : Circle? = null
    var id : Int = -1
    var mName : String = "Squirtle"
    //Maybe add a secondary object later that contains more specific data. This class is just for the world info
    constructor(){

    }

    constructor(l: LatLng, c: CircleOptions, i: Int){
        mLatLng = l
        id = i
        mCircleOptions = c

    }
}

fun addPokemonToMap(wp : WorldPokemon, map : GoogleMap){
    Log.d("Add", wp.mCircleOptions.toString())
    wp.mCircle = map.addCircle(wp.mCircleOptions)
    wp!!.mCircle!!.tag = wp.id.toString()
}

fun addPokemonToMap(wp : MutableList<WorldPokemon>, map : GoogleMap){
    for(mon in wp){
        mon.mCircle = map.addCircle(mon.mCircleOptions)
        mon.mCircle!!.tag = mon.id.toString()
    }
}