package ru.kanogor.skillcinema.presentation.adapters.savedfilm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.MovieItemVerticalBinding
import ru.kanogor.skillcinema.databinding.ViewAllFilmsButtonBinding
import ru.kanogor.skillcinema.data.entity.database.NewSavedFilm
import ru.kanogor.skillcinema.presentation.adapters.viewholders.ButtonViewHolder
import ru.kanogor.skillcinema.presentation.adapters.viewholders.FilmsListHolder

open class SavedFilmAdapter :
    ListAdapter<NewSavedFilm, RecyclerView.ViewHolder>(SavedFilmDiffUtilCallBack()) {

    var onButtonClick: (() -> Unit)? = null
    var onItemClick: ((Int) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> R.layout.view_all_films_button
            else -> R.layout.movie_item_vertical
        }
    }

    override fun submitList(list: List<NewSavedFilm>?) {
        val mutableList = list?.toMutableList()
        mutableList?.add(NewSavedFilm(null, "", 1, "", "", null, ""))
        super.submitList(mutableList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val titleText = item.name
        val genresText = item.genres
        val ratingText = item.rating
        when (getItemViewType(position)) {
            R.layout.movie_item_vertical -> {
                with((holder as FilmsListHolder).binding) {
                    title.text = titleText
                    genres.text = genresText
                    rating.text = ratingText.toString()
                    item.let {
                        Glide.with(poster.context)
                            .load(it?.url)
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

            R.layout.view_all_films_button -> {
                with((holder as ButtonViewHolder).binding) {
                    textShowAll.text = "Очистить историю"
                    pointer.setImageResource(R.mipmap.ic_container)
                    pointer.setOnClickListener {
                        item?.let {
                            onButtonClick?.invoke()
                        }
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.movie_item_vertical -> {
                val binding =
                    MovieItemVerticalBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                FilmsListHolder(binding)
            }

            R.layout.view_all_films_button -> {
                val binding = ViewAllFilmsButtonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ButtonViewHolder(binding)
            }

            else -> {
                throw IllegalArgumentException("unknown view type $viewType")
            }
        }
    }
}

class SavedFilmDiffUtilCallBack : DiffUtil.ItemCallback<NewSavedFilm>() {
    override fun areItemsTheSame(
        oldItem: NewSavedFilm,
        newItem: NewSavedFilm
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: NewSavedFilm,
        newItem: NewSavedFilm
    ): Boolean =
        oldItem.name == newItem.name
}
