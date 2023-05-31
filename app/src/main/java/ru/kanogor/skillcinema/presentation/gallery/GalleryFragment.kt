package ru.kanogor.skillcinema.presentation.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kanogor.skillcinema.R
import ru.kanogor.skillcinema.databinding.FragmentGalleryBinding
import ru.kanogor.skillcinema.presentation.adapters.gallery.PicturesPagingAdapter
import ru.kanogor.skillcinema.presentation.adapters.gallery.ViewPagerAdapter

private const val FILM_ID_NAME = "film_id_name"

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var filmId: Int? = null

    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            filmId = it.getInt(FILM_ID_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.gallery)

        galleryViewModel.loadPosterPictures(filmId!!)
        galleryViewModel.loadGalleryInfo(filmId!!)

        galleryViewModel.isLoading.onEach {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.pageLayout.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.pageLayout.visibility = View.VISIBLE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        val galleryAdapter = PicturesPagingAdapter()
        val viewPagerAdapter = ViewPagerAdapter()

        // Chip titles
        setChipTitle(
            galleryViewModel.posterItemsCount,
            binding.posterChipGallery,
            R.string.poster_chip_title
        )
        setChipTitle(
            galleryViewModel.conceptItemsCount,
            binding.conceptChipGallery,
            R.string.concept_chip_title
        )
        setChipTitle(
            galleryViewModel.coverItemsCount,
            binding.coverChipGallery,
            R.string.cover_chip_title
        )
        setChipTitle(
            galleryViewModel.promoItemsCount,
            binding.promoChipGallery,
            R.string.promo_chip_title
        )
        setChipTitle(
            galleryViewModel.fanArtItemsCount,
            binding.fanArtChipGallery,
            R.string.fanArt_chip_title
        )
        setChipTitle(
            galleryViewModel.screenshotItemsCount,
            binding.screenshotChipGallery,
            R.string.screenshot_chip_title
        )
        setChipTitle(
            galleryViewModel.shootingItemsCount,
            binding.shootingChipGallery,
            R.string.shooting_chip_title
        )
        setChipTitle(
            galleryViewModel.stillItemsCount,
            binding.stillChipGallery,
            R.string.still_chip_title
        )
        setChipTitle(
            galleryViewModel.wallpaperItemsCount,
            binding.wallpaperChipGallery,
            R.string.wallpaper_chip_title
        )

        binding.viewPager.adapter = viewPagerAdapter
        galleryAdapter.onItemClick = { position ->
            viewPagerAdapter.submitList(galleryAdapter.snapshot().items)
            binding.viewPager.setCurrentItem(position, false)
            binding.viewPager.visibility = View.VISIBLE
        }
        viewPagerAdapter.onItemClick = {
            binding.viewPager.visibility = View.GONE
        }

        binding.posterChipGallery.isChecked
        binding.recyclerView.adapter = galleryAdapter
        galleryViewModel.pagingPosterPictures.onEach {
            galleryAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.chipGroupGallery.setOnCheckedStateChangeListener { group, _ ->
            when (group.checkedChipId) {
                R.id.concept_chip_gallery -> {
                    galleryViewModel.loadConceptPictures(filmId!!)
                    galleryViewModel.pagingConceptPictures.onEach {
                        galleryAdapter.submitData(it)
                        if (galleryAdapter.snapshot().items.isEmpty()) nonPicturesToast()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.cover_chip_gallery -> {
                    galleryViewModel.loadCoverPictures(filmId!!)
                    galleryViewModel.pagingCoverPictures.onEach {
                        galleryAdapter.submitData(it)
                        if (galleryAdapter.snapshot().items.isEmpty()) nonPicturesToast()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.poster_chip_gallery -> {
                    galleryViewModel.loadPosterPictures(filmId!!)
                    galleryViewModel.pagingPosterPictures.onEach {
                        galleryAdapter.submitData(it)
                        if (galleryAdapter.snapshot().items.isEmpty()) nonPicturesToast()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.promo_chip_gallery -> {
                    galleryViewModel.loadPromoPictures(filmId!!)
                    galleryViewModel.pagingPromoPictures.onEach {
                        galleryAdapter.submitData(it)
                        if (galleryAdapter.snapshot().items.isEmpty()) nonPicturesToast()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.fanArt_chip_gallery -> {
                    galleryViewModel.loadFanArtPictures(filmId!!)
                    galleryViewModel.pagingFanArtPictures.onEach {
                        galleryAdapter.submitData(it)
                        if (galleryAdapter.snapshot().items.isEmpty()) nonPicturesToast()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.screenshot_chip_gallery -> {
                    galleryViewModel.loadScreenshotPictures(filmId!!)
                    galleryViewModel.pagingScreenshotPictures.onEach {
                        galleryAdapter.submitData(it)
                        if (galleryAdapter.snapshot().items.isEmpty()) nonPicturesToast()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.shooting_chip_gallery -> {
                    galleryViewModel.loadShootingPictures(filmId!!)
                    galleryViewModel.pagingShootingPictures.onEach {
                        galleryAdapter.submitData(it)
                        if (galleryAdapter.snapshot().items.isEmpty()) nonPicturesToast()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.still_chip_gallery -> {
                    galleryViewModel.loadStillPictures(filmId!!)
                    galleryViewModel.pagingStillPictures.onEach {
                        galleryAdapter.submitData(it)
                        if (galleryAdapter.snapshot().items.isEmpty()) nonPicturesToast()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                R.id.wallpaper_chip_gallery -> {
                    galleryViewModel.loadWallpaperPictures(filmId!!)
                    galleryViewModel.pagingWallpaperPictures.onEach {
                        galleryAdapter.submitData(it)
                        if (galleryAdapter.snapshot().items.isEmpty()) nonPicturesToast()
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                else -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.choose_gallery),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun nonPicturesToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.no_photo_gallery),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setChipTitle(stateFlow: StateFlow<Int>, chip: Chip, res: Int) {
        stateFlow.onEach {
            chip.text = getString(res, it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
