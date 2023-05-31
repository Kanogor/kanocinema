package ru.kanogor.skillcinema.presentation.viewgroups

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ru.kanogor.skillcinema.databinding.YearFilterViewGroupeBinding

class YearFilterItem @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attr, defStyleAttr) {

    private val binding = YearFilterViewGroupeBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    fun setRangeTitle(from: Int?, to: Int?) {
        binding.dataRangeTitle.text = "$from-$to"
    }
}