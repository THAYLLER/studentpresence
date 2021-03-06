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
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.squareup.okhttp.internal.Platform
import java.util.Calendar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private var locationManager : LocationManager? = null

private val locationPermissionCode = 2
var off = 1
val dist = FloatArray(1)
val data =  Calendar.getInstance().getTime();
var subject = ""
var msg = ""
var la = 0.0

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)


        val formataData = SimpleDateFormat("dd/MM/yyyy")
        val formataDataHora  = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val hour = SimpleDateFormat("HH:mm:ss")

        val dataFormatada: String = formataData.format(data)
        val horaFormatada: String = hour.format(data)
        val day = getWeek(dataFormatada.toString());

        val txt: TextView = findViewById(R.id.textSubject) as TextView
        val txtDate: TextView = findViewById(R.id.textDate) as TextView
        val btn: Button = findViewById(R.id.button) as Button;
        val txtLa: TextView = findViewById(R.id.textLa) as TextView
        val txtLo: TextView = findViewById(R.id.textLo) as TextView

        subject = "NENHUMA AULA NESSE DIA"


        // valida????o dos dias da semana
        // Nessa Valida????o ?? verificado se voc?? ta no dia da aula
        when (this.getWeek(dataFormatada.toString())) {
            "SEGUNDA" -> subject = "LINGUAGENS FORMAIS E AUT??MATOS"
            "TER??A" -> subject = "TRABALHO DE GRADUA????O INTERDISCIPLINAR I"
            "QUARTA" -> subject = "PROGRAMA????O PARA DISPOSITIVOS M??VEIS"
            else -> {
                off = 0;
                msg = "S?? ?? poss??vel marcar presen??a no dia da aula\n"

            }
        }

        if(off == 1) {
            //Valida????o de horario
            //Nessa valida????o  ?? verificado se ta dentro do horario da aula
            var dMin = hour.parse("19:10:00")
            var dMax = hour.parse("20:35:00")
//            if(horaFormatada >= hour.format(dMin).toString() || horaFormatada >= hour.format(dMax).toString() ) {
//                off = 1;
//            } else {
//                off = 0;
//                msg = "S?? ?? poss??vel marcar presen??a no hor??rio da aula\n"
//            }
        }







        txt.setText(subject)
        txtDate.setText(this.getWeek(dataFormatada.toString()) + " " +formataDataHora.format(data).toString())

    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            val txtLa: TextView = findViewById(R.id.textLa) as TextView
            val txtLo: TextView = findViewById(R.id.textLo) as TextView
            txtLa.setText("4070444666666666")
            txtLo.setText("-73444666666666")

//            txtLa.setText(location.latitude.toString())
//            txtLo.setText(location.longitude.toString())
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
                Calendar.TUESDAY -> dayWeek = "TER??A"
                Calendar.WEDNESDAY -> dayWeek = "QUARTA"
                Calendar.THURSDAY -> dayWeek = "QUINTA"
                Calendar.FRIDAY -> dayWeek = "SEXTA"
                Calendar.SATURDAY -> dayWeek = "S??BADO"
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

        val txtLa: TextView = findViewById(R.id.textLa) as TextView
        val txtLo: TextView = findViewById(R.id.textLo) as TextView

        val la = txtLa.text.toString().toDoubleOrNull()
        val lo =  txtLo.text.toString().toDoubleOrNull()

        if (lo != null && la != null) {
            Location.distanceBetween(
                    la,
                    lo,
                    -23.536286105990403,
                    -46.560337171952156,
                    dist)
        }

        if(off == 1) {
            //valida????o de localiza????o
            //Aqui ?? verificado se ta proximo da localiza????o da faculdade
            //if(dist[0].toDouble() == (1000000).toDouble()) {
                if (dist[0] / 1000 > 1) {

                    off = 1;
                }else {
                    off = 0;
                    msg = "N??o ?? poss??vel marcar presen??a fora da localiza????o da Unicid"
                }
//            }else {
//                off = 0;
//                msg = "N??o ?? poss??vel marcar presen??a fora da localiza????o da Unicid"
//            }
        }

        if(off != 0) {
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