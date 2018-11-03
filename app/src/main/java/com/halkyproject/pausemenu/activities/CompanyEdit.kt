package com.halkyproject.pausemenu.activities

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.common.base.Enums
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.enum.Country
import com.halkyproject.pausemenu.model.Company
import com.halkyproject.pausemenu.singletons.CompanyService
import com.halkyproject.pausemenu.singletons.FormatSingleton
import kotlinx.android.synthetic.main.activity_company_edit.*
import java.util.*

class CompanyEdit : AppCompatActivity() {
    private var locationValue: Marker? = null
    private var addressValue: Address? = null
    private var editingCompany: Company? = null

    companion object {
        val KEY_EDIT_ID = "EditItemId";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_edit)

        companyDocument.addTextChangedListener(FormatSingleton.mask(companyDocument, FormatSingleton.FORMAT_CNPJ))
        val editId: Int = intent?.extras?.getInt(KEY_EDIT_ID) ?: -1
        val map = (supportFragmentManager.findFragmentById(R.id.company_mapView) as SupportMapFragment)

        if (editId != -1) {
            editingCompany = CompanyService.findById(editId)
            if (editingCompany != null) {
                companyCommonName.setText(editingCompany!!.mainName, TextView.BufferType.EDITABLE)
                companyRealName.setText(editingCompany!!.realName, TextView.BufferType.EDITABLE)
                companyDocument.setText(editingCompany!!.documentNumber, TextView.BufferType.EDITABLE)
            }

            deleteButton.visibility = View.VISIBLE;
        } else {
            deleteButton.visibility = View.GONE;
        }
        map.getMapAsync { googleMap ->
            val mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (editingCompany != null) {
                setMarker(editingCompany!!.latitude!!, editingCompany!!.longitude!!, googleMap)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(editingCompany?.latitude
                        ?: 0.0, editingCompany?.longitude ?: 0.0), 18f))
            } else {
                val currentLoc: Location? = if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    } else {
                        null
                    }
                } else {
                    mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLoc?.latitude
                        ?: 0.0, currentLoc?.longitude ?: 0.0), 13f))
            }
            googleMap.setOnMapLongClickListener { latLng ->
                val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(200);
                }
                setMarker(latLng.latitude, latLng.longitude, googleMap)
            }
        }
    }

    private fun setMarker(latitude: Double, longitude: Double, googleMap: GoogleMap) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.size > 0) {
                addressValue = addresses[0]
                addressString.text = addressValue!!.getAddressLine(0)
                if (locationValue == null) {

                    // Marker was not set yet. Add marker:
                    locationValue = googleMap.addMarker(MarkerOptions()
                            .position(LatLng(addressValue!!.latitude, addressValue!!.longitude))
                            .title("Localização"))
                } else {
                    // Marker already exists, just update it's position
                    locationValue!!.position = LatLng(addressValue!!.latitude, addressValue!!.longitude)
                }
            }
        } catch (e: Exception) {

        }
    }

    fun saveCompany(v: View) {
        val commonName = companyCommonName.text.toString()
        val realName = companyRealName.text.toString()
        val docNum = FormatSingleton.unmask(companyDocument.text.toString())
        if (commonName.length < 2) {
            Toast.makeText(applicationContext, "Nome comum muito curto!", Toast.LENGTH_SHORT).show()
            return
        }
        if (realName.length < 2) {
            Toast.makeText(applicationContext, "Nome real muito curto!", Toast.LENGTH_SHORT).show()
            return
        }
        if (docNum.length != 14) {
            Toast.makeText(applicationContext, "Documento inválido!", Toast.LENGTH_SHORT).show()
            return
        }
        if (addressValue == null) {
            Toast.makeText(applicationContext, "Selecione a localização no mapa!", Toast.LENGTH_SHORT).show()
            return
        }
        val country = Enums.getIfPresent(Country::class.java, addressValue!!.countryCode)
        if (!country.isPresent) {
            Toast.makeText(applicationContext, "Local inválido!", Toast.LENGTH_SHORT).show()
        }
        m_loadingFrame.visibility = View.VISIBLE

        try {
            val companyObj: Company = editingCompany ?: Company()
            companyObj.documentNumber = docNum
            companyObj.mainName = commonName
            companyObj.realName = realName
            companyObj.latitude = addressValue!!.latitude
            companyObj.longitude = addressValue!!.longitude
            companyObj.cityDisplayName = addressValue!!.locality
            companyObj.country = country.get().acronym
            if (companyObj.id == null) {
                CompanyService.insertCompany(companyObj)
            } else {
                CompanyService.updateCompany(companyObj)
            }
            Toast.makeText(applicationContext, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Erro ao salvar: " + e.message, Toast.LENGTH_SHORT).show()
            Log.e("ERROR", "Erro salvando companhia", e)
            m_loadingFrame.visibility = View.GONE
        }
    }

    fun confirmDelete(v: View) {
        linearLayout4.visibility = View.VISIBLE;
        titleLbl.text = getString(R.string.all_confirmDelete)
    }

    fun cancelDelete(v: View) {
        linearLayout4.visibility = View.GONE;
        titleLbl.text = getString(R.string.company_editTitle)
    }

    fun doDelete(v: View) {
        m_loadingFrame.visibility = View.VISIBLE
        linearLayout4.visibility = View.GONE
        titleLbl.text = getString(R.string.company_editTitle)
        try {
            if (editingCompany!!.id != null) {
                CompanyService.deleteCompany(editingCompany!!)
            }
            Toast.makeText(applicationContext, "Apagado com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } catch (e: Exception) {
            m_loadingFrame.visibility = View.GONE
        }
    }
}
