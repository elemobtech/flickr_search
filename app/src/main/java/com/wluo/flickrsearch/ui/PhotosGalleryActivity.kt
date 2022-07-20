package com.wluo.flickrsearch.ui

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wluo.flickrsearch.R
import com.wluo.flickrsearch.databinding.ActivityPhotoGalleryBinding
import com.wluo.flickrsearch.model.GalleryItem
import com.wluo.flickrsearch.request.ThumbnailDownloader
import com.wluo.flickrsearch.viewmodel.PhotoGalleryViewModel

private const val TAG = "GalleryActivity"

class PhotosGalleryActivity : AppCompatActivity() {
    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoAdapter.PhotoHolder>
    private lateinit var binding: ActivityPhotoGalleryBinding
    private val adapter:PhotoAdapter = PhotoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_gallery)
        binding.photoRecyclerView.layoutManager = GridLayoutManager(this, 3)
        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
        val responseHandler = Handler()
        thumbnailDownloader =
            ThumbnailDownloader(responseHandler) { photoHolder, bitmap ->
                val drawable = BitmapDrawable(resources, bitmap)
                photoHolder.bindDrawable(drawable)
            }
        lifecycle.addObserver(thumbnailDownloader.lifeCycleObserver)

        binding.photoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.photoRecyclerView.canScrollVertically(1)) {
                    photoGalleryViewModel.page += 1
                    photoGalleryViewModel.fetchNewPage()
                }
            }
        })

        binding.photoRecyclerView.adapter = adapter
        adapter.thumbDownloader = thumbnailDownloader
        photoGalleryViewModel.galleryItemLiveData.observe(this) { galleryItems: ArrayList<GalleryItem> ->
            adapter.setItemList(galleryItems)
            binding.photoRecyclerView.alpha = 1f
            binding.pBPhotos.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(
            thumbnailDownloader.lifeCycleObserver
        )
        thumbnailDownloader.clearQueue()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.photo_gallery, menu)

        val searchItem : MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.apply{
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $queryText")
                    photoGalleryViewModel.page = 1
                    adapter.cleanList()
                    photoGalleryViewModel.fetchPhotos(queryText)

                    searchView.onActionViewCollapsed()

                    binding.photoRecyclerView.alpha = 0f
                    binding.pBPhotos.visibility = View.VISIBLE
                    return false
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextChange: $queryText")
                    return false
                }
            })

            setOnSearchClickListener {
                searchView.setQuery(photoGalleryViewModel.searchTerm, false)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }
}