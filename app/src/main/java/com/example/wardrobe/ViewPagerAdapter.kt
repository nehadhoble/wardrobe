package com.example.wardrobe

import android.graphics.Bitmap
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(private val viewPagerItemArrayList: ArrayList<ViewPagerItem>): RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    private var positions: List<Int> = (0 until viewPagerItemArrayList.size).toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewpager_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewPagerItemArrayList[positions[position]])
    }

    override fun getItemCount(): Int {
        return viewPagerItemArrayList.size
    }
    fun updatePositions(newPositions: List<Int>) {
        positions = newPositions
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.ivimage)
        fun bind(image: ViewPagerItem) {
            imageView.setImageBitmap(image.imageID)
        }
    }
}