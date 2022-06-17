package com.tinkoff.travelapp.menu

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import androidx.preference.ListPreference
import com.tinkoff.travelapp.R

class CustomListPreference : ListPreference {
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context!!, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context!!, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    constructor(context: Context?) : super(context!!)

    override fun onClick() {
        val builder = AlertDialog.Builder(context).setSingleChoiceItems(entries, getValueIndex())
        { dialog, index ->
            if (callChangeListener(entryValues[index].toString())) {
                setValueIndex(index)
            }
            dialog.dismiss()
        }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }.setTitle(title)

        val dialog = builder.create()
        dialog.window?.decorView?.setBackgroundResource(R.drawable.custom_dialog_layout)
        dialog.show()
    }

    private fun getValueIndex() = entryValues.indexOf(value)
}
