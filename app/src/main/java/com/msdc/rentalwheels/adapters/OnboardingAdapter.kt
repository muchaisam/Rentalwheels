package com.msdc.rentalwheels.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.msdc.rentalwheels.R

class OnboardingAdapter(private val context: Context, private val images: IntArray, private val captions: Array<String>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_screen, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.image.setImageResource(images[position])
        holder.caption.text = captions[position]
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageonboarding)
        var caption: TextView = itemView.findViewById(R.id.caption)
    }
}