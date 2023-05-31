package ru.kanogor.skillcinema.presentation.adapters.staff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.PersonsItemBinding
import ru.kanogor.skillcinema.data.entity.retrofit.Staff
import ru.kanogor.skillcinema.presentation.adapters.viewholders.StaffListHolder

open class ActorsListAdapter :
    ListAdapter<Staff, StaffListHolder>(StaffDiffUtilCallBack()) {

    companion object {
        private const val NUM_OF_ACTORS = 20
    }

    var onItemClick: ((Int) -> Unit)? = null

    override fun submitList(list: List<Staff?>?) {
        if ((list?.size ?: 0) >= NUM_OF_ACTORS) super.submitList(list?.take(NUM_OF_ACTORS))
        else super.submitList(list)
    }

    override fun onBindViewHolder(holder: StaffListHolder, position: Int) {
        val item = getItem(position)
        val titleText = item?.nameRu
        val descriptionText =
            if (item?.description == null) item?.professionText?.dropLast(1) else item.description
        with(holder.binding) {
            title.text = titleText
            description.text = descriptionText
            item.let {
                Glide.with(poster.context)
                    .load(it?.posterUrl)
                    .placeholder(R.drawable.no_poster)
                    .into(poster)
            }
            root.setOnClickListener {
                item?.let {
                    onItemClick?.invoke(item.staffId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffListHolder {
        return StaffListHolder(
            PersonsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}

class StaffDiffUtilCallBack : DiffUtil.ItemCallback<Staff>() {

    override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean =
        oldItem.staffId == newItem.staffId

    override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean =
        oldItem.staffId == newItem.staffId
}