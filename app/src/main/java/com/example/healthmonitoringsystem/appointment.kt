package com.example.healthmonitoringsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class appointment : Fragment() {
        private val client = OkHttpClient()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_appointment, container, false)
            val addButton: ImageButton = rootView.findViewById(R.id.add)
            val pillListLayout: LinearLayout = rootView.findViewById(R.id.appointmenting)

            val existingPills = getPillsFromServer()

            // Add existing appointment names to the layout
            existingPills.forEach { appointmentName ->
                val appointmentTextView = TextView(context)
                appointmentTextView.text = appointmentName
                appointmentTextView.textSize = 18f
                pillListLayout.addView(appointmentTextView)
            }

            addButton.setOnClickListener {
                val newEditText = EditText(context)
                newEditText.hint = "New Appointment Date/Name"
                newEditText.textSize = 18f
                newEditText.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                        val appointmentName = newEditText.text.toString()
                        if (appointmentName.isNotBlank()) {
                            addAppointmentToServer(appointmentName)
                            val pillTextView = TextView(context)
                            pillTextView.text = appointmentName
                            pillTextView.textSize = 18f
                            pillListLayout.addView(pillTextView)
                            pillListLayout.removeView(newEditText)
                        }
                        true
                    } else {
                        false
                    }
                }
                pillListLayout.addView(newEditText)
            }
            val backButton: ImageButton = rootView.findViewById(R.id.Back)

            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            return rootView
        }

        private fun getPillsFromServer(): List<String> {
            // Make a request to the server to get existing pill names
            // For now, return a static list
            return listOf("Appointment 1", "Appointment 2", "Appointment 3")
        }

        private fun addAppointmentToServer(appointmentname: String) {
            val json = JSONObject()
            json.put("appointment_name", appointmentname)
            val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("http://192.168.144.50:5555/add_appointment")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Failed to add appointment", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Appointment added successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Failed to add appointment", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
