package com.example.pokemoncoe

import android.graphics.drawable.Drawable
import android.util.Log
import com.google.android.gms.maps.model.LatLng

class WorldPokemon {
    var mLatLng : LatLng? = null
    var mDrawable : Drawable? = null
    var id : Int = -1

    constructor(l: LatLng, d: Drawable, i: Int){
        mLatLng = l
        id = i

        mDrawable = d
    }

}