package ru.kanogor.skillcinema.presentation.viewgroups

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import ru.kanogor.skillcinema.databinding.ItemsListViewGroupBinding
import ru.kanogor.skillcinema.presentation.adapters.gallery.GalleryLimitedAdapter
import ru.kanogor.skillcinema.presentation.adapters.idfilms.IdFilmsAdapter
import ru.kanogor.skillcinema.presentation.adapters.premieresfilms.PremieresFilmsAdapter
import ru.kanogor.skillcinema.presentation.adapters.savedfilm.SavedFilmAdapter
import ru.kanogor.skillcinema.presentation.adapters.soaps.FilteredFilmsAdapter
import ru.kanogor.skillcinema.presentation.adapters.staff.ActorsListAdapter
import ru.kanogor.skillcinema.presentation.adapters.stafffilms.StaffFilmsAdapter
import ru.kanogor.skillcinema.presentation.adapters.topfilms.TopFilmsListAdapter

class ItemsListViewGroup
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ItemsListViewGroupBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    val showAllButton = binding.allTextButton

    fun setTitle(text: String) {
        binding.titleText.text = text
    }

    fun setRecyclerView(adapter: TopFilmsListAdapter) {
        binding.recyclerView.adapter = adapter
    }

    fun setRecyclerView(adapter: PremieresFilmsAdapter) {
        binding.recyclerView.adapter = adapter
    }

    fun setRecyclerView(adapter: FilteredFilmsAdapter) {
        binding.recyclerView.adapter = adapter
    }

    fun setRecyclerView(adapter: ActorsListAdapter) {
        binding.recyclerView.adapter = adapter
    }

    fun setRecyclerView(adapter: GalleryLimitedAdapter) {
        binding.recyclerView.adapter = adapter
    }

    fun setRecyclerView(adapter: IdFilmsAdapter) {
        binding.recyclerView.adapter = adapter
    }

    fun setRecyclerView(adapter: StaffFilmsAdapter) {
        binding.recyclerView.adapter = adapter
    }

    fun setRecyclerView(adapter: SavedFilmAdapter) {
        binding.recyclerView.adapter = adapter
    }

    fun setGridLayoutManager(spanCount: Int) {
        binding.recyclerView.layoutManager =
            GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
    }

}