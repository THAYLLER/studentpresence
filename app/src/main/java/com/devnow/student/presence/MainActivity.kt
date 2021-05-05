package com.devnow.student.presence

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private var locationManager : LocationManager? = null

private val locationPermissionCode = 2
var off = 1
val dist = FloatArray(1)
val data = Date()
var subject = ""
var msg = ""

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode)
        }
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener)


        val formataData = SimpleDateFormat("dd/MM/yyyy")
        val formataDataHora  = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val hour = SimpleDateFormat("HH:mm:ss")

        val dataFormatada: String = formataData.format(data)
        val horaFormatada: String = hour.format(data)
        val day = getWeek(dataFormatada.toString());

        val txt: TextView = findViewById(R.id.textSubject) as TextView
        val txtDate: TextView = findViewById(R.id.textDate) as TextView
        val btn: Button = findViewById(R.id.button) as Button;

        subject = "NENHUMA AULA NESSE DIA"


        // validação dos dias da semana
        // Nessa Validação é verificado se você ta no dia da aula
        when (this.getWeek(dataFormatada.toString())) {
            "SEGUNDA" -> subject = "LINGUAGENS FORMAIS E AUTÔMATOS"
            "TERÇA" -> subject = "TRABALHO DE GRADUAÇÃO INTERDISCIPLINAR I"
            "QUARTA" -> subject = "PROGRAMAÇÃO PARA DISPOSITIVOS MÒVEIS"
            else -> {
                off = 0;
                msg = "Só é possível marcar presença no dia da aula\n"

            }
        }

        //Validação de horario
        //Nessa validação  é verificado se ta dentro do horario da aula
        if(horaFormatada.toString() == "19:10:00" || horaFormatada.toString() == "20:35:00") {
            off = 1;
        } else {
            off = 0;
            msg = "Só é possível marcar presença no horário da aula\n"
        }

        //validação de localização
        //Aqui é verificado se ta proximo da localização da faculdade
//        if(dist[0].toDouble() == (1000000).toDouble()) {
//            if (dist[0] / 1000 > 1) {
//
//                off = 1;
//            }else {
//                off = 0;
//                msg = "Não é possível marcar presença fora da localização da Unicid"
//            }
//        }else {
//            off = 0;
//            msg = "Não é possível marcar presença fora da localização da Unicid"
//        }




        txt.setText(subject)
        txtDate.setText(this.getWeek(dataFormatada.toString()) + " " +formataDataHora.format(data).toString())

    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {


            Location.distanceBetween(
                location.latitude,
                location.longitude,
                -23.536286105990403,
                -46.560337171952156,
                dist)
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
                Calendar.SUNDAY -> dayWeek = "DOMINGO"
                Calendar.MONDAY -> dayWeek = "SEGUINDA"
                Calendar.TUESDAY -> dayWeek = "TERÇA"
                Calendar.WEDNESDAY -> dayWeek = "QUARTA"
                Calendar.THURSDAY -> dayWeek = "QUINTA"
                Calendar.FRIDAY -> dayWeek = "SEXTA"
                Calendar.SATURDAY -> dayWeek = "SÁBADO"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dayWeek
    }

    fun handleSubmit(view: View) {
        val dataMatter = ArrayList<String>();
        val dataDay = ArrayList<String>();
        val txtInfo: TextView = findViewById(R.id.textInfo) as TextView

        if(off == 1) {
            dataMatter.add(subject);
            dataDay.add(data.toString());
            val success = Intent(this, success::class.java)
            startActivity(success);
        } else //dispara a mensagem de erro
            if(off == 0) {
                txtInfo.setText(msg)
            }
    }
}