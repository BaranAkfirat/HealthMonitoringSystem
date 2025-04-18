package com.example.healthmonitoringsystem

import android.annotation.SuppressLint
import android.app.Notification
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.example.healthmonitoringsystem.R
import com.example.healthmonitoringsystem.databinding.FragmentHomeBinding

class MainPage : Fragment() {
    private lateinit var Pill: ImageButton
    private lateinit var Appointment: ImageButton
    private lateinit var heartrate: ImageButton
    private lateinit var sleep: ImageButton
    private lateinit var sport: ImageButton
    private lateinit var profile: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_page, container, false)

        profile = rootView.findViewById(R.id.profile)
        profile.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Profile())
                .addToBackStack(null)
                .commit()
        }

        sport = rootView.findViewById(R.id.Sport)
        sport.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Sport())
                .addToBackStack(null)
                .commit()
        }

        sleep = rootView.findViewById(R.id.Sleep)
        sleep.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Sleep())
                .addToBackStack(null)
                .commit()
        }

        heartrate = rootView.findViewById(R.id.HeartRate)
        heartrate.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HeartRate())
                .addToBackStack(null)
                .commit()
        }

        Appointment = rootView.findViewById(R.id.appointment)

        Appointment.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, appointment())
                .addToBackStack(null)
                .commit()
        }

        Pill = rootView.findViewById(R.id.pill)

        Pill.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Pill())
                .addToBackStack(null)
                .commit()
        }


        return rootView
    }
}
