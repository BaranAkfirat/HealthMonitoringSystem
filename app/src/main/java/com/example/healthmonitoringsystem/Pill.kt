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


class Pill : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_pill, container, false)

        val addButton: ImageButton = rootView.findViewById(R.id.add)
        val pillListLayout: LinearLayout = rootView.findViewById(R.id.pill_list_layout)

        addButton.setOnClickListener {
            val newEditText = EditText(context)
            newEditText.hint = "New pill name"
            newEditText.textSize = 18f
            pillListLayout.addView(newEditText)
        }

        return rootView
    }
}
