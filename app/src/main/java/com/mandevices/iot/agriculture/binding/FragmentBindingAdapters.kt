package com.mandevices.iot.agriculture.binding

import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import android.widget.ImageView

import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
//        Glide.with(fragment).load(url).into(imageView)
    }
}
