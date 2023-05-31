package ru.kanogor.skillcinema.presentation.adapters.calendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.CalendarNumberBinding

class CalendarAdapter() : RecyclerView.Adapter<CalendarViewHolder>() {

    private var values = emptyList<Int>()
    var onItemClick: ((Int?) -> Unit)? = null
    var isClicked = false

    @SuppressLint("NotifyDataSetChanged")
    fun setData(values: List<Int>) {
        this.values = values
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = values[position]
        with(holder.binding) {
            yearCalendar.text = item.toString()
            root.setOnClickListener {
                if (isClicked) {
                    setData(values)
                    item.let {
                        onItemClick?.invoke(null)
                    }
                    isClicked = false
                } else {
                    it.setBackgroundColor(ContextCompat.getColor(root.context, R.color.app_blue))
                    yearCalendar.setTextColor(Color.WHITE)
                    item.let {
                        onItemClick?.invoke(item)
                    }
                    isClicked = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding =
            CalendarNumberBinding.inflate(LayoutInflater.from(parent.context))
        return CalendarViewHolder(binding)
    }

    override fun getItemCount(): Int = 12

}

class CalendarViewHolder(val binding: CalendarNumberBinding) :
    RecyclerView.ViewHolder(binding.root)
