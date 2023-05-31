package ru.kanogor.skillcinema.presentation.adapters.onboardingslider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kanogor.skillcinema.databinding.OnboardingScreenBinding

class OnboardingSliderAdapter(private val onboardingSlide: List<OnboardingSlide>) :
    RecyclerView.Adapter<OnboardingSliderAdapter.IntroSlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
        return IntroSlideViewHolder(
            OnboardingScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return onboardingSlide.size
    }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.bind(onboardingSlide[position])
    }

    inner class IntroSlideViewHolder(val binding: OnboardingScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(onboardingSlide: OnboardingSlide) {
            binding.onboardingText.text = onboardingSlide.text
            binding.onboardingPicture.setImageResource(onboardingSlide.picture)
        }

    }
}