package ru.kanogor.skillcinema.presentation.viewgroups

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ru.kanogor.skillcinema.databinding.SpinnerItemsViewGroupeBinding

class SpinnerItems @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attr, defStyleAttr) {

    private val binding = SpinnerItemsViewGroupeBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    val spinner = binding.spinner

    fun setTitle(text: String) {
        binding.title.text = text
    }

}