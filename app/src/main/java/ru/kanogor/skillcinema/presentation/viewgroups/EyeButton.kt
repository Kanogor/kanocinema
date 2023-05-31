package ru.kanogor.skillcinema.presentation.viewgroups

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.EyeButtonItemGroupBinding

class EyeButton @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attr, defStyleAttr) {

    val binding = EyeButtonItemGroupBinding.inflate(LayoutInflater.from(context))

    private val wasViewed = "Просмотрен"
    private val wasNotViewed = "Не просмотрен"

    init {
        addView(binding.root)
    }

    fun itViewed(viewed: Boolean) {
        if (!viewed) {
            binding.eyeImage.setImageResource(R.drawable.ic_eye)
            binding.title.text = wasViewed
        } else {
            binding.eyeImage.setImageResource(R.drawable.ic_slash_eye)
            binding.title.text = wasNotViewed
        }
    }
}