package com.narae.sweetdiet

data class myJsonItems(
    val DESC_KOR: String,
    val NUTR_CONT1: String,
    val NUTR_CONT2: String,
    val NUTR_CONT3: String,
    val NUTR_CONT4: String,
    val NUTR_CONT5: String,
    val NUTR_CONT6: String,
    val NUTR_CONT7: String,
    val NUTR_CONT8: String,
    val NUTR_CONT9: String,
    val ANIMAL_PLANT: String
)
data class myJsonBody(
    val items: MutableList<myJsonItems>
)
data class JsonResponse(
    val body: myJsonBody
)

// Recipe
data class myJsonFoodsRecipe (
    val food_Nm: String,
    val food_Image_Address: String
)

data class myJsonItemsRecipe (
    val fd_Nm: String,
    val ckry_Sumry_Info: String,
    val food_List: MutableList<myJsonFoodsRecipe>
)

data class myJsonResponseRecipe (
    val list: MutableList<myJsonItemsRecipe>
)

data class JsonResponseRecipe(
    val response: myJsonResponseRecipe
)
