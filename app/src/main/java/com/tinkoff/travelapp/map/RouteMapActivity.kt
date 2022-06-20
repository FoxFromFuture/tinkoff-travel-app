package com.tinkoff.travelapp.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.tinkoff.travelapp.R
import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.route.RouteItem
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError

class RouteMapActivity : AppCompatActivity(), View.OnClickListener,
    DrivingSession.DrivingRouteListener {
    private lateinit var mapView: MapView

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isLocationPermissionsObtained: Boolean? = null

    private var userLocationCoordinates: Location? = null
    private var userLocationMapObject: PlacemarkMapObject? = null
    private lateinit var userLocationCameraListener: CameraListener

    private var isLocationObtained: Boolean = false
    private var isBeingTracked: Boolean = false
    private var isCameraFollowing: Boolean = false

    private lateinit var mapObjects: MapObjectCollection

    private lateinit var routeToBeDisplayed: Route
    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingSession: DrivingSession

    private var isRouteShowingWithUser: Boolean? = null

    private var userRoutePolylines = mutableListOf<PolylineMapObject>()
    private var userRouteObjects = mutableListOf<PlacemarkMapObject>()

    private lateinit var userMapObjectListener: MapObjectTapListener

    private lateinit var activeUserLocationBitmap: ImageProvider
    private lateinit var activePassiveUserLocationBitmap: ImageProvider
    private lateinit var inactiveUserLocationBitmap: ImageProvider

    private val constMapAnimation = Animation(Animation.Type.SMOOTH, 0.4f)
    private val constZoomCloseUp = 19.0f

    private fun moveCameraToPosition(
        point: Point,
        distance: Float = mapView.map.cameraPosition.zoom
    ) {
        mapView.map.move(
            CameraPosition(point, distance, 0.0f, 0.0f),
            constMapAnimation,
            null
        )
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getBitmapFromDrawable(drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_map)

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

        val buttonZoomIn = findViewById<ImageButton>(R.id.map_zoom_in_button)
        buttonZoomIn.setOnClickListener(this)

        val buttonZoomOut = findViewById<ImageButton>(R.id.map_zoom_out_button)
        buttonZoomOut.setOnClickListener(this)

        val buttonMyLocation = findViewById<ImageButton>(R.id.map_my_location_button)
        buttonMyLocation.setOnClickListener(this)

        val buttonBack = findViewById<ImageButton>(R.id.map_back_button)
        buttonBack.setOnClickListener(this)

        activeUserLocationBitmap =
            ImageProvider.fromBitmap(getBitmapFromDrawable(R.drawable.map_user_location_active))
        activePassiveUserLocationBitmap =
            ImageProvider.fromBitmap(getBitmapFromDrawable(R.drawable.map_user_location_active_passive))
        inactiveUserLocationBitmap =
            ImageProvider.fromBitmap(getBitmapFromDrawable(R.drawable.map_user_location_inactive))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val locationList = locationResult.locations
                if (locationList.isNotEmpty()) {
                    val location = locationList.last()
                    userLocationCoordinates = location
                    isLocationObtained = true
                    val point = Point(location.latitude, location.longitude)
                    if (isRouteShowingWithUser != true) {
                        isRouteShowingWithUser = true
                        submitRouteRequest()
                    }
                    if (userLocationMapObject == null) {
                        userLocationMapObject = mapView.map.mapObjects.addPlacemark(
                            point,
                            activeUserLocationBitmap
                        )
                        moveCameraToPosition(point, constZoomCloseUp)
                        isCameraFollowing = true
                    } else {
                        userLocationMapObject?.geometry = point
                        if (isCameraFollowing) {
                            moveCameraToPosition(point)
                        }
                    }
                    if (isRouteShowingWithUser != true) {
                        submitRouteRequest()
                        isRouteShowingWithUser = true
                    }
                    Log.d(
                        "UserLocation",
                        "lat=${location.latitude} lon=${location.longitude}, track?=${isBeingTracked}, follow?=${isCameraFollowing}"
                    )
                }
            }
        }

        userLocationCameraListener = CameraListener { _, _, cameraUpdateReason, _ ->
            if (isCameraFollowing &&
                cameraUpdateReason == CameraUpdateReason.GESTURES
            ) {
                isCameraFollowing = false
                buttonMyLocation.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.yellow_600
                    )
                )
                userLocationMapObject?.setIcon(activePassiveUserLocationBitmap)
            }
        }
        mapView.map.addCameraListener(userLocationCameraListener)

        userMapObjectListener = MapObjectTapListener { mapObject, _ ->
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            val realRoute: RouteItem = mapObject.userData as RouteItem

            val name = view.findViewById<TextView>(R.id.map_place_card_place_name_text)
            val address = view.findViewById<TextView>(R.id.map_place_card_address_text)
            val workingHoursLabel = view.findViewById<TextView>(R.id.map_place_card_work_hours)
            val workingHours = view.findViewById<TextView>(R.id.map_place_card_work_hours_text)

            if (realRoute.type == "STREET") {
                name.text = realRoute.name
                address.text = realRoute.description
                workingHoursLabel.visibility = View.GONE
                workingHours.visibility = View.GONE
            } else {
                name.text = realRoute.name
                address.text = realRoute.description
                if (realRoute.openTime == "00:00" && realRoute.closeTime == "23:59") {
                    workingHours.text = getString(R.string.map_place_card_round_the_clock)
                } else {
                    workingHours.text = "${realRoute.openTime} - ${realRoute.closeTime}"
                }
            }

            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(view)
            dialog.show()
            true
        }

        routeToBeDisplayed =
            Gson().fromJson(intent.getStringExtra("itemRealRoute"), Route::class.java)
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = mapView.map.mapObjects.addCollection()

        buttonMyLocation.performClick()
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
                    constMapAnimation,
                    null
                )
            }
            R.id.map_my_location_button -> {
                if (isLocationPermissionsObtained == false) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        isLocationPermissionsObtained = true
                    } else {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.map_permission_needed),
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                }
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                    )
                } else {
                    isLocationPermissionsObtained = true
                }
                if (isLocationPermissionsObtained != true) {
                    return
                }
                val buttonMyLocation = findViewById<ImageButton>(R.id.map_my_location_button)
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        userLocationCoordinates = location
                        isLocationObtained = true
                    }
                }
                if (!isBeingTracked) {
                    isBeingTracked = true
                    isCameraFollowing = true
                    buttonMyLocation.setColorFilter(ContextCompat.getColor(this, R.color.blue_500))
                    userLocationMapObject?.setIcon(activeUserLocationBitmap)
                    startLocationUpdates()
                    userLocationCoordinates?.let {
                        moveCameraToPosition(
                            Point(it.latitude, it.longitude),
                            constZoomCloseUp
                        )
                    }
                } else {
                    stopLocationUpdates()
                    isBeingTracked = false
                    isCameraFollowing = false
                    buttonMyLocation.setColorFilter(ContextCompat.getColor(this, R.color.black))
                    userLocationMapObject?.setIcon(inactiveUserLocationBitmap)
                }
            }
            R.id.map_back_button -> {
                onBackPressed()
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in permissions.indices) {
            if (permissions[i] == "android.permission.ACCESS_FINE_LOCATION") {
                when (grantResults[i]) {
                    0 -> {
                        isLocationPermissionsObtained = true
                        val buttonMyLocation =
                            findViewById<ImageButton>(R.id.map_my_location_button)
                        buttonMyLocation.performClick()
                    }
                    -1 -> {
                        isLocationPermissionsObtained = false
                        isRouteShowingWithUser = false
                        submitRouteRequest()
                    }
                }
            }
        }
    }

    override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
        routes.forEach {
            val tempPolyline = mapObjects.addPolyline(it.geometry)
            tempPolyline.strokeWidth = 0.5f
            tempPolyline.gradientLength = 1.0f
            userRoutePolylines.add(mapObjects.addPolyline(it.geometry))
        }
    }

    override fun onDrivingRoutesError(error: Error) {
        var errorMessage = getString(R.string.map_route_unknown_error_message)
        if (error is RemoteError) {
            errorMessage = getString(R.string.map_route_remote_error_message)
        } else if (error is NetworkError) {
            errorMessage = getString(R.string.map_route_network_error_message)
        }

        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun submitRouteRequest() {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        val mapPinBitmap =
            ImageProvider.fromBitmap(getBitmapFromDrawable(R.drawable.baseline_map_pin_24))

        if (isRouteShowingWithUser == false) {
            userRoutePolylines.forEach {
                mapObjects.remove(it)
            }
            userRouteObjects.forEach {
                mapObjects.remove(it)
            }
        }

        val points: ArrayList<RequestPoint> = ArrayList()
        if (isRouteShowingWithUser == true) {
            userLocationCoordinates?.let { location ->
                points.add(
                    RequestPoint(
                        Point(location.latitude, location.longitude),
                        RequestPointType.WAYPOINT,
                        null
                    )
                )
                moveCameraToPosition(
                    Point(location.latitude, location.longitude), constZoomCloseUp
                )
                routeToBeDisplayed.dropLast(1).forEach { routeItem ->
                    points.add(
                        RequestPoint(
                            Point(
                                routeItem.coordinateX,
                                routeItem.coordinateY
                            ),
                            RequestPointType.VIAPOINT,
                            null
                        )
                    )
                    userRouteObjects.add(
                        mapObjects.addPlacemark(
                            Point(
                                routeItem.coordinateX,
                                routeItem.coordinateY
                            ), mapPinBitmap
                        )
                    )
                    userRouteObjects.last().userData = routeItem
                }
                points.add(
                    RequestPoint(
                        Point(
                            routeToBeDisplayed.last().coordinateX,
                            routeToBeDisplayed.last().coordinateY
                        ),
                        RequestPointType.WAYPOINT,
                        null
                    )
                )
                userRouteObjects.add(
                    mapObjects.addPlacemark(
                        Point(
                            routeToBeDisplayed.last().coordinateX,
                            routeToBeDisplayed.last().coordinateY
                        ), mapPinBitmap
                    )
                )
                userRouteObjects.last().userData = routeToBeDisplayed.last()
            }
        } else if (isRouteShowingWithUser == false) {
            points.add(
                RequestPoint(
                    Point(
                        routeToBeDisplayed.first().coordinateX,
                        routeToBeDisplayed.first().coordinateY
                    ),
                    RequestPointType.WAYPOINT,
                    null
                )
            )
            moveCameraToPosition(
                Point(
                    routeToBeDisplayed.first().coordinateX,
                    routeToBeDisplayed.first().coordinateY
                ), constZoomCloseUp
            )
            userRouteObjects.add(
                mapObjects.addPlacemark(
                    Point(
                        routeToBeDisplayed.first().coordinateX,
                        routeToBeDisplayed.first().coordinateY
                    ), mapPinBitmap
                )
            )
            userRouteObjects.last().userData = routeToBeDisplayed.first()
            routeToBeDisplayed.drop(1).dropLast(1).forEach { routeItem ->
                points.add(
                    RequestPoint(
                        Point(
                            routeItem.coordinateX,
                            routeItem.coordinateY
                        ),
                        RequestPointType.WAYPOINT,
                        null
                    )
                )
                userRouteObjects.add(
                    mapObjects.addPlacemark(
                        Point(
                            routeItem.coordinateX,
                            routeItem.coordinateY
                        ), mapPinBitmap
                    )
                )
                userRouteObjects.last().userData = routeItem
            }
            points.add(
                RequestPoint(
                    Point(
                        routeToBeDisplayed.last().coordinateX,
                        routeToBeDisplayed.last().coordinateY
                    ),
                    RequestPointType.WAYPOINT,
                    null
                )
            )
            userRouteObjects.add(
                mapObjects.addPlacemark(
                    Point(
                        routeToBeDisplayed.last().coordinateX,
                        routeToBeDisplayed.last().coordinateY
                    ), mapPinBitmap
                )
            )
            userRouteObjects.last().userData = routeToBeDisplayed.last()
        }

        userRouteObjects.forEach {
            it.addTapListener(userMapObjectListener)
        }

        drivingSession = drivingRouter.requestRoutes(points, drivingOptions, vehicleOptions, this)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        if (isBeingTracked) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isBeingTracked) {
            stopLocationUpdates()
        }
    }
}
