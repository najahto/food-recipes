package com.najahto.foodreceipes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.najahto.foodreceipes.models.FoodRecipe
import com.najahto.foodreceipes.utils.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}