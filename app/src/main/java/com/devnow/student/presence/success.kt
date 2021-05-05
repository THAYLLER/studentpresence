package com.devnow.student.presence

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private var locationManager : LocationManager? = null

private val locationPermissionCode = 2

private var longitude = ""
private var latitude = ""

class success : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)

        val data = Calendar.getInstance().getTime()
        val formataData = SimpleDateFormat("dd/MM/yyyy")

        val dataFormatada: String = formataData.format(data)
        val day = getWeek(dataFormatada.toString());


        val textData: TextView = findViewById(R.id.textData) as TextView
        val textMater: TextView = findViewById(R.id.textMater) as TextView

        val btn: Button = findViewById(R.id.close) as Button;

        var subject = "NENHUMA AULA NESSE DIA"


        when (this.getWeek(dataFormatada.toString())) {
            "SEG" -> subject = "LINGUAGENS FORMAIS E AUTÔNOMAS"
            "TER" -> subject = "TRABALHO DE GRADUAÇÃO INTERDICIPLINAR"
            "QUAR" -> subject = "PROGRAMAÇÃO PARA DISPOSITIVOS MOVEIS"

        }

        textData.setText(dataFormatada)
        textMater.setText(subject)

    }

    fun handleSubmit(view: View) {
        finish()
        finishAffinity()
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val textLocation: TextView = findViewById(R.id.textLocation) as TextView
            textLocation.setText("Latitude:" + location.latitude.toString() + " Logintude: "+ location.longitude.toString())
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun getWeek(date: String?): String? { //ex 07/03/2017
        var dayWeek = "---"
        val gc = GregorianCalendar()
        try {
            gc.time = SimpleDateFormat("dd/MM/yyyy").parse(date)
            when (gc[Calendar.DAY_OF_WEEK]) {
                Calendar.SUNDAY -> dayWeek = "DOM"
                Calendar.MONDAY -> dayWeek = "SEG"
                Calendar.TUESDAY -> dayWeek = "TER"
                Calendar.WEDNESDAY -> dayWeek = "QUAR"
                Calendar.THURSDAY -> dayWeek = "QUI"
                Calendar.FRIDAY -> dayWeek = "SEX"
                Calendar.SATURDAY -> dayWeek = "SAB"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dayWeek
    }
}