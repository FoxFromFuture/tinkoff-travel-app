package com.tinkoff.travelapp.adapter

import android.annotation.SuppressLint
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
import com.tinkoff.travelapp.model.route.RouteItem
import com.tinkoff.travelapp.model.street.Street

class TripCardAdapter(
    private var images: List<Int>,
    private var title: List<String>,
    private var date: List<String>,
    private var key_points: List<String>,
    private var trip_description: List<String>
) : RecyclerView.Adapter<TripCardAdapter.TripCardViewHolder>() {

    var routeList = emptyList<RouteItem>()
    var street: Street = Street()
    var testRouteItem_0: RouteItem = RouteItem(0, "tempStreet", "STREET", 56.313503, 44.008277, "temp street", "https://yandex.ru", "00:00", "23:59", 0)
    var testRouteItem_1: RouteItem = RouteItem(1, "tempBar", "BAR", 76.313503, 24.008277, "temp bar", "https://yandex.ru", "16:00", "23:00", 2)
    var testRouteItem_2: RouteItem = RouteItem(2, "tempCafe", "CAFE", 52.313503, 34.008277, "temp cafe", "https://yandex.ru", "08:00", "22:00", 3)
    var testRouteItem_3: RouteItem = RouteItem(3, "tempMuseum", "MUSEUM", 69.313503, 48.008277, "temp museum", "https://yandex.ru", "08:00", "21:00", 1)

    inner class TripCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTripCard: CardView = itemView.findViewById(R.id.trip_card_cardView)
        val itemImage: ImageView = itemView.findViewById(R.id.trip_card_image)
        val itemTitle: TextView = itemView.findViewById(R.id.trip_card_title)
        val itemDate: TextView = itemView.findViewById(R.id.trip_card_date)
        val itemKeyPoints: TextView = itemView.findViewById(R.id.trip_card_key_points)

        init {
            itemImage.setOnClickListener {
                val position = adapterPosition
                Toast.makeText(itemView.context, "Clicked", Toast.LENGTH_SHORT).show()
            }
            itemTripCard.setOnClickListener {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripCardAdapter.TripCardViewHolder {
        return TripCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_page_trip_card, parent, false))
    }

    override fun getItemCount(): Int {
        return title.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNextStreet(list: Street) {
        street = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TripCardAdapter.TripCardViewHolder, position: Int) {
        holder.itemImage.setImageResource(images[position])
//        holder.itemTitle.text = title[position]
        holder.itemTitle.text = street.name
        holder.itemDate.text = date[position]
//        holder.itemKeyPoints.text = key_points[position]
        val tempKeyPointsString: String = testRouteItem_0.name + " - " + testRouteItem_3.name
        holder.itemKeyPoints.text = tempKeyPointsString
//        val tempKeyPointsString: String = routeList.get(0).name + " - " + routeList.get(routeList.size - 1).name
//        holder.itemKeyPoints.text = tempKeyPointsString
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun setListOfRoutes(list: List<RouteItem>) {
//        routeList = list
//        notifyDataSetChanged()
//    }
}
