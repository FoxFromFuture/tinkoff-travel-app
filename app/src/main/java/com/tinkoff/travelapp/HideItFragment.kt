package com.tinkoff.travelapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HideItFragment : Fragment() {
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.activity_hide_it, container, false)
        return rootView
    }

    override fun onStart() {
        super.onStart()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }, 1500
        )
    }

    companion object {
        const val TAG = "HideItFragment"
    }
}
