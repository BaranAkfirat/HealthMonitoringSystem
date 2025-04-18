package com.example.healthmonitoringsystem.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.healthmonitoringsystem.R
import com.example.healthmonitoringsystem.MainPage
import com.example.healthmonitoringsystem.Register
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.healthmonitoringsystem.databinding.ActivityRegisterBinding
import android.widget.Toast
import com.example.healthmonitoringsystem.ui.home.HomeFragment
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import okhttp3.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response


class HomeFragment : Fragment() {

        private lateinit var kullaniciAdi: EditText
        private lateinit var sifre: EditText
        private lateinit var GirisYap: Button
        private lateinit var KayitOl: Button
        private val baseUrl = "http://192.168.144.50:5555"

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_home, container, false)

            kullaniciAdi = rootView.findViewById(R.id.UsarnameReg)
            sifre = rootView.findViewById(R.id.PasswordReg)
            GirisYap = rootView.findViewById(R.id.GirisYap)
            KayitOl = rootView.findViewById(R.id.Register)

            GirisYap.setOnClickListener {
                val username = kullaniciAdi.text.toString()
                val password = sifre.text.toString()

                loginUser(username, password)
            }

            KayitOl.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, Register())
                    .addToBackStack(null)
                    .commit()
            }

            return rootView
        }

        private fun loginUser(username: String, password: String) {
            val json = JSONObject()
            json.put("username", username)
            json.put("password", password)

            val requestBody = json.toString()
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(baseUrl + "/login")
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
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            val message = jsonResponse.getString("message")
                            if (message == "Login successful") {
                                // Başarılıysa MainPage'e geçiş yapabilirsiniz
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, MainPage())
                                    .addToBackStack(null)
                                    .commit()
                            } else {
                                Toast.makeText(context, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Invalid response from server", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
