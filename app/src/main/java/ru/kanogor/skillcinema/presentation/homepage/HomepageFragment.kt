package ru.kanogor.skillcinema.presentation.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentHomepageBinding
import ru.kanogor.skillcinema.presentation.adapters.premieresfilms.PremieresFilmsAdapter
import ru.kanogor.skillcinema.presentation.adapters.soaps.FilteredFilmsAdapter
import ru.kanogor.skillcinema.presentation.adapters.topfilms.TopFilmsListAdapter
import ru.kanogor.skillcinema.presentation.listpage.ListPageFragment.Companion.FIRST_RAND_FILM
import ru.kanogor.skillcinema.presentation.listpage.ListPageFragment.Companion.POPULAR
import ru.kanogor.skillcinema.presentation.listpage.ListPageFragment.Companion.PREMIERES
import ru.kanogor.skillcinema.presentation.listpage.ListPageFragment.Companion.SECOND_RAND_FILM
import ru.kanogor.skillcinema.presentation.listpage.ListPageFragment.Companion.SOAPS
import ru.kanogor.skillcinema.presentation.listpage.ListPageFragment.Companion.TOP_FILMS
import ru.kanogor.skillcinema.presentation.viewgroups.ItemsListViewGroup

private const val CATEGORY_NAME = "adapter_name"
private const val FILM_ID = "film_id"

@AndroidEntryPoint
class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null

    private val viewModel: HomepageViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        botNav.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkCollection()

        viewModel.isLoading.onEach { isLoading ->
            progressVisibility(isLoading = isLoading)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.filmsIds.onEach { listIds ->
            //Premieres
            val premieresAdapter = PremieresFilmsAdapter(listIds)
            premieresFilmsSettings(premieresAdapter)

            //Popular:
            val popularAdapter = TopFilmsListAdapter(listIds)
            topFilmsSettings(
                adapter = popularAdapter,
                itemsViewGroup = binding.popular,
                category = POPULAR
            )

            //First Random Films
            val firstRandomFilmsAdapter = FilteredFilmsAdapter(listIds)
            filteredFilmsSettings(
                adapter = firstRandomFilmsAdapter,
                itemsViewGroup = binding.firstRandomFilms,
                category = FIRST_RAND_FILM
            )

            //Second Random Films
            val secondRandomFilmsAdapter = FilteredFilmsAdapter(listIds)
            filteredFilmsSettings(
                adapter = secondRandomFilmsAdapter,
                itemsViewGroup = binding.secondRandomFilms,
                category = SECOND_RAND_FILM
            )

            //Top 250
            val topFilmsAdapter = TopFilmsListAdapter(listIds)
            topFilmsSettings(
                adapter = topFilmsAdapter,
                itemsViewGroup = binding.topFilms,
                category = TOP_FILMS
            )

            //Soaps
            val soapsAdapter = FilteredFilmsAdapter(listIds)
            filteredFilmsSettings(
                adapter = soapsAdapter,
                itemsViewGroup = binding.soapOpera,
                category = SOAPS
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun moveToListPage(category: String) {
        val bundle = Bundle().apply {
            putString(CATEGORY_NAME, category)
        }
        findNavController().navigate(R.id.action_homepage_to_listPage, args = bundle)
    }

    private fun moveToFilmPage(id: Int) {
        val bundle = Bundle().apply {
            putInt(FILM_ID, id)
        }
        findNavController().navigate(R.id.action_homepage_to_filmPage, args = bundle)
    }

    private fun progressVisibility(isLoading: Boolean) {
        if (isLoading) {
            with(binding) {
                progressBar.visibility = View.VISIBLE
                premieres.visibility = View.GONE
                popular.visibility = View.GONE
                topFilms.visibility = View.GONE
                firstRandomFilms.visibility = View.GONE
                secondRandomFilms.visibility = View.GONE
                soapOpera.visibility = View.GONE
            }
        } else {
            with(binding) {
                progressBar.visibility = View.GONE
                premieres.visibility = View.VISIBLE
                popular.visibility = View.VISIBLE
                topFilms.visibility = View.VISIBLE
                firstRandomFilms.visibility = View.VISIBLE
                secondRandomFilms.visibility = View.VISIBLE
                soapOpera.visibility = View.VISIBLE
            }
        }
    }

    private fun premieresFilmsSettings(adapter: PremieresFilmsAdapter) {
        with(binding.premieres) {
            setTitle(getString(R.string.premieres))
            setRecyclerView(adapter)
            showAllButton.setOnClickListener {
                moveToListPage(PREMIERES)
            }
        }
        adapter.onButtonClick = { moveToListPage(PREMIERES) }
        adapter.onItemClick = { filmId -> moveToFilmPage(filmId) }

        viewModel.premieresMovieList.onEach {
            adapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun topFilmsSettings(
        adapter: TopFilmsListAdapter,
        itemsViewGroup: ItemsListViewGroup,
        category: String
    ) {
        with(itemsViewGroup) {
            setRecyclerView(adapter)
            showAllButton.setOnClickListener {
                moveToListPage(category)
            }
        }
        adapter.onButtonClick = { moveToListPage(category) }
        adapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
        when (category) {
            POPULAR -> {
                viewModel.popularMovieList.onEach {
                    adapter.submitList(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                itemsViewGroup.setTitle(getString(R.string.popular_films))
            }

            TOP_FILMS -> {
                viewModel.topMovieList.onEach {
                    adapter.submitList(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                itemsViewGroup.setTitle(getString(R.string.top_films))
            }
        }
    }

    private fun filteredFilmsSettings(
        adapter: FilteredFilmsAdapter,
        itemsViewGroup: ItemsListViewGroup,
        category: String
    ) {
        with(itemsViewGroup) {
            setRecyclerView(adapter)
            showAllButton.setOnClickListener {
                moveToListPage(category)
            }
        }
        adapter.onButtonClick = { moveToListPage(category) }
        adapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
        when (category) {
            FIRST_RAND_FILM -> {
                viewModel.firstRandomFilms.onEach {
                    adapter.submitList(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                viewModel.firstRandomFilmTitle.onEach {
                    binding.firstRandomFilms.setTitle(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            SECOND_RAND_FILM -> {
                viewModel.secondRandomFilms.onEach {
                    adapter.submitList(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                viewModel.secondRandomFilmTitle.onEach {
                    itemsViewGroup.setTitle(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            SOAPS -> {
                viewModel.soapsFilms.onEach {
                    adapter.submitList(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
                itemsViewGroup.setTitle(getString(R.string.soaps))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}