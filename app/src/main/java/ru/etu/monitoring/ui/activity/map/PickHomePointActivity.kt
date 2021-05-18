package ru.etu.monitoring.ui.activity.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import ru.etu.monitoring.R
import ru.etu.monitoring.databinding.ActivityMapBinding
import ru.etu.monitoring.model.event.UpdateHomePointEvent
import ru.etu.monitoring.model.interactor.EventInteractor
import ru.etu.monitoring.utils.helpers.click

class PickHomePointActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btSave.click {
            val pos = mMap.cameraPosition.target
            EventInteractor.publishEvent(UpdateHomePointEvent(pos))
            finish()
        }

        binding.btBack.click {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val lastPos = intent.getParcelableExtra(ARG_LAT_LNG) ?: LatLng(59.971493714024106, 30.320338170076774)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPos, 16f))
    }

    companion object {
        private const val ARG_LAT_LNG = "lat_lng arg"

        fun getIntent(context: Context, lastPos: LatLng?): Intent {
            return Intent(context, PickHomePointActivity::class.java).apply {
                putExtra(ARG_LAT_LNG, lastPos)
            }
        }
    }
}