package com.wunder.challenge.ui.map

import android.annotation.TargetApi
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

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.wunder.challenge.R
import com.wunder.challenge.injection.ViewModelFactory
import com.wunder.challenge.model.PlaceMark
import com.wunder.challenge.ui.placemark.PlaceMarkListViewModel
import android.view.MenuItem


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMarkerClickListener {

    private val TAG = "MapsActivity"

    private var position: Int = 0
    private lateinit var googleMap: GoogleMap
    private lateinit var placeMarkListViewModel: PlaceMarkListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        position = intent.getIntExtra("position", 0)

        placeMarkListViewModel = ViewModelProviders.of(this, ViewModelFactory(this))
                .get(PlaceMarkListViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.setOnMapLoadedCallback(this)
        this.googleMap.setOnMarkerClickListener(this)
    }

    override fun onMapLoaded() {
        Log.d(TAG, "onMapLoadedCallback")
        val markerList: ArrayList<Marker> = showMarkers(placeMarkListViewModel.placeMarks)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerList[position].position, 15.0f),
                object : GoogleMap.CancelableCallback {
                    override fun onFinish() {
                        markerList[position].showInfoWindow()
                    }

                    override fun onCancel() {
                    }
                })
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, 15.0f),
                object : GoogleMap.CancelableCallback {
            override fun onFinish() {
                marker?.showInfoWindow()
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
        return MarkerOptions().position(point).title("Car:${placeMark.name}").icon(icon)
    }

    private fun showMarkers(placeMarks: List<PlaceMark>): ArrayList<Marker> {
        googleMap.clear()
        val builder = LatLngBounds.Builder()
        val markerList: ArrayList<Marker> = arrayListOf()
        placeMarks.asSequence().map {
            Pair(it, googleMap.addMarker(constructMarkerOptions(it)))
        }.map {
            it.second.tag = it.first
            it.second
        }.map {
            builder.include(it.position)
            markerList.add(it)
        }.toList()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 40))
        return markerList
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
