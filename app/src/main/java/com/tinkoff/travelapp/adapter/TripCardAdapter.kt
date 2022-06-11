package com.tinkoff.travelapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
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
import com.google.gson.Gson
import com.tinkoff.travelapp.R
import com.tinkoff.travelapp.TripDescriptionActivity
import com.tinkoff.travelapp.database.model.TripDataModel
import com.tinkoff.travelapp.TripDurations
import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.route.RouteItem
import kotlin.random.Random

class TripCardAdapter(
    private var images: List<Int>,
    private var duration: List<TripDurations>
) : RecyclerView.Adapter<TripCardAdapter.TripCardViewHolder>() {

    var routeList: List<TripDataModel> = listOfNotNull()

    inner class TripCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTripCard: CardView = itemView.findViewById(R.id.trip_card_cardView)
        val itemImage: ImageView = itemView.findViewById(R.id.trip_card_image)
        val itemTitle: TextView = itemView.findViewById(R.id.trip_card_title)
        val itemDate: TextView = itemView.findViewById(R.id.trip_card_date)
        val itemKeyPoints: TextView = itemView.findViewById(R.id.trip_card_key_points)

        init {
            itemImage.setOnClickListener {
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
                    routeList[position].route[0].name + " - " + routeList[position].route.elementAt(
                        routeList[position].route.lastIndex
                    ).name

                var tempDescription = "Places:" + "\n"
                var placesCount = 1
                for (item in routeList[position].route) {
                    tempDescription += "${placesCount}. ${item.name} \n"
                    placesCount++
                }

                val currentRoute = routeList[position].route

                intent.putExtra("itemImage", R.drawable.base_trip_card_image)
                intent.putExtra("itemTitle", routeList[position].title)
                intent.putExtra("itemDate", routeList[position].date)
                intent.putExtra("itemKeyPoints", tempKeyPointsString)
                intent.putExtra("itemTripDescription", tempDescription)
                intent.putExtra("itemRealRoute", Gson().toJson(currentRoute))

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
        return routeList.size
    }

    private fun compressRouteToDuration(route: Route, routeDuration: TripDurations): Route {
        val newRoute = Route()
        val random = Random(System.currentTimeMillis())

        val randomShortNumber: Int
        var index: Int
        when (routeDuration) {
            TripDurations.SHORT -> {
                randomShortNumber = random.nextInt(2, 3)
                for (i in 1..randomShortNumber) {
                    index = random.nextInt(route.size)
                    newRoute.add(route[index])
                    route.removeAt(index)
                    if (route.isEmpty()) {
                        break
                    }
                }
            }
            TripDurations.MEDIUM -> {
                randomShortNumber = random.nextInt(5, 6)
                for (i in 1..randomShortNumber) {
                    index = random.nextInt(route.size)
                    newRoute.add(route[index])
                    route.removeAt(index)
                    if (route.isEmpty()) {
                        break
                    }
                }
            }
            TripDurations.LONG -> {
                randomShortNumber = random.nextInt(9, 10)
                for (i in 1..randomShortNumber) {
                    index = random.nextInt(route.size)
                    newRoute.add(route[index])
                    route.removeAt(index)
                    if (route.isEmpty()) {
                        break
                    }
                }
            }
        }

        return newRoute
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setListOfRoutes(list: List<TripDataModel>) {
        routeList = list
//        val compressed = compressRouteToDuration(route, duration[title.size - 1])
//        routeList.add(compressed)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TripCardAdapter.TripCardViewHolder, position: Int) {
        holder.itemImage.setImageResource(R.drawable.base_trip_card_image)
        holder.itemTitle.text = routeList[position].title
        holder.itemDate.text = routeList[position].date
        val tempKeyPointsString: String =
            routeList[position].route[0].name + " - " + routeList[position].route.elementAt(
                routeList[position].route.lastIndex
            ).name
        holder.itemKeyPoints.text = tempKeyPointsString
    }
}
