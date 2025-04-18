package com.example.healthmonitoringsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import android.widget.Button
import okhttp3.*
import org.json.JSONObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull as okhttp3MediaTypeToMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody as okhttp3RequestBodyToRequestBody

class Sleep : Fragment() {
        private val client = OkHttpClient()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_sleep, container, false)
            val addButton: ImageButton = rootView.findViewById(R.id.add)
            val sleepListLayout: LinearLayout = rootView.findViewById(R.id.sleeptime)
            val sleepTextView: TextView = rootView.findViewById(R.id.Sleeping)

            val existingSleepTimes = getSleepTimesFromServer()

            // Add existing sleep times to the layout and text view
            existingSleepTimes.forEach { sleepTime ->
                val sleepTimeEditText = EditText(context)
                sleepTimeEditText.hint = "Sleeping Time"
                sleepTimeEditText.textSize = 18f
                sleepTimeEditText.setText(sleepTime)
                sleepTimeEditText.isEnabled = false
                sleepListLayout.addView(sleepTimeEditText)

                // Append sleep time to the text view
                sleepTextView.append("$sleepTime\n")
            }

            addButton.setOnClickListener {
                val newEditText = EditText(context)
                newEditText.hint = "Enter Your Sleeping Time"
                newEditText.textSize = 18f
                newEditText.setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                        val sleepTime = newEditText.text.toString()
                        if (sleepTime.isNotBlank()) {
                            addSleepTimeToServer(sleepTime)
                            val sleepTimeEditText = EditText(context)
                            sleepTimeEditText.hint = "Sleeping Time"
                            sleepTimeEditText.textSize = 18f
                            sleepTimeEditText.setText(sleepTime)
                            sleepTimeEditText.isEnabled = false
                            sleepListLayout.addView(sleepTimeEditText)

                            // Append new sleep time to the text view
                            sleepTextView.append("$sleepTime\n")

                            sleepListLayout.removeView(newEditText)
                        }
                        true
                    } else {
                        false
                    }
                }
                sleepListLayout.addView(newEditText)
            }

            val backButton: ImageButton = rootView.findViewById(R.id.Back)

            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            return rootView
        }

        private fun getSleepTimesFromServer(): List<String> {
            // Make a request to the server to get existing sleep times
            // For now, return a static list
            return listOf("Sleep Time 1", "Sleep Time 2", "Sleep Time 3")
        }

        private fun addSleepTimeToServer(sleepTime: String) {
            val json = JSONObject()
            json.put("sleep_name", sleepTime)
            val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("http://192.168.144.50:5555/add_sleep")  // Replace YOUR_SERVER_IP with your actual server IP
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Failed to add sleep time", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Sleep time added successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Failed to add sleep time", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

