package com.devnow.student.presence

import android.annotation.SuppressLint
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private var locationManager : LocationManager? = null

private val locationPermissionCode = 2

private var longitude: Double = 0.0
private var latitude: Double = 0.0

val data = Date()
var subject = ""

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)


        val formataData = SimpleDateFormat("dd/MM/yyyy")

        val dataFormatada: String = formataData.format(data)
        val day = getWeek(dataFormatada.toString());

        val txt: TextView = findViewById(R.id.textSubject) as TextView
        val txtDate: TextView = findViewById(R.id.textDate) as TextView
        val txtInfo: TextView = findViewById(R.id.textInfo) as TextView
        val txtLa: TextView = findViewById(R.id.textLocationLatitude) as TextView
        val txtLo: TextView = findViewById(R.id.textLocationLongitude) as TextView
        val btn: Button = findViewById(R.id.button) as Button;

        subject = "NENHUMA AULA NESSE DIA"
        var off = 1;

        when (this.getWeek(dataFormatada.toString())) {
            "SEG" -> subject = "Aula do dia: LINGUAGENS FORMAIS E AUTÔNOMAS"
            "TER" -> subject = "Aula do dia: TRABALHO DE GRADUAÇÃO INTERDICIPLINAR"
            "QUAR" -> subject = "Aula do dia: PROGRAMAÇÃO PARA DISPOSITIVOS MOVEIS"
            else -> {
                off = 0;
            }
        }

        if(txtLa.text == "-23.536286105990403" && txtLo.text == "-46.560337171952156") {
            off = 1;
        }else {
            off = 0;
        }

        off = 1;
        if(off == 0) {
            btn.isInvisible = true;
            txtInfo.setText("Você não pode registrar presença hoje volte outro dia")
        }

        txt.setText(subject)
        txtDate.setText(data.toString())

    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val txtLa: TextView = findViewById(R.id.textLocationLatitude) as TextView
            val txtLo: TextView = findViewById(R.id.textLocationLongitude) as TextView

            txtLo.setText(location.longitude.toString())
            txtLa.setText(location.latitude.toString())
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
                Calendar.WEDNESDAY -> dayWeek = "QUA"
                Calendar.THURSDAY -> dayWeek = "QUI"
                Calendar.FRIDAY -> dayWeek = "SEX"
                Calendar.SATURDAY -> dayWeek = "SAB"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dayWeek
    }

    fun handleSubmit(view: View) {
        val dataMatter = ArrayList<String>();
        val dataDay = ArrayList<String>();

        dataMatter.add(subject);
        dataDay.add(data.toString());

        val success = Intent(this, success::class.java)
        startActivity(success);
    }
}