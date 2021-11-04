package com.mikyegresl.openweather.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikyegresl.openweather.R
import com.mikyegresl.openweather.data.model.City

class CitiesAdapter(
    private val context: Context,
    private val onCityClickedListener: (City) -> Unit
): RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {

    private var cityList: List<City> = ArrayList(0)

    inner class CityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cityCountryTextView: TextView = itemView.findViewById(R.id.city_country_text_view)
    }

    fun bindCityList(cityList: List<City>) {
        this.cityList = ArrayList(cityList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.city_item, parent, false)
        return CityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.cityCountryTextView.text = context.getString(
            R.string.city_country_name,
            cityList[position].name,
            cityList[position].country)
        holder.itemView.setOnClickListener {
            onCityClickedListener.invoke(cityList[position])
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }
}