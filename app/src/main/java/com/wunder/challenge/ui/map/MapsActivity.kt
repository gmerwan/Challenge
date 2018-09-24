package com.wunder.challenge.ui.map

import android.annotation.TargetApi
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.wunder.challenge.R
import com.wunder.challenge.model.PlaceMark
import com.wunder.challenge.ui.placemark.PlaceMarkListViewModel
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMarkerClickListener {

    private val TAG = "MapsActivity"

    private lateinit var googleMap: GoogleMap
    private lateinit var placeMarkListViewModel: PlaceMarkListViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        placeMarkListViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceMarkListViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.setOnMapLoadedCallback(this)
        this.googleMap.setOnMarkerClickListener(this)
    }

    override fun onMapLoaded() {
        Log.d(TAG, "onMapLoadedCallback")
        showMarkers(placeMarkListViewModel.placeMarks)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, 13.0f), object : GoogleMap.CancelableCallback {
            override fun onFinish() {

            }

            override fun onCancel() {
            }
        })
        return true
    }

    private fun constructMarkerOptions(placeMark: PlaceMark): MarkerOptions {
        val point = LatLng(placeMark.coordinates[0], placeMark.coordinates[1])
        val icon = BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_car))
        //snippet to distinguish markers within UiAutomator
        return MarkerOptions().snippet("Car:${placeMark.name}").position(point).title(placeMark.name).icon(icon)
    }

    private fun showMarkers(placeMarks: List<PlaceMark>) {
        googleMap.clear()
        val builder = LatLngBounds.Builder()
        placeMarks.map {
            Pair(it, googleMap.addMarker(constructMarkerOptions(it)))
        }.map {
            it.second.tag = it.first
            it.second
        }.map {
            builder.include(it.position)
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 40))
    }

    private fun showErrorMessage(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
    }

    private fun getBitmap(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(this, drawableId)
        return when (drawable) {
            is BitmapDrawable -> BitmapFactory.decodeResource(this.resources, drawableId)
            is VectorDrawable -> getBitmap(drawable)
            else -> throw IllegalArgumentException("unsupported drawable type")
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }
}
