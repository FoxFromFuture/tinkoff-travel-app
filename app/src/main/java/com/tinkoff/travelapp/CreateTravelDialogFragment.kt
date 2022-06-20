package com.tinkoff.travelapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment

class CreateTravelDialogFragment : DialogFragment(), View.OnClickListener {
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.main_create_travel_popup, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val buttonGo = rootView.findViewById<Button>(R.id.main_create_travel_popup_button)
        buttonGo.setOnClickListener(this)

        return rootView
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.main_create_travel_popup_button -> {
                val regexTime = """^([0-1]?[0-9]|2[0-3]):[0-5][0-9]${'$'}""".toRegex()

                val textTripName =
                    rootView.findViewById<EditText>(R.id.create_travel_popup_input_name)
                val valueTripName = textTripName.text.toString()
                    .ifEmpty { getString(R.string.main_create_travel_popup_untitled) }

                val textTripCost =
                    rootView.findViewById<EditText>(R.id.create_travel_popup_input_budget)
                val valueTripCost =
                    if (textTripCost.text.toString().isEmpty())
                        0
                    else
                        textTripCost.text.toString().toInt()

                val textTripStartTime =
                    rootView.findViewById<EditText>(R.id.create_travel_popup_input_start_time)
                val valueTripStartTime = textTripStartTime.text.toString()
                if (valueTripStartTime.isEmpty()) {
                    Toast.makeText(
                        context,
                        getString(R.string.main_create_travel_popup_error_start_time_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!valueTripStartTime.matches(regexTime)) {
                    Toast.makeText(
                        context,
                        getString(R.string.main_create_travel_popup_error_start_time_invalid),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val textTripEndTime =
                    rootView.findViewById<EditText>(R.id.create_travel_popup_input_end_time)
                val valueTripEndTime = textTripEndTime.text.toString()
                if (valueTripEndTime.isEmpty()) {
                    Toast.makeText(
                        context,
                        getString(R.string.main_create_travel_popup_error_end_time_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!valueTripEndTime.matches(regexTime)) {
                    Toast.makeText(
                        context,
                        getString(R.string.main_create_travel_popup_error_end_time_invalid),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                if (valueTripStartTime >= valueTripEndTime) {
                    Toast.makeText(
                        context,
                        getString(R.string.main_create_travel_popup_error_period_invalid),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val valueTripCategoryStreet: Boolean
                val valueTripCategoryBar: Boolean
                val valueTripCategoryCafe: Boolean
                val valueTripCategoryMuseum: Boolean

                var reusableCategoryValue =
                    rootView.findViewById<CheckBox>(R.id.main_create_travel_popup_street_button)
                valueTripCategoryStreet = reusableCategoryValue.isChecked

                reusableCategoryValue =
                    rootView.findViewById(R.id.main_create_travel_popup_bar_button)
                valueTripCategoryBar = reusableCategoryValue.isChecked

                reusableCategoryValue =
                    rootView.findViewById(R.id.main_create_travel_popup_cafe_btn)
                valueTripCategoryCafe = reusableCategoryValue.isChecked

                reusableCategoryValue =
                    rootView.findViewById(R.id.main_create_travel_popup_museum_button)
                valueTripCategoryMuseum = reusableCategoryValue.isChecked

                val categoriesList = mutableListOf<String>()
                if (valueTripCategoryStreet) {
                    categoriesList.add("STREET")
                }
                if (valueTripCategoryBar) {
                    categoriesList.add("BAR")
                }
                if (valueTripCategoryCafe) {
                    categoriesList.add("CAFE")
                }
                if (valueTripCategoryMuseum) {
                    categoriesList.add("MUSEUM")
                }

                if (categoriesList.isEmpty()) {
                    Toast.makeText(
                        context,
                        getString(R.string.main_create_travel_popup_error_choose_types),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val radioGroup =
                    rootView.findViewById<RadioGroup>(R.id.main_create_travel_popup_radiogroup)
                val checkedRadioButton =
                    rootView.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                var valueTripDuration: TripDurations = TripDurations.SHORT
                when (checkedRadioButton.text) {
                    getString(R.string.main_create_travel_popup_short) -> {
                        valueTripDuration = TripDurations.SHORT
                    }
                    getString(R.string.main_create_travel_popup_medium) -> {
                        valueTripDuration = TripDurations.MEDIUM
                    }
                    getString(R.string.main_create_travel_popup_long) -> {
                        valueTripDuration = TripDurations.LONG
                    }
                }

                (activity as MainActivity).dialogFragmentListener(
                    valueTripName,
                    categoriesList,
                    valueTripCost,
                    valueTripStartTime,
                    valueTripEndTime,
                    valueTripDuration
                )

                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "CreateTravelDialog"
    }
}
