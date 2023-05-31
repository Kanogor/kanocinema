package ru.kanogor.skillcinema.presentation.adapters.staff

import ru.kanogor.skillcinema.data.entity.retrofit.Staff


class ProfessionsListAdapter : ActorsListAdapter() {

    companion object {
       private const val NUM_OF_STAFF = 6
    }

    override fun submitList(list: List<Staff?>?) {
        if ((list?.size ?: 0) >= NUM_OF_STAFF) super.submitList(list?.take(NUM_OF_STAFF))
        else super.submitList(list)
    }

}