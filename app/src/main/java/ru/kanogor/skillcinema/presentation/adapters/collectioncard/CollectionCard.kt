package ru.kanogor.skillcinema.presentation.adapters.collectioncard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.CollectionCardBinding
import ru.kanogor.skillcinema.data.entity.database.DefaultCollectionsName
import ru.kanogor.skillcinema.data.entity.database.FilmsCollection

class CollectionCardAdapter :
    ListAdapter<FilmsCollection, CollectionCardHolder>(CollectionCardDiffUtilCallBack()) {

    var onCloseClick: ((String) -> Unit)? = null
    var onItemClick: ((String) -> Unit)? = null

    private val collectionsName = DefaultCollectionsName()

    override fun submitList(list: List<FilmsCollection>?) {
        val mutableList: MutableList<FilmsCollection>? = list?.toMutableList()
        mutableList?.removeIf {
            it.savedCollection.collectionName == collectionsName.interested ||
                    it.savedCollection.collectionName == collectionsName.watched
        }
        super.submitList(mutableList)
    }

    override fun onBindViewHolder(holder: CollectionCardHolder, position: Int) {
        val item = getItem(position)
        if (item.savedCollection.collectionName == collectionsName.liked) {
            holder.binding.close.visibility = View.GONE
            holder.binding.icon.setImageResource(R.drawable.ic_heart)
        }
        if (item.savedCollection.collectionName == collectionsName.wantWatch) {
            holder.binding.close.visibility = View.GONE
            holder.binding.icon.setImageResource(R.drawable.ic_bookmark)
        }
        with(holder.binding)
        {
            collectionName.text = item.savedCollection.collectionName
            filmsCount.text = item.collectionFilms?.size.toString()
            touchZone.setOnClickListener {
                onItemClick?.invoke(item.savedCollection.collectionName)
            }
            close.setOnClickListener {
                onCloseClick?.invoke(item.savedCollection.collectionName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionCardHolder {
        val binding = CollectionCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionCardHolder(binding)
    }

}

class CollectionCardDiffUtilCallBack : DiffUtil.ItemCallback<FilmsCollection>() {
    override fun areItemsTheSame(
        oldItem: FilmsCollection,
        newItem: FilmsCollection
    ): Boolean =
        oldItem.savedCollection.collectionName == newItem.savedCollection.collectionName

    override fun areContentsTheSame(
        oldItem: FilmsCollection,
        newItem: FilmsCollection
    ): Boolean =
        oldItem.savedCollection.collectionName == newItem.savedCollection.collectionName
}

class CollectionCardHolder(val binding: CollectionCardBinding) :
    RecyclerView.ViewHolder(binding.root)