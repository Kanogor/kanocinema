package ru.kanogor.skillcinema.presentation.adapters.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.ImageItemBinding
import ru.kanogor.skillcinema.data.entity.retrofit.GalleryPicture

class PicturesPagingAdapter :
    PagingDataAdapter<GalleryPicture, GalleryHolder>(GalleryDiffUtilCallBack()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            item.let {
                Glide.with(poster.context)
                    .load(it?.previewUrl)
                    .placeholder(R.drawable.no_poster)
                    .into(poster)
            }
            root.setOnClickListener {
                item?.let {
                    onItemClick?.invoke(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        return GalleryHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}

