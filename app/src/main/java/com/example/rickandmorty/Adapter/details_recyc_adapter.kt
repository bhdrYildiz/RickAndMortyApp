package com.example.rickandmorty.Adapter

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.CharacterDetails
import com.example.rickandmorty.R
import com.example.rickandmorty.Apis.retrofit_apis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class details_recyc_adapter(
    private val context: Context,
    private val list: List<CharacterDetails>
) : RecyclerView.Adapter<details_recyc_adapter.ViewHolder>() {

    private val characterDetailsList = mutableListOf<CharacterDetails>()
    private var onResidentIdsClickListener: ItemClickListener? = null

    init {
        characterDetailsList.addAll(list)
    }

    override fun getItemCount(): Int {
        return characterDetailsList.size
    }

    fun updateList(newList: List<CharacterDetails>) {
        characterDetailsList.clear()
        characterDetailsList.addAll(newList)
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onItemClicked(id: String)
    }

    fun setOnCharacterClickListener(listener: ItemClickListener) {
        onResidentIdsClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.residents_viewholder, parent, false)
        return ViewHolder(itemView)
    }

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var characterPic: ImageView = view.findViewById(R.id.characterPic)
        var genderPic: ImageView = view.findViewById(R.id.genderPic)
        var characterName: TextView = view.findViewById(R.id.characterName)
        var idTextHidden: TextView = view.findViewById(R.id.idTextHidden)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val id = idTextHidden.text.toString()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val apiService = retrofit.create(retrofit_apis::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                val idMoreCharacterDetail = apiService.getMoreDetailCharacter(id)
                Log.d("idMoreCharacterDetail::", idMoreCharacterDetail.toString())
                if (!id.isNullOrEmpty()) {
                    onResidentIdsClickListener?.onItemClicked(id)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = characterDetailsList[position]
        Glide.with(context).load(currentItem.image).into(holder.characterPic)
        holder.characterName.text = currentItem.name.toString()
        holder.idTextHidden.text = currentItem.id.toString()
        if(position % 2 == 0){
            holder.characterPic.left = Gravity.LEFT
            holder.genderPic.right = Gravity.RIGHT
        }
        else {
            holder.characterPic.right = Gravity.RIGHT
            holder.genderPic.left = Gravity.LEFT
        }

        when (currentItem.gender) {
            "Female" -> Glide.with(context).load(R.drawable.female).into(holder.genderPic)
            "Male" -> Glide.with(context).load(R.drawable.male).into(holder.genderPic)
            else -> Glide.with(context).load(R.drawable.na).into(holder.genderPic)
        }
    }


}

