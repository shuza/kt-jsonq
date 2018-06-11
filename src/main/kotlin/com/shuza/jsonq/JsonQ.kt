package com.shuza.jsonq

import com.google.gson.*
import com.shuza.jsonq.exceptions.InvalidFilePathException
import com.shuza.jsonq.exceptions.InvalidJsonException
import com.shuza.jsonq.exceptions.InvalidQueryPathException
import com.shuza.jsonq.ext.get
import com.shuza.jsonq.models.QueryModel
import com.shuza.jsonq.models.QueryOperators
import java.io.FileNotFoundException
import java.io.InputStream

class JsonQ {
    //  raw data to query
    private var jsonObj = JsonObject()

    //  store all the conditions
    private var conditions = arrayListOf<QueryModel>()

    //  select * from
    private var queryArray = JsonArray()

    private var previousCondition = QueryOperators.OPERATOR_OR

    constructor(inputStream: InputStream) {
        try {
            val inputString = inputStream.bufferedReader().use { it.readText() }

            val parser = JsonParser()
            this.jsonObj = parser.parse(inputString) as JsonObject
        } catch (e: FileNotFoundException) {
            throw InvalidFilePathException(e.message!!)
        } catch (e: JsonParseException) {
            throw InvalidJsonException(e.message!!)
        }
    }

    constructor(jsonString: String) {
        this.jsonObj = JsonParser().parse(jsonString) as JsonObject
    }

    constructor(jsonObj: JsonObject) {
        this.jsonObj = jsonObj
    }

    constructor(jsonByteArray: ByteArray) {
        this.jsonObj = JsonParser().parse(jsonByteArray.toString()) as JsonObject
    }

    /**
     *  From builds a SELECT FROM clause
     *  @param  objectKey    key of the node e.g: "users"
     */
    fun from(objectKey: String): JsonQ {
        val result = find(objectKey)

        when (result) {
            is JsonArray -> queryArray = result
            is JsonObject -> {
                queryArray = JsonArray()
                queryArray.add(result)
            }
            else -> queryArray = JsonArray()
        }
        return this
    }

    /**
     *  find nested object
     */
    private fun find(path: String): Any {
        val pathList = path.split(".")
        var result: JsonElement = jsonObj.get(pathList[0])
        for (i in 0 until pathList.size) {
            try {
                if (i == pathList.size - 1) {
                    result = jsonObj.get(pathList[i])
                } else {
                    jsonObj = jsonObj.get(pathList[i]).asJsonObject
                }
            } catch (e: Exception) {
                throw InvalidQueryPathException(path)
            }
        }

        return when {
            result.isJsonObject -> result.asJsonObject
            result.isJsonArray -> result.asJsonArray
            else -> result.asString
        }
    }

    /**
     *  make query  SELECT * WHERE key = value
     *  @param  key     column
     *  @param  value   value of the column
     */
    fun whereEq(key: String, value: Any): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.EQUAL_TO, value, previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  SELECT * WHERE key != value
     *  @param  key     column
     *  @param  value   value of the column
     */
    fun whereNotEq(key: String, value: Any): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.NOT_EQUAL_TO, value, previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  SELECT * WHERE key = NULL
     *  @param  key     column
     */
    fun whereNull(key: String): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.EQUAL_TO_NULL, "", previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  SELECT * WHERE key != NULL
     *  @param  key     column
     */
    fun whereNotNull(key: String): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.EQUAL_TO_NOT_NULL, "", previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  SELECT * WHERE key < value
     *  @param  key     column name
     *  @param  value   column value
     */
    fun whereLess(key: String, value: Any): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.LESS_THAN, value, previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  SELECT * WHERE key <= value
     *  @param  key     column name
     *  @param  value   column value
     */
    fun whereLessEq(key: String, value: Any): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.LESS_THAN_EQUAL, value, previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  SELECT * WHERE key > value
     *  @param  key     column name
     *  @param  value   column value
     */
    fun whereGreater(key: String, value: Any): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.GREATER_THAN, value, previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  SELECT * WHERE key >= value
     *  @param  key     column name
     *  @param  value   column value
     */
    fun whereGreaterEq(key: String, value: Any): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.GREATER_THAN_EQUAL, value, previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  WHERE key LIKE 'value%'
     *  @param  key     column name
     *  @param  value   starting sequence
     */
    fun whereStartsWith(key: String, value: String): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.START_WITH, value, previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  WHERE key LIKE '%value'
     *  @param  key     column name
     *  @param  value   ending sequence
     */
    fun whereEndsWith(key: String, value: String): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.END_WITH, value, previousCondition)
        conditions.add(queryModel)

        return this
    }

    /**
     *  make query  WHERE key LIKE '%value%'
     *  @param  key     column name
     *  @param  value   sequence
     */
    fun whereContains(key: String, value: String): JsonQ {
        val queryModel = QueryModel(key, QueryOperators.CONTAINS, value, previousCondition)
        conditions.add(queryModel)

        return this
    }

    fun and(): JsonQ {
        previousCondition = QueryOperators.OPERATOR_AND

        return this
    }

    fun or(): JsonQ {
        previousCondition = QueryOperators.OPERATOR_OR

        return this
    }

    fun build(): JsonArray {
        return queryArray.get(conditions)
    }
}