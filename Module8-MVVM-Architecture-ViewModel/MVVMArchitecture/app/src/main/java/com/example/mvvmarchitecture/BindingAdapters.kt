package com.example.mvvmarchitecture

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("visibleIf")
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleIfState")
fun setVisibleForState(view: View, state: UiState<*>?) {
    view.visibility = when (state) {
        is UiState.Loading -> View.VISIBLE
        is UiState.Success -> View.VISIBLE
        is UiState.Error -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        Glide.with(imageView.context)
            .load(it)
            .circleCrop()
            .into(imageView)
    }
}

@BindingAdapter("loadUserAvatar")
fun loadUserAvatar(imageView: ImageView, userId: Int) {
    val avatarUrl = "https://i.pravatar.cc/150?u=$userId"
    Glide.with(imageView.context)
        .load(avatarUrl)
        .circleCrop()
        .into(imageView)
}
