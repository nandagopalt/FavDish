package com.amalwin.favdishapplication.model.entities
import com.google.gson.annotations.SerializedName


object RandomDish {
    data class Recipes(
    @SerializedName("recipes")
    val recipes: List<Recipe>
)

data class Recipe(
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int,
    @SerializedName("analyzedInstructions")
    val analyzedInstructions: List<AnalyzedInstruction>,
    @SerializedName("cheap")
    val cheap: Boolean,
    @SerializedName("cookingMinutes")
    val cookingMinutes: Int,
    @SerializedName("creditsText")
    val creditsText: String,
    @SerializedName("cuisines")
    val cuisines: List<String>,
    @SerializedName("dairyFree")
    val dairyFree: Boolean,
    @SerializedName("diets")
    val diets: List<String>,
    @SerializedName("dishTypes")
    val dishTypes: List<String>,
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredient>,
    @SerializedName("gaps")
    val gaps: String,
    @SerializedName("glutenFree")
    val glutenFree: Boolean,
    @SerializedName("healthScore")
    val healthScore: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("instructions")
    val instructions: String,
    @SerializedName("lowFodmap")
    val lowFodmap: Boolean,
    @SerializedName("occasions")
    val occasions: List<String>,
    @SerializedName("originalId")
    val originalId: Any,
    @SerializedName("preparationMinutes")
    val preparationMinutes: Int,
    @SerializedName("pricePerServing")
    val pricePerServing: Double,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @SerializedName("servings")
    val servings: Int,
    @SerializedName("sourceName")
    val sourceName: String,
    @SerializedName("sourceUrl")
    val sourceUrl: String,
    @SerializedName("spoonacularSourceUrl")
    val spoonacularSourceUrl: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("sustainable")
    val sustainable: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("vegan")
    val vegan: Boolean,
    @SerializedName("vegetarian")
    val vegetarian: Boolean,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean,
    @SerializedName("veryPopular")
    val veryPopular: Boolean,
    @SerializedName("weightWatcherSmartPoints")
    val weightWatcherSmartPoints: Int
)

data class AnalyzedInstruction(
    @SerializedName("name")
    val name: String,
    @SerializedName("steps")
    val steps: List<Step>
)

data class ExtendedIngredient(
    @SerializedName("aisle")
    val aisle: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("consistency")
    val consistency: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("measures")
    val measures: Measures,
    @SerializedName("meta")
    val meta: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("nameClean")
    val nameClean: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("originalName")
    val originalName: String,
    @SerializedName("unit")
    val unit: String
)

data class Step(
    @SerializedName("equipment")
    val equipment: List<Equipment>,
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>,
    @SerializedName("length")
    val length: Length,
    @SerializedName("number")
    val number: Int,
    @SerializedName("step")
    val step: String
)

data class Equipment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("localizedName")
    val localizedName: String,
    @SerializedName("name")
    val name: String
)

data class Ingredient(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("localizedName")
    val localizedName: String,
    @SerializedName("name")
    val name: String
)

data class Length(
    @SerializedName("number")
    val number: Int,
    @SerializedName("unit")
    val unit: String
)

data class Measures(
    @SerializedName("metric")
    val metric: Metric,
    @SerializedName("us")
    val us: Us
)

data class Metric(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("unitLong")
    val unitLong: String,
    @SerializedName("unitShort")
    val unitShort: String
)

data class Us(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("unitLong")
    val unitLong: String,
    @SerializedName("unitShort")
    val unitShort: String
)
}