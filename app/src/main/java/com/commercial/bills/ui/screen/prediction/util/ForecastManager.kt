package com.commercial.bills.ui.screen.prediction.util

object ForecastManager {
    fun forecastValues(values: List<Float>, steps: Int) : List<Float> {
        if(values.isEmpty()) {
            val listOfZeros = mutableListOf<Float>()
            for(i in 1..steps) {
                listOfZeros.add(0f)
            }
            return listOfZeros
        }

        val summedValuesAvgList: MutableList<Float> = mutableListOf()

        for(i in 1..steps) {
            if(summedValuesAvgList.isEmpty()) {
                summedValuesAvgList.add(values.average().toFloat())
            }
            else {
                val offset = 100f
                val newList = values.toMutableList()
                newList.addAll(summedValuesAvgList)
                val avg = if(i >= 2 && values.count() >= steps) {
                    if(values[i-1] > values[i-2]){
                        newList.average().toFloat() + offset
                    } else {
                        newList.average().toFloat() - offset
                    }
                } else {
                    newList.average().toFloat() + offset / 2f
                }
                summedValuesAvgList.add(avg)
            }
        }

        return summedValuesAvgList
    }
}