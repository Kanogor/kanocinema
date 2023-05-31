package ru.kanogor.skillcinema.presentation.adapters.topfilms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.MovieItemVerticalBinding
import ru.kanogor.skillcinema.data.entity.retrofit.TopFilmsList
import ru.kanogor.skillcinema.presentation.adapters.viewholders.FilmsListHolder

class TopFilmsPagingAdapter(private val listIds: List<Int>) :
    PagingDataAdapter<TopFilmsList, FilmsListHolder>(TopFilmsDiffUtilCallBack()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: FilmsListHolder, position: Int) {
        val item = getItem(position)
        val titleText = item?.nameRu
        val ratingText = item?.rating
        val ratingTextWithoutPercent = if ("%" in ratingText.toString()) {
            ratingText?.replace("%", "")!!.toDouble() / 10
        } else {
            ratingText
        }
        val genresText = item?.genres?.joinToString(separator = ", ", limit = 3) { it.genre }
        if (listIds.contains(item?.filmId)) {
            holder.binding.iconIsViewed.visibility = View.VISIBLE
        } else {
            holder.binding.iconIsViewed.visibility = View.GONE
        }
        with(holder.binding) {
            title.text = titleText
            genres.text = genresText
            rating.text = ratingTextWithoutPercent.toString()
            item.let {
                Glide.with(poster.context)
                    .load(it?.posterUrl)
                    .placeholder(R.drawable.no_poster)
                    .into(poster)
            }
            root.setOnClickListener {
                item?.let {
                    onItemClick?.invoke(item.filmId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsListHolder {
        val binding =
            MovieItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmsListHolder(binding)
    }
}

