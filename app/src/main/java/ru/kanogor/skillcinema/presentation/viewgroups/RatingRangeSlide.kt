package ru.kanogor.skillcinema.presentation.viewgroups

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.RatingRangeslideItemBinding

class RatingRangeSlide @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attr, defStyleAttr) {

    val binding = RatingRangeslideItemBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun numbersCount() {
        val values = binding.slider.values
        binding.numStart.text = values[0].toInt().toString()
        binding.numEnd.text = values[1].toInt().toString()
    }

    fun ratingRange() {
        val values = binding.slider.values
        val valueStart = values[0].toInt()
        val valueEnd = values[1].toInt()

        if (valueStart == 1 && valueEnd == 10) binding.ratingRange.text = "Любое"
        else {
            binding.ratingRange.text =
                context.getString(R.string.range_from_to, valueStart, valueEnd)
        }
    }

    val slider = binding.slider

}