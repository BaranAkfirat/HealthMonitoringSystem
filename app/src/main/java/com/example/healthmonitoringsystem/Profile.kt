package com.example.healthmonitoringsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Profile : Fragment() {

    private lateinit var nameTextView: TextView
    private val baseUrl = "http://192.168.144.50:5555"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        nameTextView = rootView.findViewById(R.id.Name)

        // Giriş yapan kullanıcının adını çek
        val username = "baran" // Bunu giriş yapan kullanıcının adıyla değiştirin
        fetchProfileData(username)

        return rootView
    }

    private fun fetchProfileData(username: String) {
        val client = OkHttpClient()
        val url = "$baseUrl/get_profile?username=$username"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    nameTextView.text = "Error: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                activity?.runOnUiThread {
                    try {
                        val json = JSONObject(responseBody)
                        val name = json.getString("username")
                        nameTextView.text = name
                    } catch (e: Exception) {
                        nameTextView.text = "Error: ${e.message}"
                    }
                }
            }
        })
    }
}