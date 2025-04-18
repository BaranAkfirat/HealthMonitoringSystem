package com.example.healthmonitoringsystem

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Button
import android.widget.Toast
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.view.KeyEvent
import android.widget.TextView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class HeartRate : Fragment() {
    private val client = OkHttpClient()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_heart_rate, container, false)
            val addButton: ImageButton = rootView.findViewById(R.id.add)
            val heartRateListLayout: LinearLayout = rootView.findViewById(R.id.Heartrating)

            val existingHeartRates = getHeartRatesFromServer()

            // Add existing heart rate names to the layout
            existingHeartRates.forEach { heartRateName ->
                val heartRateTextView = TextView(context)
                heartRateTextView.text = heartRateName
                heartRateTextView.textSize = 18f
                heartRateListLayout.addView(heartRateTextView)
            }

            addButton.setOnClickListener {
                val newEditText = EditText(context)
                newEditText.hint = "Enter Your Last Heart Rate"
                newEditText.textSize = 18f
                newEditText.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                        val heartRateName = newEditText.text.toString()
                        if (heartRateName.isNotBlank()) {
                            addHeartRateToServer(heartRateName)
                            val heartRateTextView = TextView(context)
                            heartRateTextView.text = heartRateName
                            heartRateTextView.textSize = 18f
                            heartRateListLayout.addView(heartRateTextView)
                            heartRateListLayout.removeView(newEditText)
                        }
                        true
                    } else {
                        false
                    }
                }
                heartRateListLayout.addView(newEditText)
            }
            val backButton: ImageButton = rootView.findViewById(R.id.Back)

            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            return rootView
        }

        private fun getHeartRatesFromServer(): List<String> {
            // Make a request to the server to get existing heart rate names
            // For now, return a static list
            return listOf("Heart Rate 1", "Heart Rate 2", "Heart Rate 3")
        }

        private fun addHeartRateToServer(heartRateName: String) {
            val json = JSONObject()
            json.put("heart_name", heartRateName)
            val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("http://192.168.144.50:5555/add_heart")  // Replace YOUR_SERVER_IP with your actual server IP
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Failed to add heart rate", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Heart rate added successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Failed to add heart rate", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
