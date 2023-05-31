package ru.kanogor.skillcinema.presentation.adapters.idfilms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.MovieItemVerticalBinding
import ru.kanogor.skillcinema.data.entity.retrofit.IdFilm
import ru.kanogor.skillcinema.presentation.adapters.diffutil.IdFilmsDiffUtilCallBack
import ru.kanogor.skillcinema.presentation.adapters.viewholders.FilmsListHolder

class IdFilmsAdapter : ListAdapter<IdFilm, FilmsListHolder>(IdFilmsDiffUtilCallBack()) {

    companion object {
        const val NUM_OF_LIST = 20
    }

    var onItemClick: ((IdFilm) -> Unit)? = null

    override fun submitList(list: List<IdFilm?>?) {
        if ((list?.size ?: 0) > NUM_OF_LIST) super.submitList(list?.take(NUM_OF_LIST))
        else super.submitList(list)
    }

    override fun onBindViewHolder(holder: FilmsListHolder, position: Int) {
        val item = getItem(position)
        val titleText = if (item?.nameRu == null) item.nameOriginal else item.nameRu
        val genresText = item?.genres?.joinToString(separator = ", ", limit = 3) { it.genre }
        val ratingText =
            if (item?.ratingKinopoisk == null) item?.ratingImdb else item.ratingKinopoisk
        with(holder.binding) {
            title.text = titleText
            genres.text = genresText
            rating.text = ratingText.toString()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsListHolder {
        val binding =
            MovieItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmsListHolder(binding)
    }
}
