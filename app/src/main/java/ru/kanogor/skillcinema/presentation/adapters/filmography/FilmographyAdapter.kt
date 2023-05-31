package ru.kanogor.skillcinema.presentation.adapters.filmography

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.kanogor.skillcinema.databinding.TitleSubtitleTextItemBinding
import ru.kanogor.skillcinema.data.entity.retrofit.StaffFilms
import ru.kanogor.skillcinema.presentation.adapters.viewholders.TitleSubtitleViewHolder

class FilmographyAdapter :
    ListAdapter<StaffFilms, TitleSubtitleViewHolder>(FilmographyDiffUtilCallBack()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: TitleSubtitleViewHolder, position: Int) {
        val item = getItem(position)
        val titleText = if (item?.nameRu == null) item.nameEn else item.nameRu
        val description = if (item?.description == "") {
            when (item.professionKey) {
                "WRITER" -> "Сценарист"
                "DIRECTOR" -> "Режиссер"
                "PRODUCER" -> "Продюсер"
                "ACTOR" -> "Играет персонажа"
                "HIMSELF" -> "В роли себя"
                else -> "Другое"
            }
        } else item.description
        with(holder.binding) {
            title.text = titleText
            subtitle.text = description
            root.setOnClickListener {
                item?.let {
                    onItemClick?.invoke(item.filmId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleSubtitleViewHolder {
        val binding =
            TitleSubtitleTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TitleSubtitleViewHolder(binding)
    }
}

class FilmographyDiffUtilCallBack : DiffUtil.ItemCallback<StaffFilms>() {
    override fun areItemsTheSame(
        oldItem: StaffFilms,
        newItem: StaffFilms
    ): Boolean =
        oldItem.filmId == newItem.filmId

    override fun areContentsTheSame(
        oldItem: StaffFilms,
        newItem: StaffFilms
    ): Boolean =
        oldItem.nameRu == newItem.nameRu
}