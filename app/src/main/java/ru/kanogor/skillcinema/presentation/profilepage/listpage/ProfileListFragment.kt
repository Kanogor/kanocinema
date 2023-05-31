package ru.kanogor.skillcinema.presentation.profilepage.listpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentProfileListBinding
import ru.kanogor.skillcinema.presentation.adapters.savedfilm.ListSavedFilmsAdapter
import ru.kanogor.skillcinema.presentation.profilepage.ProfileViewModel

private const val COLLECTION_NAME = "collection_name"
private const val FILM_ID = "film_id"

@AndroidEntryPoint
class ProfileListFragment : Fragment() {

    private var _binging: FragmentProfileListBinding? = null

    private val binding get() = _binging!!

    private val viewModel: ProfileViewModel by viewModels()

    private var collectionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            collectionName = it.getString(COLLECTION_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binging = FragmentProfileListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = collectionName

        val filmsAdapter = ListSavedFilmsAdapter()
        binding.recyclerView.adapter = filmsAdapter

        lifecycleScope.launch {
            viewModel.collections.collect { collectionList ->
                collectionList.forEach { filmCollection ->
                    if (filmCollection.savedCollection.collectionName == collectionName) {
                        filmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }
                        filmsAdapter.submitList(filmCollection.collectionFilms)
                    }
                }
            }
        }

    }

    private fun moveToFilmPage(id: Int) {
        val bundle = Bundle().apply {
            putInt(FILM_ID, id)
        }
        findNavController().navigate(R.id.action_profileList_to_filmPage, args = bundle)
    }

}