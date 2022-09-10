package com.najahto.foodreceipes.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.najahto.foodreceipes.R
import com.najahto.foodreceipes.utils.Constants
import com.najahto.foodreceipes.viewmodels.RecipesViewModel
import kotlinx.android.synthetic.main.recipes_bottom_sheet.view.*
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "RecipesBottomSheet"
    }

    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypeChip = Constants.DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = Constants.DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.recipes_bottom_sheet, container, false)

        // Get meal type & diet type from datastore preferences & update chips
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, mView.chipGroupMealType)
            updateChip(value.selectedDietTypeId, mView.chipGroupDietType)
        }

        mView.chipGroupMealType.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip: Chip? = group.findViewById(group.checkedChipId)
            val selectedMealType = chip?.text.toString().lowercase(Locale.ROOT)
            Log.d(TAG, "onCreateView: selectedMealType - $selectedMealType")
            mealTypeChip = selectedMealType
            mealTypeChipId = group.checkedChipId
            Log.d(TAG, "onCreateView: mealTypeChipId - $mealTypeChipId")
        }

        mView.chipGroupDietType.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip: Chip? = group.findViewById(group.checkedChipId)
            val selectedDietType = chip?.text.toString().lowercase(Locale.ROOT)
            Log.d(TAG, "onCreateView: selectedDietType - $selectedDietType")
            dietTypeChip = selectedDietType
            dietTypeChipId = group.checkedChipId
            Log.d(TAG, "onCreateView: dietTypeChipId - $dietTypeChipId")
        }

        // apply filter
        mView.btnApply.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            val action =
                RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        return mView
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d(TAG, "updateChip: exception ${e.message.toString()}")
            }
        }
    }

}