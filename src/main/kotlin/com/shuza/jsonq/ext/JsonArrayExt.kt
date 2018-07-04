package com.shuza.jsonq.ext

import com.google.gson.JsonArray
import com.shuza.jsonq.QueryHelper
import com.shuza.jsonq.models.QueryModel
import io.reactivex.Single

/**
 * :=  created by:  Shuza
 * :=  create date:  04-Jul-18
 * :=  (C) CopyRight Shuza
 * :=  www.shuza.ninja
 * :=  shuza.sa@gmail.com
 * :=  Fun  :  Coffee  :  Code
 **/


/**
 *  iterate through JsonArray and test conditions
 *  returns result that satisfied conditions
 */
fun JsonArray.get(conditions: ArrayList<QueryModel>): JsonArray {
    if (conditions.isEmpty()) {
        return this
    }

    val result = JsonArray()
    val queryHelper = QueryHelper()
    this
            .map { it.asJsonObject }
            .forEach {
                if (queryHelper.processQuery(it, conditions)) {
                    result.add(it)
                }
            }

    return result
}

fun JsonArray.rcGet(conditions: ArrayList<QueryModel>) = Single.fromCallable {
    this.get(conditions)
}

/**
 *  returns size of the result
 */
fun JsonArray.count(): Int {
    return this.size()
}

fun JsonArray.rxCount() = Single.fromCallable { this.count() }
/**
 *  summation of all values
 */
fun JsonArray.sum(key: String): Double {
    var sum = 0.0
    this
            .map { it.asJsonObject }
            .forEach {
                try {
                    sum += it.get(key).asDouble
                } catch (e: Exception) {
                }
            }
    return sum
}

fun JsonArray.rxSum(key: String) = Single.fromCallable { this.sum(key) }

/**
 *  find maximum value
 */
fun JsonArray.max(key: String): Double {
    var max = Double.MIN_VALUE
    this
            .map { it.asJsonObject }
            .forEach {
                try {
                    val value = it.asJsonObject.get(key).asDouble
                    max = if (value > max) value else max
                } catch (e: Exception) {
                }
            }
    return max
}

fun JsonArray.rxMax(key: String) = Single.fromCallable { this.max(key) }

/**
 *  find minimum value
 */
fun JsonArray.min(key: String): Double {
    var min = Double.MAX_VALUE
    this
            .map { it.asJsonObject }
            .forEach {
                try {
                    val value = it.asJsonObject.get(key).asDouble
                    min = if (value < min) value else min
                } catch (e: Exception) {
                }
            }
    return min
}

fun JsonArray.rxMin(key: String) = Single.fromCallable { this.min(key) }

/**
 *  find average value
 */
fun JsonArray.avg(key: String): Double {
    return (sum(key) / count())
}

fun JsonArray.rxAvg(key: String) = Single.fromCallable { this.avg(key) }
