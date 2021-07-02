//package com.minal.ridecell.ui.home.map
//
//import android.Manifest
//import android.content.Context
//import android.content.IntentSender.SendIntentException
//import android.content.pm.PackageManager
//import android.location.LocationManager
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.Animation
//import android.view.animation.LinearInterpolator
//import android.view.animation.RotateAnimation
//import androidx.core.app.ActivityCompat
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.common.api.ResolvableApiException
//import com.google.android.gms.location.*
//import com.google.android.gms.maps.*
//import com.google.android.gms.maps.GoogleMap.CancelableCallback
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import com.minal.ridecell.R
//import com.minal.ridecell.databinding.FragmentMapBinding
//import com.minal.ridecell.extensions.putData
//import com.nabinbhandari.android.permissions.PermissionHandler
//import com.nabinbhandari.android.permissions.Permissions
//import java.util.*
//
//
//class MapFragment : Fragment(), OnMapReadyCallback {
//
//private lateinit var binding: FragmentMapBinding
//private lateinit var mGoogleMap: GoogleMap
//private var LatituteOfTajMahal: Double? = null
//private var LongitudeOfTajMahal: Double? = null
//private var mLocationCallBack: LocationCallback? = null
//private var mSettingsClient: SettingsClient? = null
//private var mLocationSettingsRequest: LocationSettingsRequest? = null
//private var mLocationClient: FusedLocationProviderClient? = null
//private val REQUEST_CHECK_SETTINGS = 214
//private val REQUEST_ENABLE_GPS = 516
//private var mylongitude = 0.0
//private var mylatitude = 0.0
//private var isStart = true
//private val isFirstTime = true
//private var locationRequest: LocationRequest? = null
//private val handler: Handler? = null
//private var areaname: String? = null
//private var marker: Marker? = null
//
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        }
//
//        override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//        ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentMapBinding.inflate(inflater, container, false).apply {
//        lifecycleOwner = this@MapFragment.viewLifecycleOwner
//            viewModel = ViewModelProvider(this@MapFragment).get(MapViewModel::class.java)
//
//
//        binding.refreshButton.setOnClickListener {
//        if (mylatitude != 0.0 && mylongitude != 0.0)
//        goToLocationWithAnimation(mylatitude, mylongitude, 12)
//
//        val anim = RotateAnimation(
//        0f,
//        360f,
//        Animation.RELATIVE_TO_SELF,
//        0.5f,
//        Animation.RELATIVE_TO_SELF,
//        0.5f
//        )
//        anim.interpolator = LinearInterpolator()
//        anim.repeatCount = Animation.INFINITE
//        anim.duration = 800
//
//        // Start animating the image
//        binding.refreshIcon.startAnimation(anim)
//        Handler(Looper.getMainLooper()).postDelayed({ //stop animation
//        binding.refreshIcon.setAnimation(null)
//        }, 2500)
//        //TODO VISIBILITY ON
//        mGoogleMap.clear()
//        }
//
//        val fragment: SupportMapFragment = requireActivity().supportFragmentManager
//        .findFragmentById(R.id.map) as SupportMapFragment
//        fragment.getMapAsync(this@MapFragment::onMapReady)
//
//        mLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        myLocation.setOnClickListener {
//        getNearmeArea(mylatitude, mylatitude)
//        val manager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        if (permissionIsGranted() && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//        if (mylatitude != 0.0 && mylongitude != 0.0) goToLocationWithAnimation(
//        mylatitude,
//        mylongitude,
//        9
//        )
//        } else {
//        if (mylatitude != 0.0 && mylongitude != 0.0) goToLocationWithAnimation(
//        mylatitude,
//        mylongitude,
//        9
//        )
//
//        mLocationClient?.removeLocationUpdates(mLocationCallBack)
//        locationRequest = null
//        getCurrentLocation()
//        }
//
//        }
//
//
//        mLocationCallBack = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//        super.onLocationResult(locationResult)
//        val lastLocation = locationResult.lastLocation
//        mylatitude = lastLocation.latitude
//        mylongitude = lastLocation.longitude
////                    val sharedPreferences: SharedPreferences =
////                        getSharedPreferences("currentLocation", Context.MODE_PRIVATE)
////                    val edit = sharedPreferences.edit()
////                    edit.putString("lat", Mylatitude.toString())
////                    edit.putString("long", Mylongitude.toString())
////                    edit.apply()
//        requireContext().putData("lat",mylatitude.toString())
//        requireContext().putData("long",mylongitude.toString())
//        val latLng = LatLng(mylatitude, mylongitude)
//        if (marker != null) marker?.remove()
//        val markerOptions = MarkerOptions()
//        markerOptions.position(latLng)
//        markerOptions.title("My Location")
//        marker = mGoogleMap.addMarker(markerOptions)
//        if (isStart) {
//        getoLocation(mylatitude, mylongitude)
//        isStart = false
//        }
//        }
//        }
//
//        getCurrentLocation()
//
//
//        }
//
//        return binding.root
//        }
//
//private fun goToLocationWithAnimation(latitude: Double, longitude: Double, i: Int) {
//        var i = i
//        i = 7
//        val latLng = LatLng(latitude, longitude)
//        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, i.toFloat())
//        mGoogleMap.animateCamera(cameraUpdateFactory, 500, object : CancelableCallback {
//        override fun onFinish() {}
//        override fun onCancel() {}
//        })
//        }
//
//        override fun onMapReady(googleMap: GoogleMap) {
//        mGoogleMap = googleMap
//        getoLocation(LatituteOfTajMahal, LongitudeOfTajMahal)
//        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//        mGoogleMap.uiSettings.isMapToolbarEnabled = false
//
//        }
//
//private fun getoLocation(latituteOfTajMahal: Double, longitudeOfTajMahal: Double) {
//        val latLng = LatLng(latituteOfTajMahal, longitudeOfTajMahal)
//        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 9f)
//        mGoogleMap.moveCamera(cameraUpdateFactory)
//        }
//
//private fun getNearmeArea(latitude: Double?, longitude: Double?) {
//
//        }
//
//        fun permissionIsGranted(): Boolean {
//        val permissionState = ActivityCompat.checkSelfPermission(
//        requireContext(),
//        Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        return permissionState == PackageManager.PERMISSION_GRANTED
//        }
//
//private fun getCurrentLocation() {
//        if (permissionIsGranted()) {
//        openDialogOfSettingGps()
//        } else {
//        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
//        val rationale = "Please provide location permission so that you can ..."
//        val options = Permissions.Options()
//        .setRationaleDialogTitle("Info")
//        .setSettingsDialogTitle("Warning")
//        Permissions.check(
//        this /*context*/,
//        permissions,
//        rationale,
//        options,
//        object : PermissionHandler() {
//        override fun onGranted() {
//        openDialogOfSettingGps()
//        }
//
//        override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
//        // permission denied, block the feature.
//        }
//        })
//        }
//        }
//
//private fun openDialogOfSettingGps() {
//        val builder = LocationSettingsRequest.Builder()
//        builder.addLocationRequest(LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
//        builder.setAlwaysShow(true)
//        mLocationSettingsRequest = builder.build()
//        mSettingsClient = LocationServices.getSettingsClient(requireActivity())
//        mSettingsClient?.checkLocationSettings(mLocationSettingsRequest)
//        .addOnSuccessListener { doLocationWork() }
//        .addOnFailureListener { e ->
//        val statusCode = (e as ApiException).statusCode
//        when (statusCode) {
//        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
//        val rae = e as ResolvableApiException
//        rae.startResolutionForResult(
//        requireActivity(),
//        REQUEST_CHECK_SETTINGS
//        )
//        } catch (sie: SendIntentException) {
//        Log.e("GPS", "Unable to execute request.")
//        }
//        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.e(
//        "GPS",
//        "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
//        )
//        }
//        }
//        .addOnCanceledListener {  }
//        }
//
//private fun doLocationWork() {
//        if (ActivityCompat.checkSelfPermission(
//        requireContext(),
//        Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//        requireContext(),
//        Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//        ) {
//        return
//        }
//        mGoogleMap.isMyLocationEnabled = true
//        mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
//        if (locationRequest == null) {
//        isStart = true
//        locationRequest = LocationRequest.create()
//        locationRequest?.setInterval(120000) // two minute interval
//        locationRequest?.setFastestInterval(10000)
//        locationRequest?.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
//        mLocationClient?.requestLocationUpdates(locationRequest, mLocationCallBack, null)
//        }
//        }
//
//
//        }