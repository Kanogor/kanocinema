package ru.kanogor.skillcinema.presentation.adapters.premieresfilms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.MovieItemVerticalBinding
import ru.kanogor.skillcinema.databinding.ViewAllFilmsButtonBinding
import ru.kanogor.skillcinema.data.entity.retrofit.PremieresFilmsList
import ru.kanogor.skillcinema.presentation.adapters.soaps.FilteredFilmsAdapter.Companion.NUM_OF_LIST
import ru.kanogor.skillcinema.presentation.adapters.viewholders.ButtonViewHolder
import ru.kanogor.skillcinema.presentation.adapters.viewholders.FilmsListHolder

class PremieresFilmsAdapter(private val listIds: List<Int>) :
    ListAdapter<PremieresFilmsList, RecyclerView.ViewHolder>(PremieresFilmsDiffUtilCallBack()) {

    var onButtonClick: (() -> Unit)? = null
    var onItemClick: ((Int) -> Unit)? = null

    override fun submitList(list: List<PremieresFilmsList?>?) {
        if ((list?.size ?: 0) > NUM_OF_LIST) super.submitList(list?.take(NUM_OF_LIST))
        else super.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            NUM_OF_LIST - 1 -> R.layout.view_all_films_button
            else -> R.layout.movie_item_vertical
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val titleText = item?.nameRu
        val genresText = item?.genres?.joinToString(separator = ", ", limit = 3) { it.genre }
        when (getItemViewType(position)) {
            R.layout.movie_item_vertical -> {
                if (listIds.contains(item.kinopoiskId)) {
                    (holder as FilmsListHolder).binding.iconIsViewed.visibility = View.VISIBLE
                } else {
                    (holder as FilmsListHolder).binding.iconIsViewed.visibility = View.GONE
                }
                with(holder.binding) {
                    title.text = titleText
                    genres.text = genresText
                    rating.background = null
                    item.let {
                        Glide.with(poster.context)
                            .load(it?.posterUrl)
                            .placeholder(R.drawable.no_poster)
                            .into(poster)
                    }
                    root.setOnClickListener {
                        item?.let {
                            onItemClick?.invoke(item.kinopoiskId)
                        }
                    }
                }
            }

            R.layout.view_all_films_button -> {
                (holder as ButtonViewHolder).binding.root.setOnClickListener {
                    item?.let {
                        onButtonClick?.invoke()
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

class PremieresFilmsDiffUtilCallBack : DiffUtil.ItemCallback<PremieresFilmsList>() {
    override fun areItemsTheSame(
        oldItem: PremieresFilmsList,
        newItem: PremieresFilmsList
    ): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(
        oldItem: PremieresFilmsList,
        newItem: PremieresFilmsList
    ): Boolean =
        oldItem.nameRu == newItem.nameRu
}