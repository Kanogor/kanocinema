package ru.kanogor.skillcinema.presentation.viewgroups

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.MovieItemHorizontalBinding

class MovieItemHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = MovieItemHorizontalBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    @SuppressLint("SetTextI18n")
    fun setSubtitle(year: String, genres: String) {
        binding.subtitle.text = "$year $genres"
    }

    fun setRating(rating: Double?) {
        if (rating == null) binding.rating.visibility = View.GONE
        else {
            binding.rating.visibility = View.VISIBLE
            binding.rating.text = rating.toString()
        }
    }

    fun setPicture(url: String) {
        Glide.with(binding.poster.context)
            .load(url)
            .placeholder(R.drawable.no_poster)
            .into(binding.poster)
    }

}