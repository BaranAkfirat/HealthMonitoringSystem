package com.example.healthmonitoringsystem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class Sport : Fragment() {
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sport, container, false)
        val addButton: ImageButton = rootView.findViewById(R.id.add)
        val SportListLayout: LinearLayout = rootView.findViewById(R.id.Walking)

        val existingWalking = getWalkingToServer()

        // Add existing sleep times to the layout
        existingWalking.forEach { Walking ->
            val WalkingEditText = EditText(context)
            WalkingEditText.hint = "Walk"
            WalkingEditText.textSize = 18f
            WalkingEditText.setText(Walking)
            WalkingEditText.isEnabled = false
            SportListLayout.addView(WalkingEditText)
        }

        addButton.setOnClickListener {
            val newEditText = EditText(context)
            newEditText.hint = "Enter Your Walk Distance"
            newEditText.textSize = 18f
            newEditText.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    val Walking = newEditText.text.toString()
                    if (Walking.isNotBlank()) {
                        WalkingToServer(Walking)
                        val WalkingEditText = EditText(context)
                        WalkingEditText.hint = "Sport Distance"
                        WalkingEditText.textSize = 18f
                        WalkingEditText.setText(Walking)
                        WalkingEditText.isEnabled = false
                        SportListLayout.addView(WalkingEditText)
                        SportListLayout.removeView(newEditText)
                    }
                    true
                } else {
                    false
                }
            }
            SportListLayout.addView(newEditText)
        }
        val backButton: ImageButton = rootView.findViewById(R.id.Back)

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


        return rootView
    }

    private fun getWalkingToServer(): List<String> {
        // Make a request to the server to get existing sleep times
        // For now, return a static list
        return listOf("Running Distance 1", "Running Distance 2", "Running Distance 3")
    }

    private fun WalkingToServer(Walking: String) {
        val json = JSONObject()
        json.put("sport_name", Walking)
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://192.168.144.50:5555/add_sport")  // Replace YOUR_SERVER_IP with your actual server IP
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Failed to add Walk Distance", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Walk Distance added successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Failed to add Walk Distance", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
