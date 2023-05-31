package ru.kanogor.skillcinema.presentation.filmography

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentFilmographyBinding
import ru.kanogor.skillcinema.data.entity.retrofit.StaffFilms
import ru.kanogor.skillcinema.presentation.adapters.filmography.FilmographyAdapter

private const val ACTOR_ID = "actor_id"
private const val FILM_ID = "film_id"

@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    private var _binding: FragmentFilmographyBinding? = null

    private val binding get() = _binding!!

    private val viewModel: FilmographyViewModel by viewModels()

    private var personId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            personId = it.getInt(ACTOR_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmographyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.filmography_page_title)

        viewModel.isLoading.onEach {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.pageLayout.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.pageLayout.visibility = View.VISIBLE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.loadInfo(personId!!)

        viewModel.personInfo.onEach { person ->
            binding.actorName.text = person?.nameRu
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // Наименование чипов
        setChipTitle(
            viewModel.asActorFilms,
            binding.actorChipFilmography,
            titleMan = R.string.actor,
            titleWoman = R.string.actress,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.asDirectorFilms,
            binding.directorChipFilmography,
            titleMan = R.string.director,
            titleWoman = R.string.director,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.asHimselfFilms,
            binding.himselfChipFilmography,
            titleMan = R.string.himself_man,
            titleWoman = R.string.himself_woman,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.asProducerFilms,
            binding.producerChipFilmography,
            titleMan = R.string.producer,
            titleWoman = R.string.producer,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.asWriterFilms,
            binding.writerChipFilmography,
            titleMan = R.string.writer,
            titleWoman = R.string.writer,
            viewModel.isItMan
        )
        setChipTitle(
            viewModel.otherFilms,
            binding.otherChipFilmography,
            titleMan = R.string.other,
            titleWoman = R.string.other,
            viewModel.isItMan
        )

        // Работа со списком
        val listAdapter = FilmographyAdapter()
        listAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
        binding.recyclerView.adapter = listAdapter
        binding.chipGroupFilmography.setOnCheckedStateChangeListener { group, _ ->
            when (group.checkedChipId) {
                R.id.actor_chip_filmography -> {
                    viewModel.asActorFilms.onEach {
                        listAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                R.id.director_chip_filmography -> {
                    viewModel.asDirectorFilms.onEach {
                        listAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                R.id.producer_chip_filmography -> {
                    viewModel.asProducerFilms.onEach {
                        listAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                R.id.himself_chip_filmography -> {
                    viewModel.asHimselfFilms.onEach {
                        listAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                R.id.writer_chip_filmography -> {
                    viewModel.asWriterFilms.onEach {
                        listAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
                R.id.other_chip_filmography -> {
                    viewModel.otherFilms.onEach {
                        listAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
            }

        }


    }

    private fun setChipTitle(
        stateFlowFilms: StateFlow<List<StaffFilms>>,
        chip: Chip,
        titleMan: Int,
        titleWoman: Int,
        isItManStateFlow: StateFlow<Boolean>
    ) {
        stateFlowFilms.onEach { filmList ->
            if (filmList.isEmpty()) chip.visibility = View.GONE
            else {
                with(chip) {
                    visibility = View.VISIBLE
                    isItManStateFlow.onEach {
                        Log.d("Filmography", it.toString())
                        text = if (it) getString(
                            titleMan,
                            filmList.size
                        ) else getString(
                            titleWoman,
                            filmList.size
                        )
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun moveToFilmPage(id: Int) {
        val bundle = Bundle().apply {
            putInt(FILM_ID, id)
        }
        findNavController().navigate(R.id.action_filmography_to_filmPage, args = bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}