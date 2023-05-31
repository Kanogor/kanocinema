package ru.kanogor.skillcinema.presentation.searchpage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentSearchPageBinding
import ru.kanogor.skillcinema.presentation.adapters.calendar.CalendarAdapter
import ru.kanogor.skillcinema.presentation.adapters.searchpage.SearchFilmsAdapter

private const val FILM_ID = "film_id"

@AndroidEntryPoint
class SearchPageFragment : Fragment() {

    private var _binding: FragmentSearchPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchPageViewModel by viewModels()

    private var isMenuRevealed = false
    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.onEach {
            if (it) {
                binding.underEditTextSearchFilms.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.underEditTextSearchFilms.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        val searchAdapter = SearchFilmsAdapter()
        binding.recyclerView.adapter = searchAdapter
        searchAdapter.onItemClick = { film -> moveToFilmPage(film.kinopoiskId) }

        binding.textInputLayout.setEndIconOnClickListener {
            revealLayoutFun()
        }

        binding.toolbarSearchPage.setNavigationOnClickListener {
            revealLayoutFun()
            viewModel.getFilms(
                text = binding.textInputEditText.text.toString(),
            )
            binding.textInputEditText.requestFocus()
        }

        binding.textInputEditText.doOnTextChanged { text, _, _, _ ->
            viewLifecycleOwner.lifecycleScope.launch {
                binding.underEditTextSearchFilms.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                viewModel.getFilms(
                    text = text.toString(),
                )
            }
        }

        viewModel.searchedFilms.onEach { filmList ->
            if (filmList.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.notFoundText.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.notFoundText.visibility = View.GONE
            }
            searchAdapter.submitList(filmList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // ! Menu
        // Type Group

        var lastTypeChosen = R.id.type_left_button

        binding.typeButtonGroup.addOnButtonCheckedListener { group, _, _ ->

            when (group.checkedButtonId) {
                R.id.type_left_button -> {
                    lastTypeChosen = R.id.type_left_button
                    viewModel.typeFilter = viewModel.filmType.allTypes
                }

                R.id.type_center_button -> {
                    lastTypeChosen = R.id.type_center_button
                    viewModel.typeFilter = viewModel.filmType.films
                }

                R.id.type_right_button -> {
                    lastTypeChosen = R.id.type_right_button
                    viewModel.typeFilter = viewModel.filmType.soaps
                }

                else -> {
                    binding.typeButtonGroup.check(lastTypeChosen)
                }
            }
        }

        // Country Spinner

        val countriesArray = resources.getStringArray(R.array.Countries)
        val countryAdapter = ArrayAdapter(requireContext(), R.layout.spinner_text, countriesArray)
        countryAdapter.setDropDownViewResource(R.layout.spinner_drop_text)
        with(binding.countryFilterSearch) {
            setTitle(getString(R.string.country))
            spinner.adapter = countryAdapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.countryFilter = viewModel.countryAndGenreLists.countryList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        // Genre Spinner

        val genreArray = resources.getStringArray(R.array.Genres)
        val genreAdapter = ArrayAdapter(requireContext(), R.layout.spinner_text, genreArray)
        genreAdapter.setDropDownViewResource(R.layout.spinner_drop_text)
        with(binding.genresFilterSearch) {
            setTitle(getString(R.string.genre))
            spinner.adapter = genreAdapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.genreFilter = viewModel.countryAndGenreLists.genreList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        // Data filter

        binding.yearsFilterSearch.setRangeTitle(viewModel.yearFromFilter, viewModel.yearToFilter)
        binding.yearsFilterSearch.setOnClickListener {
            viewModel.yearFromFilter = null
            viewModel.yearToFilter = null
            binding.menuSearchLayout.visibility = View.GONE
            binding.dataRangeLayoutFilterSearchpage.visibility = View.VISIBLE
        }
        val listFromData: List<Int> = (DATA_FROM..DATA_FROM_UNTIL).map { it }
        val listToData: List<Int> = (DATA_TO..DATA_TO_UNTIL).map { it }
        val mutableListFromData = listFromData.toMutableList()
        val mutableListToData = listToData.toMutableList()
        val adapterFromData = CalendarAdapter()
        val adapterToData = CalendarAdapter()
        adapterFromData.setData(mutableListFromData)
        adapterToData.setData(mutableListToData)
        binding.recyclerViewFromSearchpageFilter.adapter = adapterFromData
        binding.recyclerViewToSearchpageFilter.adapter = adapterToData
        adapterFromData.onItemClick = { item ->
            viewModel.yearFromFilter = item
        }
        adapterToData.onItemClick = { item ->
            viewModel.yearToFilter = item
        }

        binding.dateRangeFromSearchpageFilter.text =
            "${mutableListFromData.first()}-${mutableListFromData.last()}"
        binding.dateRangeToSearchpageFilter.text =
            "${mutableListToData.first()}-${mutableListToData.last()}"

        binding.leftButtonDataFromSearchpageFilter.setOnClickListener {
            changeDataList(
                mutableListData = mutableListFromData,
                adapter = adapterFromData,
                isLeftButton = true,
                titleRange = binding.dateRangeFromSearchpageFilter
            )
        }
        binding.rightButtonDataFromSearchpageFilter.setOnClickListener {
            changeDataList(
                mutableListData = mutableListFromData,
                adapter = adapterFromData,
                isLeftButton = false,
                titleRange = binding.dateRangeFromSearchpageFilter
            )
        }
        binding.leftButtonDataToSearchpageFilter.setOnClickListener {
            changeDataList(
                mutableListData = mutableListToData,
                adapter = adapterToData,
                isLeftButton = true,
                titleRange = binding.dateRangeToSearchpageFilter
            )
        }
        binding.rightButtonDataToSearchpageFilter.setOnClickListener {
            changeDataList(
                mutableListData = mutableListToData,
                adapter = adapterToData,
                isLeftButton = false,
                titleRange = binding.dateRangeToSearchpageFilter
            )
        }

        binding.toolbarCalendarSearchPage.setNavigationOnClickListener {
            toFilterPage(viewModel.yearToFilter, viewModel.yearFromFilter)
        }

        binding.choseDataButtonSearchpageFilter.setOnClickListener {
            toFilterPage(viewModel.yearToFilter, viewModel.yearFromFilter)
        }

        // Rating Group

        binding.ratingFilterSearch.numbersCount()
        binding.ratingFilterSearch.ratingRange()
        binding.ratingFilterSearch.slider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            viewModel.ratingFromFilter = values[0].toInt()
            viewModel.ratingToFilter = values[1].toInt()
            binding.ratingFilterSearch.numbersCount()
            binding.ratingFilterSearch.ratingRange()
        }

        // Sorting Group

        var lastSortingChosen = R.id.sorting_left_button

        binding.sortingButtonGroup.addOnButtonCheckedListener { group, _, _ ->
            when (group.checkedButtonId) {
                R.id.sorting_left_button -> {
                    lastSortingChosen = R.id.sorting_left_button
                    viewModel.orderFilter = viewModel.filmsOrder.year
                }

                R.id.sorting_center_button -> {
                    lastSortingChosen = R.id.sorting_center_button
                    viewModel.orderFilter = viewModel.filmsOrder.numVote
                }

                R.id.sorting_right_button -> {
                    lastSortingChosen = R.id.sorting_right_button
                    viewModel.orderFilter = viewModel.filmsOrder.rating
                }

                else -> {
                    binding.sortingButtonGroup.check(lastSortingChosen)
                }
            }
        }

        // Is viewed

        binding.isViewedButton.setOnClickListener {
            viewModel.isViewed = if (viewModel.isViewed) {
                binding.isViewedButton.itViewed(viewModel.isViewed)
                false
            } else {
                binding.isViewedButton.itViewed(viewModel.isViewed)
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun moveToFilmPage(id: Int) {
        val bundle = Bundle().apply {
            putInt(FILM_ID, id)
        }
        findNavController().navigate(
            R.id.action_Search_to_filmPage,
            args = bundle
        )
    }

    private fun revealLayoutFun() {
        if (!isMenuRevealed) {
            binding.menuSearchLayout.visibility = View.VISIBLE
            binding.searchLayout.visibility = View.GONE
            isMenuRevealed = true

        } else {
            binding.menuSearchLayout.visibility = View.GONE
            binding.searchLayout.visibility = View.VISIBLE
            isMenuRevealed = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun changeDataList(
        mutableListData: MutableList<Int>,
        adapter: CalendarAdapter,
        isLeftButton: Boolean,
        titleRange: TextView
    ) {
        mutableListData.replaceAll {
            if (isLeftButton) {
                it - YEAR_STEP
            } else {
                it + YEAR_STEP
            }
        }
        adapter.setData(mutableListData)
        titleRange.text = "${mutableListData.first()}-${mutableListData.last()}"
    }

    private fun toFilterPage(yearToFilter: Int?, yearFromFilter: Int?) {
        if (yearToFilter == null || yearFromFilter == null) Toast.makeText(
            requireContext(),
            getString(R.string.data_chose_toast_first),
            Toast.LENGTH_SHORT
        ).show()
        else {
            if (yearFromFilter > yearToFilter) Toast.makeText(
                requireContext(),
                getString(R.string.data_chose_toast_second),
                Toast.LENGTH_SHORT
            ).show()
            else {
                binding.yearsFilterSearch.setRangeTitle(yearFromFilter, yearToFilter)
                binding.dataRangeLayoutFilterSearchpage.visibility = View.GONE
                binding.menuSearchLayout.visibility = View.VISIBLE
            }
        }
    }

    private companion object {
        private const val YEAR_STEP = 12
        private const val DATA_FROM = 2001
        private const val DATA_FROM_UNTIL = DATA_FROM + 11
        private const val DATA_TO = 2001
        private const val DATA_TO_UNTIL = DATA_TO + 11
    }

}