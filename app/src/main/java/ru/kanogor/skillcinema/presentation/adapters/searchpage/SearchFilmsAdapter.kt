package ru.kanogor.skillcinema.presentation.adapters.searchpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.MovieItemHorizontalBinding
import ru.kanogor.skillcinema.data.entity.retrofit.FilteredFilmsList

class SearchFilmsAdapter :
    ListAdapter<FilteredFilmsList, SearchFilmsListHolder>(SearchDiffUtilCallBack()) {

    var onItemClick: ((FilteredFilmsList) -> Unit)? = null

    override fun onBindViewHolder(holder: SearchFilmsListHolder, position: Int) {
        val item = getItem(position)
        val titleText = if (item?.nameRu == null) item.nameOriginal else item.nameRu
        val year = item.year
        val subtitleText = if (year == null) "${
            item?.genres?.joinToString(
                separator = ", ",
                limit = 3
            ) { it.genre }
        }" else "$year, ${item?.genres?.joinToString(separator = ", ", limit = 3) { it.genre }}"
        val ratingText =
            if (item?.ratingKinopoisk == null) item?.ratingImdb else item.ratingKinopoisk
        with(holder.binding) {
            title.text = titleText
            subtitle.text = subtitleText
            if (ratingText == null) {
                rating.visibility = View.GONE
            } else {
                rating.visibility = View.VISIBLE
                rating.text = ratingText.toString()
            }
            item.let {
                Glide.with(poster.context)
                    .load(it?.posterUrl)
                    .placeholder(R.drawable.no_poster)
                    .into(poster)
            }
            root.setOnClickListener {
                item?.let {
                    onItemClick?.invoke(item)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFilmsListHolder {
        val binding =
            MovieItemHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchFilmsListHolder(binding)
    }

}

class SearchDiffUtilCallBack : DiffUtil.ItemCallback<FilteredFilmsList>() {
    override fun areItemsTheSame(
        oldItem: FilteredFilmsList,
        newItem: FilteredFilmsList
    ): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(
        oldItem: FilteredFilmsList,
        newItem: FilteredFilmsList
    ): Boolean =
        oldItem.nameRu == newItem.nameRu
}

class SearchFilmsListHolder(val binding: MovieItemHorizontalBinding) :
    RecyclerView.ViewHolder(binding.root)
