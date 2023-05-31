package ru.kanogor.skillcinema.presentation.adapters.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.ImageSligerItemBinding
import ru.kanogor.skillcinema.data.entity.retrofit.GalleryPicture

class ViewPagerAdapter :
    ListAdapter<GalleryPicture, SliderGalleryHolder>(GalleryDiffUtilCallBack()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: SliderGalleryHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            item.let {
                Glide.with(image.context)
                    .load(it?.imageUrl)
                    .placeholder(R.drawable.no_poster)
                    .into(image)
            }
            root.setOnClickListener {
                item?.let {
                    onItemClick?.invoke(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderGalleryHolder {
        return SliderGalleryHolder(
            ImageSligerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}

class SliderGalleryHolder(val binding: ImageSligerItemBinding) :
    RecyclerView.ViewHolder(binding.root)

