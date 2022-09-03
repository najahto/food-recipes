package com.najahto.foodreceipes.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.najahto.foodreceipes.R
import com.najahto.foodreceipes.viewmodels.MainViewModel
import com.najahto.foodreceipes.adapters.RecipesAdapter
import com.najahto.foodreceipes.utils.NetworkResult
import com.najahto.foodreceipes.utils.observeOnce
import com.najahto.foodreceipes.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    companion object {
        private const val TAG = "RecipesFragment"
    }

    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)

        setupRecipesRecyclerView()
        readDatabase()
        return mView
    }

    private fun setupRecipesRecyclerView() {
        mView.rvRecipesList.adapter = mAdapter
        mView.rvRecipesList.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()

    }

    private fun readDatabase() {
        Log.d(TAG, "readDatabase: called!")
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.d(TAG, "readDatabase: db not empty")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    // Get data from API
    private fun requestApiData() {
        Log.d(TAG, "requestApiData: called!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Laoding -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showShimmerEffect()
    }

    override fun onPause() {
        super.onPause()
        hideShimmerEffect()
    }

    private fun showShimmerEffect() {
        mView.rvRecipesList.visibility = View.GONE
        mView.shimmerFrameLayout.visibility = View.VISIBLE
    }

    private fun hideShimmerEffect() {
        mView.shimmerFrameLayout.visibility = View.GONE
        mView.rvRecipesList.visibility = View.VISIBLE
    }

}