package ru.kanogor.skillcinema.presentation.profilepage

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentProfileBinding
import ru.kanogor.skillcinema.presentation.adapters.collectioncard.CollectionCardAdapter
import ru.kanogor.skillcinema.presentation.adapters.savedfilm.SavedFilmAdapter

private const val COLLECTION_NAME = "collection_name"
private const val FILM_ID = "film_id"

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val viewModel: ProfileViewModel by viewModels()

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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Просмотрено

        val interestedFilmsAdapter = SavedFilmAdapter()
        val watchedFilmsAdapter = SavedFilmAdapter()
        binding.wasWatchedListProfile.setTitle(getString(R.string.watched_profile))
        binding.wasWatchedListProfile.setRecyclerView(watchedFilmsAdapter)
        watchedFilmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }

        // Было интересно

        binding.wasInterestedListProfile.setTitle(getString(R.string.was_interested_profile))
        binding.wasInterestedListProfile.setRecyclerView(interestedFilmsAdapter)
        interestedFilmsAdapter.onItemClick = { filmId -> moveToFilmPage(filmId) }

        // Все коллекции
        val collectionCardsAdapter = CollectionCardAdapter()
        binding.collectionCardsProfile.adapter = collectionCardsAdapter
        binding.createCollectionProfile.setOnClickListener {
            addCollectionDialog()
        }

        // Заполнение recyclerview
        lifecycleScope.launch {
            viewModel.collections.collect { collectionList ->
                collectionCardsAdapter.submitList(collectionList)
                collectionCardsAdapter.onItemClick =
                    { collectionName -> moveToListPage(collectionName) }
                collectionCardsAdapter.onCloseClick =
                    { collectionName -> viewModel.deleteCollection(collectionName) }
                collectionList.forEach { filmCollection ->
                    if (filmCollection.savedCollection.collectionName == viewModel.collectionsName.watched) {
                        with(binding.wasWatchedListProfile.showAllButton) {
                            text = getString(
                                R.string.count_button,
                                filmCollection.collectionFilms?.size
                            )
                            setOnClickListener {
                                moveToListPage(filmCollection.savedCollection.collectionName)
                            }
                        }
                        watchedFilmsAdapter.onButtonClick =
                            { viewModel.deleteAllFilms(filmCollection.savedCollection.collectionName) }
                        watchedFilmsAdapter.submitList(filmCollection.collectionFilms)
                    }
                    if (filmCollection.savedCollection.collectionName == viewModel.collectionsName.interested) {
                        with(binding.wasInterestedListProfile.showAllButton) {
                            text = getString(
                                R.string.count_button,
                                filmCollection.collectionFilms?.size
                            )
                            setOnClickListener {
                                moveToListPage(filmCollection.savedCollection.collectionName)
                            }
                        }
                        interestedFilmsAdapter.onButtonClick =
                            { viewModel.deleteAllFilms(filmCollection.savedCollection.collectionName) }
                        interestedFilmsAdapter.submitList(filmCollection.collectionFilms)
                    }
                }
            }
        }

    }

    private fun moveToFilmPage(id: Int) {
        val bundle = Bundle().apply {
            putInt(FILM_ID, id)
        }
        findNavController().navigate(R.id.action_profile_to_filmPage, args = bundle)
    }

    private fun moveToListPage(category: String) {
        val bundle = Bundle().apply {
            putString(COLLECTION_NAME, category)
        }
        findNavController().navigate(R.id.action_profile_to_profileList, args = bundle)
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

}