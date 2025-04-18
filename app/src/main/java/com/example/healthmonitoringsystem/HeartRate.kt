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

class HeartRate : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_heart_rate, container, false)

        val addButton: ImageButton = rootView.findViewById(R.id.add)
        val pillListLayout: LinearLayout = rootView.findViewById(R.id.Heartrating)

        addButton.setOnClickListener {
            val newEditText = EditText(context)
            newEditText.hint = "Enter Your Last Heart Rate"
            newEditText.textSize = 18f
            pillListLayout.addView(newEditText)
        }

        return rootView
    }
}
