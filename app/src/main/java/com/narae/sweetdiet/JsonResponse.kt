package com.narae.sweetdiet

data class myJsonItems(val DESC_KOR: String, val NUTR_CONT1: String, val NUTR_CONT2: String, val NUTR_CONT3: String, val NUTR_CONT4: String, val NUTR_CONT5: String, val NUTR_CONT6: String, val NUTR_CONT7: String, val NUTR_CONT8: String, val NUTR_CONT9: String, val ANIMAL_PLANT: String)
data class myJsonBody(val items: MutableList<myJsonItems>)
data class JsonResponse(val body: myJsonBody)
