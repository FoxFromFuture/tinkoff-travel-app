package com.tinkoff.travelapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class CreateTravelDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.main_create_travel_popup, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        rootView.findViewById<Button>(R.id.create_travel_popup_button).setOnClickListener {
            val trip_name = rootView.findViewById<EditText>(R.id.create_travel_popup_input_name)
            val trip_name_value = trip_name.text.toString()
            val trip_cost = rootView.findViewById<EditText>(R.id.create_travel_popup_input_budget)
            val trip_cost_value = trip_cost.text.toString().toInt()
            val trip_start_time =
                rootView.findViewById<EditText>(R.id.create_travel_popup_input_start_time)
            val trip_start_time_value = trip_start_time.text.toString()
            val trip_end_time =
                rootView.findViewById<EditText>(R.id.create_travel_popup_input_end_time)
            val trip_end_time_value = trip_end_time.text.toString()

            var temp_category_value: CheckBox
            var trip_category_street_value: Boolean = false
            var trip_category_bar_value: Boolean = false
            var trip_category_cafe_value: Boolean = false
            var trip_category_museum_value: Boolean = false

            temp_category_value =
                rootView.findViewById<CheckBox>(R.id.main_create_travel_popup_street_btn)
            trip_category_street_value = temp_category_value.isChecked
            temp_category_value =
                rootView.findViewById<CheckBox>(R.id.main_create_travel_popup_bar_btn)
            trip_category_bar_value = temp_category_value.isChecked
            temp_category_value =
                rootView.findViewById<CheckBox>(R.id.main_create_travel_popup_cafe_btn)
            trip_category_cafe_value = temp_category_value.isChecked
            temp_category_value =
                rootView.findViewById<CheckBox>(R.id.main_create_travel_popup_museum_btn)
            trip_category_museum_value = temp_category_value.isChecked


            val categoriesList = mutableListOf<String>()
            if (trip_category_street_value) {
                categoriesList.add("STREET")
            }
            if (trip_category_bar_value) {
                categoriesList.add("BAR")
            }
            if (trip_category_cafe_value) {
                categoriesList.add("CAFE")
            }
            if (trip_category_museum_value) {
                categoriesList.add("MUSEUM")
            }

            val mainActivity: MainActivity = activity as MainActivity

            mainActivity.dialogFragmentListener(
                trip_name_value,
                categoriesList,
                trip_cost_value,
                trip_start_time_value,
                trip_end_time_value
            )

            Toast.makeText(context, "Trip was created!", Toast.LENGTH_SHORT).show()

            dismiss()
        }

        return rootView
    }

    companion object {
        const val TAG = "CreateTravelDialog"
    }
}
