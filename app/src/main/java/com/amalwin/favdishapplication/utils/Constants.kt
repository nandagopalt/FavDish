package com.amalwin.favdishapplication.utils

object Constants {
    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY = "DishCategory"
    const val DISH_COOKING_TIMELINE = "DishCookingTimeLine"

    const val DISH_IMAGE_SOURCE_LOCAL = "Local"
    const val DISH_IMAGE_SOURCE_REMOTE = "Remote"

    fun dishTypes(): ArrayList<String> {
        return listOf(
            "Breakfast",
            "Lunch",
            "Snakes",
            "Dinner",
            "Salad",
            "Side Dish",
            "Dessert",
            "Other"
        ).toList() as ArrayList<String>
    }

    fun dishCategories(): ArrayList<String> {
        return listOf(
            "Pizza",
            "BBQ",
            "Bakery",
            "Burger",
            "Cafe",
            "Chicken",
            "Dessert",
            "Drinks",
            "Hot Dogs",
            "Juices",
            "Sandwich",
            "Tea & Coffee",
            "Wraps",
            "Other"
        ).toList() as ArrayList<String>
    }

    fun dishCookTime(): ArrayList<String> {
        return listOf(
            "10",
            "20",
            "30",
            "40",
            "50",
            "60",
            "70",
            "80",
            "90",
            "180"
        ).toList() as ArrayList<String>
    }
}