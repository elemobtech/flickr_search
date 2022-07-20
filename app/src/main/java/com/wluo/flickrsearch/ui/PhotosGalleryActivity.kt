package com.wluo.flickrsearch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wluo.flickrsearch.R
import com.wluo.flickrsearch.databinding.ActivityPhotoGalleryBinding
import com.wluo.flickrsearch.model.GalleryItem
import com.wluo.flickrsearch.storage.QueryPreferences
import com.wluo.flickrsearch.viewmodel.PhotoGalleryViewModel

private const val TAG = "GalleryActivity"

class PhotosGalleryActivity : AppCompatActivity() {
    private lateinit var viewModel: PhotoGalleryViewModel
    private lateinit var binding: ActivityPhotoGalleryBinding
    private val adapter:PhotoAdapter = PhotoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_gallery)
        binding.photoRecyclerView.layoutManager = GridLayoutManager(this, 3)
        viewModel = ViewModelProvider(this,
            MyViewModelFactory(QueryPreferences.getStoredQuery(applicationContext)))
            .get(PhotoGalleryViewModel::class.java)

        binding.photoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.photoRecyclerView.canScrollVertically(1)) {
                    viewModel.page += 1
                    viewModel.fetchNewPage()
                }
            }
        })

        binding.photoRecyclerView.adapter = adapter
        viewModel.galleryItemLiveData.observe(this) { galleryItems: ArrayList<GalleryItem> ->
            adapter.setItemList(galleryItems)
            binding.photoRecyclerView.alpha = 1f
            binding.pBPhotos.visibility = View.GONE
        }
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
                    viewModel.page = 1
                    viewModel.queryText = queryText
                    QueryPreferences.setStoredQuery(applicationContext, queryText)
                    adapter.cleanList()
                    viewModel.fetchPhotos()

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
                searchView.setQuery(viewModel.queryText, false)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    inner class MyViewModelFactory(
        private val queryText: String
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T: ViewModel> create(modelClass:Class<T>): T {
            return PhotoGalleryViewModel(queryText) as T
        }
    }
}