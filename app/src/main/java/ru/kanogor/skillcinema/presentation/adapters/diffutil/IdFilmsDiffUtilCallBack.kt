package ru.kanogor.skillcinema.presentation.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kanogor.skillcinema.data.entity.retrofit.IdFilm

class IdFilmsDiffUtilCallBack : DiffUtil.ItemCallback<IdFilm>() {
    override fun areItemsTheSame(
        oldItem: IdFilm,
        newItem: IdFilm
    ): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(
        oldItem: IdFilm,
        newItem: IdFilm
    ): Boolean =
        oldItem.nameRu == newItem.nameRu
}