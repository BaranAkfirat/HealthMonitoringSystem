package com.example.healthmonitoringsystem

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.healthmonitoringsystem.databinding.ActivityRegisterBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.healthmonitoringsystem.R
import com.example.healthmonitoringsystem.MainPage
import com.example.healthmonitoringsystem.ui.home.HomeFragment
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import okhttp3.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import java.io.IOException


class Register : Fragment() {

        private lateinit var kullaniciAdi: EditText
        private lateinit var sifre: EditText
        private lateinit var registerButton: Button
        private val baseUrl = "http://192.168.118.25:5555"

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.activity_register, container, false)

            kullaniciAdi = rootView.findViewById(R.id.UsarnameReg)
            sifre = rootView.findViewById(R.id.PasswordReg)
            registerButton = rootView.findViewById(R.id.Register)

            registerButton.setOnClickListener {
                val username = kullaniciAdi.text.toString()
                val password = sifre.text.toString()

                addUser(username, password)
            }

            return rootView
        }

        private fun addUser(username: String, password: String) {
            val json = JSONObject()
            json.put("username", username)
            json.put("password", password)

            val requestBody = json.toString()
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(baseUrl + "add_data")
                .post(RequestBody.create("application/json".toMediaTypeOrNull(), requestBody))
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Error: " + e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    activity?.runOnUiThread {
                        Toast.makeText(context, responseBody, Toast.LENGTH_SHORT).show()
                        if (response.isSuccessful) {
                            // Başarılıysa HomeFragment'e geçiş yap
                            val fragmentManager = requireActivity().supportFragmentManager
                            fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, HomeFragment())
                                .commit()
                        }
                    }
                }

            })
        }
    }


