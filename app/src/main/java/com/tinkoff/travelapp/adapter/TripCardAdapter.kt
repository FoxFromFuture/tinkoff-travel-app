package com.tinkoff.travelapp.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.travelapp.R
import com.tinkoff.travelapp.TripDescriptionActivity

class TripCardAdapter(private var images: List<Int>, private var title: List<String>, private var date: List<String>, private var key_points: List<String>, private var trip_description: List<String>) : RecyclerView.Adapter<TripCardAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTripCard: CardView = itemView.findViewById(R.id.trip_card_cardView)
        val itemImage: ImageView = itemView.findViewById(R.id.trip_card_image)
        val itemTitle: TextView = itemView.findViewById(R.id.trip_card_title)
        val itemDate: TextView = itemView.findViewById(R.id.trip_card_date)
        val itemKeyPoints: TextView = itemView.findViewById(R.id.trip_card_key_points)

        init {
            itemImage.setOnClickListener { v: View ->
                val position = adapterPosition
                Toast.makeText(itemView.context, "You clicked on Billy! He likes it!", Toast.LENGTH_SHORT).show()
            }
            itemTripCard.setOnClickListener { v: View ->
                val position = adapterPosition
                val intent = Intent(itemView.context, TripDescriptionActivity::class.java)

                val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    itemView.context as Activity?,
                    Pair(itemImage, "trip_card_transition")
                )

                intent.putExtra("itemImage", images[position])
                intent.putExtra("itemTitle", title[position])
                intent.putExtra("itemDate", date[position])
                intent.putExtra("itemKeyPoints", key_points[position])
                intent.putExtra("itemTripDescription", trip_description[position])

                startActivity(itemView.context, intent, options.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripCardAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.page_trip_card, parent, false))
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: TripCardAdapter.Pager2ViewHolder, position: Int) {
        holder.itemImage.setImageResource(images[position])
        holder.itemTitle.text = title[position]
        holder.itemDate.text = date[position]
        holder.itemKeyPoints.text = key_points[position]
    }
}