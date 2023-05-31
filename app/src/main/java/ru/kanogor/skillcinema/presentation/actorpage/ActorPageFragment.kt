package ru.kanogor.skillcinema.presentation.actorpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentActorPageBinding
import ru.kanogor.skillcinema.presentation.adapters.stafffilms.StaffFilmsAdapter

private const val ACTOR_ID = "actor_id"
private const val FILM_ID = "film_id"

@AndroidEntryPoint
class ActorPageFragment : Fragment() {

    private var _binding: FragmentActorPageBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ActorPageViewModel by viewModels()

    private var actorId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            actorId = it.getInt(ACTOR_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = ""

        viewModel.isLoading.onEach {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.actorPageGroup.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.actorPageGroup.visibility = View.VISIBLE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.loadInfo(actorId!!)

        viewModel.personInfo.onEach { info ->
            with(binding) {
                name.text = if (info?.nameRu == null) info?.nameEn else info.nameRu
                profession.text = info?.profession
                countOfFilms.text = resources.getQuantityString(
                    R.plurals.count_of_films_actorpage,
                    info?.films?.size ?: 0, info?.films?.size ?: 0
                )
                toListTextButton.setOnClickListener {
                    moveToFilmographyPage(actorId!!)
                }
                countOfFilms.setOnClickListener {
                    moveToFilmographyPage(actorId!!)
                }
            }
            Glide.with(binding.photo.context)
                .load(info?.posterUrl)
                .placeholder(R.drawable.no_poster)
                .into(binding.photo)

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Лучшее
        val bestFilmsAdapter = StaffFilmsAdapter()
        with(binding.bestFilmsList) {
            showAllButton.text = getString(R.string.all_button_actorpage)
            showAllButton.setOnClickListener {
                moveToFilmographyPage(actorId!!)
            }
            setTitle(getString(R.string.best))
            setRecyclerView(bestFilmsAdapter)
        }

        bestFilmsAdapter.onItemClick = { film -> moveToFilmPage(film.kinopoiskId) }
        bestFilmsAdapter.onButtonClick = { moveToFilmographyPage(actorId!!) }

        viewModel.bestFilms.onEach {
            bestFilmsAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun moveToFilmographyPage(id: Int) {
        val bundle = Bundle().apply {
            putInt(ACTOR_ID, id)
        }
        findNavController().navigate(R.id.action_actorPage_to_filmography, args = bundle)
    }

    private fun moveToFilmPage(id: Int) {
        val bundle = Bundle().apply {
            putInt(FILM_ID, id)
        }
        findNavController().navigate(R.id.action_actorPage_to_filmPage, args = bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}