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
import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.route.RouteItem

class TripCardAdapter(
    private var images: List<Int>,
    private var title: List<String>,
    private var date: List<String>
) : RecyclerView.Adapter<TripCardAdapter.TripCardViewHolder>() {

    var routeList = mutableListOf<Route>()
    var curRoute = Route()
//    var curRoute =  emptyList<RouteItem>()

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

                val tempKeyPointsString: String =
                    routeList[position][0].name + " - " + routeList[position].elementAt(routeList[position].lastIndex).name

                var tempDescription = "Places: \n"
                var placesCount = 1
                for (item in routeList[position]) {
                    tempDescription += "${placesCount}. ${item.name} \n"
                    placesCount++
                }

                intent.putExtra("itemImage", R.drawable.base_trip_card_image)
                intent.putExtra("itemTitle", title[position])
                intent.putExtra("itemDate", date[position])
                intent.putExtra("itemKeyPoints", tempKeyPointsString)
                intent.putExtra("itemTripDescription", tempDescription)

                startActivity(itemView.context, intent, options.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TripCardAdapter.TripCardViewHolder {
        return TripCardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_page_trip_card, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return title.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListOfRoutes(list: List<RouteItem>, route: Route) {
//        curRoute.clear()
//        curRoute.addAll(list)
        route.addAll(list)
        routeList.add(route)
//        curRoute = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TripCardAdapter.TripCardViewHolder, position: Int) {
        holder.itemImage.setImageResource(R.drawable.base_trip_card_image)
        holder.itemTitle.text = title[position]
        holder.itemDate.text = date[position]
        val tempKeyPointsString: String =
            routeList[position][0].name + " - " + routeList[position].elementAt(routeList[position].lastIndex).name
        holder.itemKeyPoints.text = tempKeyPointsString
        if (position == title.lastIndex) {
            routeList.clear()
        }
    }
}
