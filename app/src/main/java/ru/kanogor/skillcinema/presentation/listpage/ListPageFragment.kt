package ru.kanogor.skillcinema.presentation.listpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentListPageBinding
import ru.kanogor.skillcinema.presentation.homepage.HomepageViewModel
import ru.kanogor.skillcinema.presentation.adapters.idfilms.IdFilmsAdapter
import ru.kanogor.skillcinema.presentation.adapters.premieresfilms.PremieresFilmsListPageAdapter
import ru.kanogor.skillcinema.presentation.adapters.soaps.FilteredFilmsListPageAdapter
import ru.kanogor.skillcinema.presentation.adapters.staff.ActorsListAdapter
import ru.kanogor.skillcinema.presentation.adapters.staff.ProfessionsListAdapter
import ru.kanogor.skillcinema.presentation.adapters.topfilms.TopFilmsPagingAdapter
import ru.kanogor.skillcinema.presentation.filmpage.FilmPageViewModel

private const val FILM_ID = "film_id"
private const val ADAPTER_NAME = "adapter_name"
private const val ACTOR_ID = "actor_id"
private const val FILM_ID_NAME = "film_id_name"

@AndroidEntryPoint
class ListPageFragment : Fragment() {

    private var _binding: FragmentListPageBinding? = null

    private val homePageViewModel: HomepageViewModel by viewModels()

    private val filmPageViewModel: FilmPageViewModel by viewModels()

    private val binding get() = _binding!!

    private var adapterName: String? = null

    private var filmId: Int? = null

    companion object {
        const val SIMILAR_FILMS = "similar_films"
        const val STAFF_PAGE = "staff_page"
        const val ACTOR_PAGE = "actor_page"
        const val FIRST_RAND_FILM = "first_random"
        const val SECOND_RAND_FILM = "second_random"
        const val SOAPS = "soaps"
        const val TOP_FILMS = "top films"
        const val POPULAR = "popular"
        const val PREMIERES = "premieres"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            adapterName = it.getString(ADAPTER_NAME)
            filmId = it.getInt(FILM_ID_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePageViewModel.filmsIds.onEach { listIds ->
            val premieresFilmAdapter = PremieresFilmsListPageAdapter(listIds)
            val topFilmsPagedAdapter = TopFilmsPagingAdapter(listIds)
            val filteredFilmsAdapter = FilteredFilmsListPageAdapter(listIds)
            val actorsAdapter = ActorsListAdapter()
            val professionStaffAdapter = ProfessionsListAdapter()
            val similarFilmsAdapter = IdFilmsAdapter()

            binding.recyclerView.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

            when (adapterName) {
                PREMIERES -> {
                    (activity as AppCompatActivity).supportActionBar?.title =
                        getString(R.string.premieres)
                    binding.swipeRefresh.setOnRefreshListener {
                        homePageViewModel.refresh()
                    }
                    binding.recyclerView.adapter = premieresFilmAdapter
                    premieresFilmAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
                    homePageViewModel.premieresMovieList.onEach {
                        premieresFilmAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                    homePageViewModel.isLoading.onEach {
                        binding.swipeRefresh.isRefreshing = it
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                POPULAR -> {
                    (activity as AppCompatActivity).supportActionBar?.title =
                        getString(R.string.popular_films)
                    binding.swipeRefresh.setOnRefreshListener {
                        topFilmsPagedAdapter.refresh()
                    }
                    topFilmsPagedAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
                    topFilmsPagedAdapter.loadStateFlow.onEach {
                        binding.swipeRefresh.isRefreshing = it.refresh == LoadState.Loading
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                    binding.recyclerView.adapter = topFilmsPagedAdapter
                    homePageViewModel.pagingPopularFilms.onEach {
                        topFilmsPagedAdapter.submitData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                TOP_FILMS -> {
                    (activity as AppCompatActivity).supportActionBar?.title =
                        getString(R.string.top_films)
                    binding.swipeRefresh.setOnRefreshListener {
                        topFilmsPagedAdapter.refresh()
                    }
                    topFilmsPagedAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
                    topFilmsPagedAdapter.loadStateFlow.onEach {
                        binding.swipeRefresh.isRefreshing = it.refresh == LoadState.Loading
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                    binding.recyclerView.adapter = topFilmsPagedAdapter
                    homePageViewModel.pagingTopFilms.onEach {
                        topFilmsPagedAdapter.submitData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                SOAPS -> {
                    (activity as AppCompatActivity).supportActionBar?.title =
                        getString(R.string.soaps)
                    binding.swipeRefresh.setOnRefreshListener {
                        filteredFilmsAdapter.refresh()
                    }
                    filteredFilmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
                    filteredFilmsAdapter.loadStateFlow.onEach {
                        binding.swipeRefresh.isRefreshing = it.refresh == LoadState.Loading
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                    binding.recyclerView.adapter = filteredFilmsAdapter
                    homePageViewModel.pagingSoaps.onEach {
                        filteredFilmsAdapter.submitData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                FIRST_RAND_FILM -> {
                    homePageViewModel.firstRandomFilmTitle.onEach {
                        (activity as AppCompatActivity).supportActionBar?.title = it
                    }.launchIn(viewLifecycleOwner.lifecycleScope)

                    binding.swipeRefresh.setOnRefreshListener {
                        filteredFilmsAdapter.refresh()
                    }
                    filteredFilmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
                    filteredFilmsAdapter.loadStateFlow.onEach {
                        binding.swipeRefresh.isRefreshing = it.refresh == LoadState.Loading
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                    binding.recyclerView.adapter = filteredFilmsAdapter
                    homePageViewModel.pagingFirstRandomFilms.onEach {
                        filteredFilmsAdapter.submitData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                SECOND_RAND_FILM -> {
                    homePageViewModel.secondRandomFilmTitle.onEach {
                        (activity as AppCompatActivity).supportActionBar?.title = it
                    }.launchIn(viewLifecycleOwner.lifecycleScope)

                    binding.swipeRefresh.setOnRefreshListener {
                        filteredFilmsAdapter.refresh()
                    }
                    filteredFilmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
                    filteredFilmsAdapter.loadStateFlow.onEach {
                        binding.swipeRefresh.isRefreshing = it.refresh == LoadState.Loading
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                    binding.recyclerView.adapter = filteredFilmsAdapter
                    homePageViewModel.pagingSecondRandomFilms.onEach {
                        filteredFilmsAdapter.submitData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                ACTOR_PAGE -> {
                    (activity as AppCompatActivity).supportActionBar?.title =
                        getString(R.string.actors_list_title)
                    binding.recyclerView.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    filmPageViewModel.refresh(filmId!!)
                    binding.swipeRefresh.setOnRefreshListener {
                        filmPageViewModel.refresh(filmId!!)
                    }
                    binding.recyclerView.adapter = actorsAdapter
                    actorsAdapter.onItemClick = { staffId -> moveToActorPage(staffId) }
                    filmPageViewModel.actorsStaff.onEach {
                        actorsAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                    filmPageViewModel.isLoading.onEach {
                        binding.swipeRefresh.isRefreshing = it
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                STAFF_PAGE -> {
                    (activity as AppCompatActivity).supportActionBar?.title =
                        getString(R.string.staff_list_title)
                    binding.recyclerView.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    filmPageViewModel.refresh(filmId!!)
                    binding.swipeRefresh.setOnRefreshListener {
                        filmPageViewModel.refresh(filmId!!)
                    }
                    binding.recyclerView.adapter = professionStaffAdapter
                    professionStaffAdapter.onItemClick = { staffId -> moveToActorPage(staffId) }
                    filmPageViewModel.professionStaff.onEach {
                        professionStaffAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                    filmPageViewModel.isLoading.onEach {
                        binding.swipeRefresh.isRefreshing = it
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                SIMILAR_FILMS -> {
                    (activity as AppCompatActivity).supportActionBar?.title =
                        getString(R.string.similar_films)
                    filmPageViewModel.refresh(filmId!!)
                    binding.swipeRefresh.setOnRefreshListener {
                        filmPageViewModel.refresh(filmId!!)
                    }
                    binding.recyclerView.adapter = similarFilmsAdapter
                    similarFilmsAdapter.onItemClick = { film -> moveToFilmPage(film.kinopoiskId) }
                    filmPageViewModel.similarFilms.onEach {
                        similarFilmsAdapter.submitList(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                    filmPageViewModel.isLoading.onEach {
                        binding.swipeRefresh.isRefreshing = it
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun moveToFilmPage(id: Int) {
        val bundle = Bundle().apply {
            putInt(FILM_ID, id)
        }
        findNavController().navigate(
            R.id.action_listPage_to_filmPage,
            args = bundle
        )
    }

    private fun moveToActorPage(staffId: Int) {
        val bundle = Bundle().apply {
            putInt(ACTOR_ID, staffId)
        }
        findNavController().navigate(R.id.action_listPage_to_actorPage, args = bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}