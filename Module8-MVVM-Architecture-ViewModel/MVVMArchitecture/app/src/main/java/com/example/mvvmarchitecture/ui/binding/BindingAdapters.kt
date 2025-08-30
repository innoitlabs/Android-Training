package com.example.mvvmarchitecture.ui.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mvvmarchitecture.data.model.UiState

@BindingAdapter("app:visibleIf")
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:visibleIfState")
fun setVisibleForState(view: View, state: UiState<*>?) {
    view.visibility = when (state) {
        is UiState.Loading -> View.VISIBLE
        is UiState.Success -> View.VISIBLE
        is UiState.Error -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("app:loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        Glide.with(imageView.context)
            .load(it)
            .circleCrop()
            .into(imageView)
    }
}

@BindingAdapter("app:loadUserAvatar")
fun loadUserAvatar(imageView: ImageView, userId: Int?) {
    userId?.let {
        val avatarUrl = "https://i.pravatar.cc/150?u=$it"
        Glide.with(imageView.context)
            .load(avatarUrl)
            .circleCrop()
            .into(imageView)
    }
}
