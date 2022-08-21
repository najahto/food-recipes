package com.najahto.foodreceipes.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.najahto.foodreceipes.R

class RecipesRowBinding {
    companion object {
        @BindingAdapter("setLikesNumber")
        @JvmStatic
        fun setLikesNumber(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("setMinutesNumber")
        @JvmStatic
        fun setMinutesNumber(textView: TextView, minutes: Int) {
            textView.text = minutes.toString()
        }

        @BindingAdapter("setVeganColor")
        @JvmStatic
        fun setVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }
                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                    }
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl:String){
            imageView.load(imageUrl){
                crossfade(600)
            }
        }
    }
}