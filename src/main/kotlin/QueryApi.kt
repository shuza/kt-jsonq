import com.google.gson.JsonArray
import com.google.gson.JsonObject

interface QueryApi {
    fun orWhereEq(key: String, value: Any): QueryApi
    fun orWhereNotEq(key: String, value: Any): QueryApi
    fun orWhereNull(key: String): QueryApi
    fun orWhereNotNull(key: String): QueryApi
    fun orWhereLess(key: String, value: Any): QueryApi
    fun orWhereLessEq(key: String, value: Any): QueryApi
    fun orWhereGreater(key: String, value: Any): QueryApi
    fun orWhereGreaterEq(key: String, value: Any): QueryApi
    fun orWhereStartsWith(key: String, value: String): QueryApi
    fun orWhereEndsWith(key: String, value: String): QueryApi
    fun orWhereContains(key: String, value: String): QueryApi

    fun andWhereEq(key: String, value: Any): QueryApi
    fun andWhereNotEq(key: String, value: Any): QueryApi
    fun andWhereNull(key: String): QueryApi
    fun andWhereNotNull(key: String): QueryApi
    fun andWhereLess(key: String, value: Any): QueryApi
    fun andWhereLessEq(key: String, value: Any): QueryApi
    fun andWhereGreater(key: String, value: Any): QueryApi
    fun andWhereGreaterEq(key: String, value: Any): QueryApi
    fun andWhereStartsWith(key: String, value: String): QueryApi
    fun andWhereEndsWith(key: String, value: String): QueryApi
    fun andWhereContains(key: String, value: String): QueryApi

    fun get(): JsonArray
    fun sum(key: String): Double
    fun max(key: String): Double
    fun min(key: String): Double
    fun avg(key: String): Double
    fun count(): Int
    fun size(): Int
    fun first(): JsonObject
    fun last(): JsonObject
    fun nth(position: Int): JsonObject
    fun exists(): Boolean
}