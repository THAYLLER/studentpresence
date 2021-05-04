package com.devnow.student.presence

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import org.jetbrains.anko.doAsync

class signin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
    }

    fun handleSubmit(view: View) {
        val password: TextInputEditText = findViewById(R.id.password) as TextInputEditText
        val username: TextInputEditText = findViewById(R.id.username) as TextInputEditText

        val user =  arrayOf("19425864", "19329792", "19229704", "19161662", "18815545", "19652135");
        val pass =  arrayOf("123", "1234", "12345");
        var ok: Boolean = false;

        for(i in user.indices) {
            print(user[i]);
            if(username.text.toString() == user[i].toString()) {
                for(j in pass.indices) {
                    if(password.text.toString() == pass[j].toString()) {
                        ok = true;
                        break;
                    } else {
                        ok = false;
                    }
                }
                break;
            } else {
                ok = false;
            }
        }



        if(ok) {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main);
        }
    }
}