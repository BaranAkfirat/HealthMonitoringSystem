package com.example.healthmonitoringsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.EditText
import android.view.KeyEvent
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class Pill : Fragment() {
        private val client = OkHttpClient()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_pill, container, false)

            val addButton: ImageButton = rootView.findViewById(R.id.add)
            val pillListLayout: LinearLayout = rootView.findViewById(R.id.pill_list_layout)

            // Existing pill names
            val existingPills = getPillsFromServer()

            // Add existing pill names to the layout
            existingPills.forEach { pillName ->
                val pillTextView = TextView(context)
                pillTextView.text = pillName
                pillTextView.textSize = 18f
                pillListLayout.addView(pillTextView)
            }

            addButton.setOnClickListener {
                val newEditText = EditText(context)
                newEditText.hint = "New pill name"
                newEditText.textSize = 18f
                newEditText.setOnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                        val pillName = newEditText.text.toString()
                        if (pillName.isNotBlank()) {
                            addPillToServer(pillName)
                            val pillTextView = TextView(context)
                            pillTextView.text = pillName
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
            return listOf("Pill 1", "Pill 2", "Pill 3")
        }

        private fun addPillToServer(pillName: String) {
            val json = JSONObject()
            json.put("pill_name", pillName)
            val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("http://192.168.144.50:5555/add_pill")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Failed to add pill", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Pill added successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Failed to add pill", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
