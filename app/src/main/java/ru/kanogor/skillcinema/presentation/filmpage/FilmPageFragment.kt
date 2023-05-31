package ru.kanogor.skillcinema.presentation.filmpage

import android.animation.LayoutTransition
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentFilmPageBinding
import ru.kanogor.skillcinema.presentation.adapters.checkcollection.CheckBoxCollectionAdapter
import ru.kanogor.skillcinema.presentation.adapters.gallery.ViewPagerAdapter
import ru.kanogor.skillcinema.presentation.adapters.gallery.GalleryLimitedAdapter
import ru.kanogor.skillcinema.presentation.adapters.idfilms.IdFilmsAdapter
import ru.kanogor.skillcinema.presentation.adapters.staff.ActorsListAdapter
import ru.kanogor.skillcinema.presentation.adapters.staff.ProfessionsListAdapter
import ru.kanogor.skillcinema.presentation.listpage.ListPageFragment.Companion.ACTOR_PAGE
import ru.kanogor.skillcinema.presentation.listpage.ListPageFragment.Companion.SIMILAR_FILMS
import ru.kanogor.skillcinema.presentation.listpage.ListPageFragment.Companion.STAFF_PAGE
import ru.kanogor.skillcinema.presentation.viewgroups.MovieItemHorizontal

private const val FILM_ID = "film_id"
private const val ACTOR_ID = "actor_id"
private const val ADAPTER_NAME = "adapter_name"
private const val FILM_ID_NAME = "film_id_name"
private const val FILM_NAME = "film_name"

@AndroidEntryPoint
class FilmPageFragment : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null

    private val binding get() = _binding!!

    private val viewModel: FilmPageViewModel by viewModels()

    private var filmId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filmId = it.getInt(FILM_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = ""

        var isItClicked = false
        var isSoap: Boolean
        applyLayoutTransition()

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
        Log.d("film_id", filmId.toString())

        viewModel.isFilmLiked.onEach {
            if (it) {
                binding.heartButtonFilmpage.setImageResource(R.drawable.ic_heart)
            } else {
                binding.heartButtonFilmpage.setImageResource(R.drawable.ic_slash_heart)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isFilmWatched.onEach {
            if (it) {
                binding.eyeButtonFilmpage.setImageResource(R.drawable.ic_eye)

            } else {
                binding.eyeButtonFilmpage.setImageResource(R.drawable.ic_slash_eye)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isFilmWantWatched.onEach {
            isInfoLoad(isLoad = it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Actors
        val actorsAdapter = ActorsListAdapter()
        with(binding.actorsGroup) {
            setTitle(getString(R.string.actors_list_title))
            setGridLayoutManager(GRID_SPAN_ACTOR)
            setRecyclerView(actorsAdapter)
            showAllButton.setOnClickListener {
                moveToListPage(ACTOR_PAGE, filmId!!)
            }
        }

        actorsAdapter.onItemClick = { actorId -> moveToActorPage(actorId) }

        viewModel.actorsStaff.onEach { list ->
            binding.actorsGroup.showAllButton.text = getString(R.string.count_button, list.size)
            actorsAdapter.submitList(list)
            if (list.isEmpty()) binding.actorsGroup.visibility = View.GONE
            else binding.actorsGroup.visibility = View.VISIBLE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // ProfessionStaff
        val professionStaffAdapter = ProfessionsListAdapter()
        with(binding.professionsStaffGroup) {
            setTitle(getString(R.string.staff_list_title))
            setGridLayoutManager(GRID_SPAN_STAFF)
            setRecyclerView(professionStaffAdapter)
            showAllButton.setOnClickListener {
                moveToListPage(STAFF_PAGE, filmId!!)
            }
        }

        professionStaffAdapter.onItemClick = { staffId -> moveToActorPage(staffId) }

        viewModel.professionStaff.onEach { list ->
            binding.professionsStaffGroup.showAllButton.text =
                getString(R.string.count_button, list.size)
            professionStaffAdapter.submitList(list)
            if (list.isEmpty()) binding.professionsStaffGroup.visibility = View.GONE
            else binding.professionsStaffGroup.visibility = View.VISIBLE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Gallery
        val galleryAdapter = GalleryLimitedAdapter()
        val viewPagerAdapter = ViewPagerAdapter()
        viewModel.loadGalleryInfo(filmId!!)
        with(binding.galleryGroup) {
            setTitle(getString(R.string.gallery))
            setGridLayoutManager(1)
            setRecyclerView(galleryAdapter)
            showAllButton.setOnClickListener {
                moveToGalleryPage(filmId!!)
            }
        }

        binding.viewPager.adapter = viewPagerAdapter

        galleryAdapter.onItemClick = { position ->
            binding.viewPager.setCurrentItem(position, false)
            binding.viewPager.visibility = View.VISIBLE
        }
        viewPagerAdapter.onItemClick = {
            binding.viewPager.visibility = View.GONE
        }

        viewModel.galleryPictures.onEach { list ->
            galleryAdapter.submitList(list)
            viewPagerAdapter.submitList(list)
            if (list.isEmpty()) binding.galleryGroup.visibility = View.GONE
            else binding.galleryGroup.visibility = View.VISIBLE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.galleryItemsCount.onEach {
            binding.galleryGroup.showAllButton.text =
                getString(R.string.count_button, it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //SimilarFilms
        val similarFilmsAdapter = IdFilmsAdapter()
        with(binding.similarFilmsGroup) {
            setTitle(getString(R.string.similar_films))
            setRecyclerView(similarFilmsAdapter)
            showAllButton.setOnClickListener {
                moveToListPage(SIMILAR_FILMS, filmId!!)
            }
        }

        similarFilmsAdapter.onItemClick = { film -> viewModel.loadInfo(film.kinopoiskId) }

        viewModel.similarFilms.onEach { list ->
            binding.similarFilmsGroup.showAllButton.text =
                getString(R.string.count_button, list.size)
            similarFilmsAdapter.submitList(list)
            if (list.isEmpty()) binding.similarFilmsGroup.visibility = View.GONE
            else binding.similarFilmsGroup.visibility = View.VISIBLE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //FilmPoster
        viewModel.film.onEach { film ->
            val titleText = if (film?.nameRu == null) film?.nameOriginal else film.nameRu
            val genresText = film?.genres?.joinToString(separator = ", ", limit = 3) { it.genre }
            isSoap = if (film?.serial == true) {
                viewModel.loadSeasonsInfo(filmId!!)
                binding.soapInfoGroup.visibility = ViewGroup.VISIBLE
                true
            } else {
                binding.soapInfoGroup.visibility = ViewGroup.GONE
                false
            }
            if (isSoap) {  //информация о сериале
                viewModel.seasonsInfo.onEach { info ->
                    val countOfSeasonsAndSeries: String =
                        try {   // на случай отсутствия информации о сериях и сезонах, особенно в премьерах
                            resources.getQuantityString(
                                R.plurals.count_seasons_group,
                                info?.total ?: 0, info?.total ?: 0
                            ) + resources.getQuantityString(
                                R.plurals.count_series_group,
                                info?.items?.get(0)?.episodes?.size ?: 0,
                                info?.items?.get(0)?.episodes?.size ?: 0
                            )
                        } catch (e: IndexOutOfBoundsException) {
                            resources.getQuantityString(
                                R.plurals.count_seasons_group,
                                info?.total ?: 0, info?.total ?: 0
                            ) + resources.getQuantityString(
                                R.plurals.count_series_group, 0, 0
                            )
                        }
                    with(binding) {
                        soapsSeasons.text = resources.getQuantityString(
                            R.plurals.count_seasons_poster,
                            info?.total ?: 0, info?.total ?: 0
                        )
                        numberOfSeasons.text = countOfSeasonsAndSeries
                        numberOfSeasons.setOnClickListener {
                            moveToSeasonsPage(filmId!!, titleText!!)
                        }
                        allSeasonsTextButton.setOnClickListener {
                            moveToSeasonsPage(filmId!!, titleText!!)
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
            // общая информация
            if (film?.shortDescription == null) binding.shortDescription.visibility =
                View.GONE else binding.shortDescription.visibility = View.VISIBLE
            if (film?.description == null) binding.description.visibility =
                View.GONE else binding.description.visibility = View.VISIBLE
            val ratingText =
                if (film?.ratingKinopoisk == null) film?.ratingImdb else film.ratingKinopoisk
            val ageRestrictionText = if (film?.ratingAgeLimits == null) "" else ", ${
                film.ratingAgeLimits?.replace(
                    "age",
                    ""
                ) + "+"
            }"
            val yearText =
                if (film?.year == null) "" else getString(R.string.filmpage_year, film.year)
            val longDescription =
                film?.description                           // полное описание фильма
            val cutDescription =
                if (longDescription == null || longDescription.length <= 250) longDescription
                else longDescription.substring(0, 250) + "..."   // ограниченное в 250 символов
            with(binding) {
                title.text = titleText
                rating.text = if (ratingText == null) "" else "$ratingText, "
                year.text = yearText
                genres.text = genresText
                country.text =
                    film?.countries?.joinToString(separator = ", ", limit = 3) { it.country }
                filmLength.text =
                    if (film?.filmLength == null) "" else getString(
                        R.string.filmpage_leight,
                        film.filmLength.toString()
                    )
                ageRestriction.text = ageRestrictionText
                description.text = cutDescription
                description.setOnClickListener {
                    if (isItClicked) {
                        binding.description.text = cutDescription
                    } else {
                        binding.description.text = longDescription
                    }
                    isItClicked = !isItClicked
                }
                shortDescription.text = film?.shortDescription
            }
            Glide.with(binding.poster.context)
                .load(film?.posterUrl)
                .placeholder(R.drawable.no_poster)
                .into(binding.poster)

            viewModel.checkIcons(
                id = filmId!!,
                name = titleText.toString(),
                genres = genresText.toString(),
                rating = ratingText,
                url = film?.posterUrl.toString()
            )

            binding.shareButtonFilmpage.setOnClickListener {
                sareButtonAction(film?.imdbId, film?.kinopoiskId.toString())
            }

            binding.heartButtonFilmpage.setOnClickListener {
                viewModel.onIconButtonClick(viewModel.collectionsName.liked)
            }
            binding.eyeButtonFilmpage.setOnClickListener {
                viewModel.onIconButtonClick(viewModel.collectionsName.watched)
            }
            binding.bookmarkButtonFilmpage.setOnClickListener {
                viewModel.onIconButtonClick(viewModel.collectionsName.wantWatch)
            }
            binding.pointsButtonFilmpage.setOnClickListener {
                configCollectionBottomDialog(
                    title = titleText.toString(),
                    year = yearText,
                    genres = genresText.toString(),
                    rating = ratingText,
                    url = film?.posterUrl.toString()
                )
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }
    private companion object {
        private const val GRID_SPAN_ACTOR = 4
        private const val  GRID_SPAN_STAFF = 2
    }

    fun sareButtonAction(imdbId: String?, kinopoiskId: String?) {
        val link =
            if (imdbId == null) getString(R.string.kinopoisk_ru) + kinopoiskId + "/"  // добавлена ссылка на Кинопоиск, так как не все фильмы присутствуют на imdb.com
            else {
                getString(R.string.imdb_com_t) + imdbId + "/"
            }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Look at this movie")
        startActivity(shareIntent)
    }
    private fun applyLayoutTransition() {
        val layoutTransition = LayoutTransition()
        layoutTransition.setDuration(300)
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.rootContainer.layoutTransition = layoutTransition
    }

    private fun configCollectionBottomDialog(
        title: String,
        year: String,
        genres: String,
        rating: Double?,
        url: String
    ) {
        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_dialog_filmpage)
        dialog.setCancelable(true)
        dialog.dismissWithAnimation
        val closeButton = dialog.findViewById<AppCompatImageView>(R.id.close_dialog_profile)
        val movieItem = dialog.findViewById<MovieItemHorizontal>(R.id.movie_item_horizontal)
        val recycler = dialog.findViewById<RecyclerView>(R.id.check_box_recyclerview)
        val createCollection =
            dialog.findViewById<AppCompatTextView>(R.id.create_collection_profile)
        movieItem?.setTitle(title)
        movieItem?.setSubtitle(year, genres)
        movieItem?.setRating(rating)
        movieItem?.setPicture(url)

        val adapter = CheckBoxCollectionAdapter(filmId!!)
        recycler?.adapter = adapter
        viewModel.savedCollections.onEach {
            adapter.submitList(it)
            adapter.needAdd = { collectionName ->
                viewModel.checkFilm(
                    collectionName,
                    filmId!!,
                    title,
                    genres,
                    rating,
                    url
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        createCollection?.setOnClickListener {
            addCollectionDialog()
        }

        closeButton?.setOnClickListener {
            viewModel.checkIcons(filmId!!, title, genres, rating, url)
            dialog.cancel()
        }
        dialog.show()
    }

    private fun addCollectionDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_collection)
        dialog.setCancelable(false)
        val editText = dialog.findViewById<AppCompatEditText>(R.id.edittext_dialog_profile)
        val doneButton = dialog.findViewById<AppCompatButton>(R.id.done_dialog_profile)
        val closeButton = dialog.findViewById<AppCompatImageView>(R.id.close_error)
        closeButton?.setOnClickListener {
            dialog.cancel()
        }
        doneButton.setOnClickListener {
            val collectionName = editText.text.toString()
            viewModel.addCollection(collectionName)
            viewModel.collectionAdded.onEach {
                if (it == collectionName) {
                    errorBottomDialog()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            dialog.cancel()
        }
        dialog.show()
    }

    private fun errorBottomDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_error_message)
        dialog.setCancelable(true)
        dialog.dismissWithAnimation
        val closeButton = dialog.findViewById<AppCompatImageView>(R.id.close_error)
        closeButton?.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }

    private fun moveToListPage(category: String, filmId: Int) {
        val bundle = Bundle().apply {
            putString(ADAPTER_NAME, category)
            putInt(FILM_ID_NAME, filmId)
        }
        findNavController().navigate(R.id.action_filmPage_to_listPage, args = bundle)
    }

    private fun moveToActorPage(staffId: Int) {
        val bundle = Bundle().apply {
            putInt(ACTOR_ID, staffId)
        }
        findNavController().navigate(R.id.action_filmPage_to_actorPage, args = bundle)
    }

    private fun moveToSeasonsPage(filmId: Int, filmName: String) {
        val bundle = Bundle().apply {
            putString(FILM_NAME, filmName)
            putInt(FILM_ID_NAME, filmId)
        }
        findNavController().navigate(R.id.action_filmPage_to_seasons, args = bundle)
    }

    private fun moveToGalleryPage(filmId: Int) {
        val bundle = Bundle().apply {
            putInt(FILM_ID_NAME, filmId)
        }
        findNavController().navigate(R.id.action_filmPage_to_gallery, args = bundle)
    }

    private fun isInfoLoad(isLoad: Boolean) {
        if (isLoad) {
            binding.progressBar.visibility = View.VISIBLE
            binding.pageLayout.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.pageLayout.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}