package com.najahto.foodreceipes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.najahto.foodreceipes.viewmodels.MainViewModel
import com.najahto.foodreceipes.R
import com.najahto.foodreceipes.adapters.RecipesAdapter
import com.najahto.foodreceipes.utils.Constants.Companion.API_KEY
import com.najahto.foodreceipes.utils.NetworkResult
import com.najahto.foodreceipes.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mView: View
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

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
        requestApiData()
        return mView
    }

    private fun setupRecipesRecyclerView() {
        mView.rvRecipesList.adapter = mAdapter
        mView.rvRecipesList.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()

    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
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

    private fun showShimmerEffect() {
        mView.rvRecipesList.visibility = View.GONE
        mView.shimmerFrameLayout.visibility = View.VISIBLE
    }

    private fun hideShimmerEffect() {
        mView.shimmerFrameLayout.visibility = View.GONE
        mView.rvRecipesList.visibility = View.VISIBLE
    }
}