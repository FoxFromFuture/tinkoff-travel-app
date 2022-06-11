package com.tinkoff.travelapp

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MapActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mapView: MapView

    private var permissionsGranted: Boolean = false

    private val constLongDistancesMapAnimation = Animation(Animation.Type.SMOOTH, 1.0f)
    private val constShortDistancesMapAnimation = Animation(Animation.Type.SMOOTH, 0.5f)

    private val constZoomFarOff = 12.0f
    private val constZoomCloseUp = 19.0f

    private val constDefaultMapLocation =
        CameraPosition(Point(56.317492, 44.067198), constZoomFarOff, 0.0f, 0.0f)

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
            )
        } else {
            permissionsGranted = true
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.map_map)

        when (PreferenceManager.getDefaultSharedPreferences(this)
            .getString("settings_theme_choosing", "")) {
            "SYNC" -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        mapView.map.isNightModeEnabled = true
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        mapView.map.isNightModeEnabled = false
                    }
                }
            }
            "LIGHT" -> {
                mapView.map.isNightModeEnabled = false
            }
            "DARK" -> {
                mapView.map.isNightModeEnabled = true
            }
        }

        if (permissionsGranted) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location == null) {
                        mapView.map.move(
                            constDefaultMapLocation,
                            constLongDistancesMapAnimation,
                            null
                        )
                    } else {
                        mapView.map.move(
                            CameraPosition(
                                Point(location.latitude, location.longitude),
                                constZoomCloseUp,
                                0.0f,
                                0.0f
                            ),
                            constLongDistancesMapAnimation,
                            null
                        )
                    }
                }
        } else {
            mapView.map.move(
                constDefaultMapLocation,
                constLongDistancesMapAnimation,
                null
            )
        }

        val buttonZoomIn = findViewById<ImageButton>(R.id.map_zoom_in_button)
        buttonZoomIn.setOnClickListener(this)

        val buttonZoomOut = findViewById<ImageButton>(R.id.map_zoom_out_button)
        buttonZoomOut.setOnClickListener(this)

        val buttonMyLocation = findViewById<ImageButton>(R.id.map_my_location_button)
        buttonMyLocation.setOnClickListener(this)

        val buttonBack = findViewById<ImageButton>(R.id.map_back_button)
        buttonBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.map_zoom_in_button, R.id.map_zoom_out_button -> {
                val point = Point(
                    mapView.map.cameraPosition.target.latitude,
                    mapView.map.cameraPosition.target.longitude
                )
                var zoom = 11.0f
                val azimuth = mapView.map.cameraPosition.azimuth
                val tilt = mapView.map.cameraPosition.tilt
                when (view.id) {
                    R.id.map_zoom_in_button -> {
                        zoom = mapView.map.cameraPosition.zoom + 1.5f
                    }
                    R.id.map_zoom_out_button -> {
                        zoom = mapView.map.cameraPosition.zoom - 1.5f
                    }
                }
                mapView.map.move(
                    CameraPosition(point, zoom, azimuth, tilt),
                    constShortDistancesMapAnimation,
                    null
                )
            }
            R.id.map_my_location_button -> {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
                    )
                } else {
                    permissionsGranted = true
                }
                if (!permissionsGranted) {
                    return
                }
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location == null) {
                            mapView.map.move(
                                constDefaultMapLocation,
                                constLongDistancesMapAnimation,
                                null
                            )
                        } else {
                            mapView.map.move(
                                CameraPosition(
                                    Point(location.latitude, location.longitude),
                                    constZoomCloseUp,
                                    0.0f,
                                    0.0f
                                ),
                                constLongDistancesMapAnimation,
                                null
                            )
                        }
                    }
            }
            R.id.map_back_button -> {
                onBackPressed()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (!(grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                Toast.makeText(this, R.string.map_permission_needed, Toast.LENGTH_SHORT).show()
            } else {
                permissionsGranted = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}
