package com.example.pokemoncoe

import android.graphics.Point
import android.graphics.drawable.Drawable
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions

class WorldPokemon {
    var mLatLng : LatLng? = null
    lateinit var mPolyOptions : PolygonOptions
    //This will be null until addPokemonToMap has been run on this 'mon
    var mPoly : Polygon? = null
    var id : Int = -1

    constructor(){

    }

    constructor(l: LatLng, p: PolygonOptions, i: Int){
        mLatLng = l
        id = i
        mPolyOptions = p
    }
}

fun addPokemonToMap(wp : WorldPokemon, map : GoogleMap){
    Log.d("Add", wp.mPolyOptions.toString())
    wp.mPoly = map.addPolygon(wp.mPolyOptions)
}

fun addPokemonToMap(wp : MutableList<WorldPokemon>, map : GoogleMap){
    for(mon in wp){
        mon.mPoly = map.addPolygon(mon.mPolyOptions)
    }
}