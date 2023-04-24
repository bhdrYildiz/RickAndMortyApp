package com.example.rickandmorty.Adapter

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.LocationModelMain
import com.example.rickandmorty.R
import com.example.rickandmorty.Apis.retrofit_apis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class location_recyc_adapter(private val list: ArrayList<LocationModelMain>) : RecyclerView.Adapter<location_recyc_adapter.ViewHolder>() {

    private var selectedPosition = -1
    private var onResidentIdsClickListener: OnResidentIdsClickListener? = null


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var locationName: TextView = view.findViewById(R.id.locationName)
        var idText: TextView = view.findViewById(R.id.idText)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val prevSelectedPosition = selectedPosition
            selectedPosition = adapterPosition

            if (prevSelectedPosition != -1) {
                notifyItemChanged(prevSelectedPosition)
            }
            notifyItemChanged(selectedPosition)

            val selectedId = list[selectedPosition].results[selectedPosition % list[selectedPosition].results.size].id
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(retrofit_apis::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                val locationDetail = apiService.getLocationDetails(selectedId)
                val residentsIds = locationDetail.residents.map { residentUrl ->
                    residentUrl.substringAfterLast("/").toInt()
                }

                Log.d("ResidentId::",residentsIds.toString())
                if(!residentsIds.toString().isNullOrEmpty()){
                    onResidentIdsClickListener?.onResidentIdsClick(residentsIds)
                }

            }


        }
    }

    interface OnResidentIdsClickListener {
        suspend fun onResidentIdsClick(residentsIds: List<Int>)
    }
    fun setOnResidentIdsClickListener(listener: OnResidentIdsClickListener) {
        onResidentIdsClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_location, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPage = list[position]
        val currentItem = currentPage.results[position % currentPage.results.size]
        holder.locationName.text = currentItem.name
        holder.idText.text = currentItem.id.toString()

        if (selectedPosition == position) {
            changeBackgroundColor(holder.itemView, R.drawable.background_location)
        } else {
            changeBackgroundColor(holder.itemView, R.drawable.background_location_else)
        }
    }

    private fun changeBackgroundColor(view: View, back: Int) {
        val background = view.background
        if (background is ColorDrawable) {
            if (background.color != back) {
                view.setBackgroundResource(back)
            }
        } else {
            view.setBackgroundResource(back)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

