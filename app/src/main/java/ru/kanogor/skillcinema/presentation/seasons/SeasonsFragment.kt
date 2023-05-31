package ru.kanogor.skillcinema.presentation.seasons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentSeasonsBinding
import ru.kanogor.skillcinema.presentation.adapters.seasons.SeasonsAdapter


private const val FILM_ID_NAME = "film_id_name"
private const val FILM_NAME = "film_name"

@AndroidEntryPoint
class SeasonsFragment : Fragment() {
    private var _binding: FragmentSeasonsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SeasonsViewModel by viewModels()

    private var filmId: Int? = null

    private var filmName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filmId = it.getInt(FILM_ID_NAME)
            filmName = it.getString(FILM_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = filmName

        viewModel.isLoading.onEach {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.pageLayout.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.pageLayout.visibility = View.VISIBLE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.loadInfo(filmId!!)

        val seasonsAdapter = SeasonsAdapter()
        binding.recyclerView.adapter = seasonsAdapter

        viewModel.seasonsInfo.onEach { seasonsInfo ->
            val chipCount = seasonsInfo?.total
            val startNum = seasonsInfo?.items?.get(0)?.number ?: 0
            with(binding.chipGroupSeasons) {
                addChip(chipCount, startNum)
                setOnCheckedStateChangeListener { group, _ ->
                    val chip = view.findViewById<Chip>(group.checkedChipId)
                    val checkedSeason = seasonsInfo?.items?.get(chip.id)
                    val countOfSeasonsEpisodesText = resources.getQuantityString(
                        R.plurals.count_of_seasons_seasonsPage,
                        checkedSeason?.episodes?.size ?: 0,
                        checkedSeason?.number,
                        checkedSeason?.episodes?.size
                    )
                    seasonsAdapter.submitList(checkedSeason?.episodes)
                    binding.countOfSeasonsEpisodes.text = countOfSeasonsEpisodesText
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun ChipGroup.addChip(count: Int?, startNum: Int) {
        if (count != null) {
            val endPoint = if (startNum == 0) count - 1 else count
            for (i in startNum..endPoint) {
                val chip = layoutInflater.inflate(
                    R.layout.layout_chip,
                    binding.chipGroupSeasons,
                    false
                ) as Chip
                chip.id = if (startNum == 0) i else i - 1
                chip.text = getString(R.string.chip_text_season, i)
                chip.isClickable = true
                addView(chip)
            }
        }
    }

}