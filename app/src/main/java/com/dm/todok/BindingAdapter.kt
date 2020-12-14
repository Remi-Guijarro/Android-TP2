package com.dm.todok

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation

@BindingAdapter("app:avatar")
fun setAvatar(imageView: ImageView, avatarUrl: String?) {
    imageView.load(avatarUrl) {
        transformations(CircleCropTransformation())
    }
}
