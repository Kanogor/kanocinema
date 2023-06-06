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

        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        botNav.visibility = View.VISIBLE

        viewModel.checkCollection()

        viewModel.isLoading.onEach { isLoading ->
            progressVisibility(isLoading = isLoading)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Premieres
        viewModel.filmsIds.onEach { listIds ->
            val premieresAdapter = PremieresFilmsAdapter(listIds)
            with(binding.premieres) {
                setTitle(getString(R.string.premieres))
                setRecyclerView(premieresAdapter)
                showAllButton.setOnClickListener {
                    moveToListPage(PREMIERES)
                }
            }
            premieresAdapter.onButtonClick = { moveToListPage(PREMIERES) }
            premieresAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }

            viewModel.premieresMovieList.onEach {
                premieresAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)


            //Popular:
            val popularAdapter = TopFilmsListAdapter(listIds)
            with(binding.popular) {
                setTitle(getString(R.string.popular_films))
                setRecyclerView(popularAdapter)
                showAllButton.setOnClickListener {
                    moveToListPage(POPULAR)
                }
            }
            popularAdapter.onButtonClick = { moveToListPage(POPULAR) }
            popularAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }

            viewModel.popularMovieList.onEach {
                popularAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)


            //First Random Films
            val firstRandomFilmsAdapter = FilteredFilmsAdapter(listIds)
            with(binding.firstRandomFilms) {
                setRecyclerView(firstRandomFilmsAdapter)
                showAllButton.setOnClickListener {
                    moveToListPage(FIRST_RAND_FILM)
                }
            }
            firstRandomFilmsAdapter.onButtonClick = { moveToListPage(FIRST_RAND_FILM) }
            firstRandomFilmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }

            viewModel.firstRandomFilmTitle.onEach {
                binding.firstRandomFilms.setTitle(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.firstRandomFilms.onEach {
                firstRandomFilmsAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)


            //Second Random Films
            val secondRandomFilmsAdapter = FilteredFilmsAdapter(listIds)
            with(binding.secondRandomFilms) {
                setRecyclerView(secondRandomFilmsAdapter)
                showAllButton.setOnClickListener {
                    moveToListPage(SECOND_RAND_FILM)
                }
            }
            secondRandomFilmsAdapter.onButtonClick = { moveToListPage(SECOND_RAND_FILM) }
            secondRandomFilmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }

            viewModel.secondRandomFilmTitle.onEach {
                binding.secondRandomFilms.setTitle(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.secondRandomFilms.onEach {
                secondRandomFilmsAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)


            //Top 250
            val topFilmsAdapter = TopFilmsListAdapter(listIds)
            with(binding.topFilms) {
                setTitle(getString(R.string.top_films))
                setRecyclerView(topFilmsAdapter)
                showAllButton.setOnClickListener {
                    moveToListPage(TOP_FILMS)
                }
            }
            topFilmsAdapter.onButtonClick = { moveToListPage(TOP_FILMS) }
            topFilmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }

            viewModel.topMovieList.onEach {
                topFilmsAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)


            //Soaps
            val soapsAdapter = FilteredFilmsAdapter(listIds)
            with(binding.soapOpera) {
                setTitle(getString(R.string.soaps))
                setRecyclerView(soapsAdapter)
                showAllButton.setOnClickListener {
                    moveToListPage(SOAPS)
                }
            }
            soapsAdapter.onButtonClick = { moveToListPage(SOAPS) }
            soapsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }

            viewModel.soapsFilms.onEach {
                soapsAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}