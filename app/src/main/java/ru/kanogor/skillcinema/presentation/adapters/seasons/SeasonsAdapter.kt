package ru.kanogor.skillcinema.presentation.adapters.seasons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.kanogor.skillcinema.databinding.TitleSubtitleTextItemBinding
import ru.kanogor.skillcinema.data.entity.retrofit.Episode
import ru.kanogor.skillcinema.presentation.adapters.viewholders.TitleSubtitleViewHolder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeasonsAdapter :
    ListAdapter<Episode, TitleSubtitleViewHolder>(SeasonsDiffUtilCallBack()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: TitleSubtitleViewHolder, position: Int) {
        val item = getItem(position)
        val episodeName = if (item?.nameRu == null) item.nameEn else item.nameRu
        val titleText = "${item.episodeNumber} серия. $episodeName"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val data =
            if (item.releaseDate != null) LocalDate.parse(item.releaseDate, formatter) else null
        val subtitleText =
            if (data != null) data.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) else ""
        with(holder.binding) {
            title.text = titleText
            subtitle.text = subtitleText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleSubtitleViewHolder {
        val binding =
            TitleSubtitleTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TitleSubtitleViewHolder(binding)
    }
}

class SeasonsDiffUtilCallBack() : DiffUtil.ItemCallback<Episode>() {
    override fun areItemsTheSame(
        oldItem: Episode,
        newItem: Episode
    ): Boolean =
        oldItem.nameRu == newItem.nameRu

    override fun areContentsTheSame(
        oldItem: Episode,
        newItem: Episode
    ): Boolean =
        oldItem.nameEn == newItem.nameEn
}

