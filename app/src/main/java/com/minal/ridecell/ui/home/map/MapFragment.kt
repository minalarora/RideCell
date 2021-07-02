package com.minal.ridecell.ui.home.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.minal.ridecell.R
import com.minal.ridecell.databinding.FragmentMapBinding
import com.minal.ridecell.extensions.createToast
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback, PermissionsListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var mapboxMap: MapboxMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireContext(), this.getString(R.string.mapbox));
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MapFragment.viewLifecycleOwner
            viewModel = ViewModelProvider(this@MapFragment).get(MapViewModel::class.java)

            // This contains the MapView in XML and needs to be called after the access token is configured.
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this@MapFragment);

        }

        return binding.root
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
            object : Style.OnStyleLoaded{
                override fun onStyleLoaded(style: Style) {
                    enableLocationComponent(style)
                }

            }
        )
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

// Get an instance of the component
            val locationComponent: LocationComponent = mapboxMap.getLocationComponent()

// Activate with options
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle).build()
            )

// Enable to make component visible
            locationComponent.isLocationComponentEnabled = true

// Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING

// Set the component's render mode
            locationComponent.renderMode = RenderMode.COMPASS
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(requireActivity())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String?>?) {
        context?.createToast("Need permission to access your current location.")
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap.getStyle(object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {
                    enableLocationComponent(style)
                }
            })
        } else {
            context?.createToast("Permission not granted! Unable to load Map.")
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}

