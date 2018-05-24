import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import exceptions.InvalidFilePathException
import exceptions.InvalidJsonException
import exceptions.InvalidQueryOperator
import models.QueryModel
import models.QueryOperators
import utils.*
import java.io.File
import java.io.FileNotFoundException

open class QueryHelper(filePath: String) : QueryApi {
    //  store the raw json object
    internal var rawJsonData: JsonObject = JsonObject()

    //  store the json object in which we want to query
    internal var queryObject: JsonArray = JsonArray()

    //  store all the conditions
    private var conditions = arrayListOf<QueryModel>()

    init {
        loadJsonFile(filePath)
    }

    /**
     *  load json object from file
     *  @param  filePath     path of the file
     */
    private fun loadJsonFile(filePath: String) {
        try {
            val inputStream = File(filePath).inputStream()
            val inputString = inputStream.bufferedReader().use { it.readText() }

            val parser = JsonParser()
            rawJsonData = parser.parse(inputString) as JsonObject
        } catch (e: FileNotFoundException) {
            throw InvalidFilePathException(e.message!!)
        } catch (e: JsonParseException) {
            throw InvalidJsonException(e.message!!)
        }
    }

    /**
     *  makeWhere builds a where clause
     *  e.g:    Where("name", "=", "Jack")
     */
    internal fun makeWhere(key: String, condition: Int, value: Any, previousCondition: Int) {
        val queryModel = QueryModel(key, condition, value, previousCondition)
        conditions.add(queryModel)
    }

    /**
     *  processQuery will take JsonObject and test with all the query conditions
     *  @param  obj         it will testes
     *  @return Boolean     true or false
     */
    private fun processQuery(obj: JsonObject): Boolean {
        var result = false
        for (i in 0 until conditions.size) {
            if (conditions[i].condition != QueryOperators.OPERATOR_AND
                    && conditions[i].condition != QueryOperators.OPERATOR_OR) {
                val previousCondition = if (i > 0) conditions[i - 1].condition else null

                result = if (obj.has(conditions[i].key))
                    testCondition(obj, conditions[i], result) else
                    false
            }
        }

        return result
    }

    /**
     *  testCondition tests an individual condition of the query sequence
     *  @param  obj                 is the json makeWhere condition will be tested
     *  @param  query               individual condition that will be tested
     *  @param  previousResult      result of the previous tested condition if any otherwise true
     *  @param  previousCondition   previous condition if necessary for this condition e.g: AND, OR
     *
     *  @return     true or false
     */
    private fun testCondition(obj: JsonObject, query: QueryModel,
                              previousResult: Boolean = true): Boolean {

        var result = when (query.condition) {
            QueryOperators.EQUAL_TO -> obj.isEqual(query.key, query.value)
            QueryOperators.NOT_EQUAL_TO -> obj.isNotEqual(query.key, query.value)

            QueryOperators.EQUAL_TO_NULL -> obj.get(query.key).isJsonNull
            QueryOperators.EQUAL_TO_NOT_NULL -> !obj.get(query.key).isJsonNull

            QueryOperators.LESS_THAN -> obj.isLess(query.key, query.value)
            QueryOperators.LESS_THAN_EQUAL -> obj.isLessEq(query.key, query.value)

            QueryOperators.GREATER_THAN -> obj.isGreater(query.key, query.value)
            QueryOperators.GREATER_THAN_EQUAL -> obj.isGreaterEq(query.key, query.value)

            QueryOperators.START_WITH -> obj.startsWith(query.key, query.value.toString())
            QueryOperators.END_WITH -> obj.endsWith(query.key, query.value.toString())
            QueryOperators.CONTAINS -> obj.isExists(query.key, query.value.toString())

            else -> false
        }

        query.previousCondition?.let {
            result = when (it) {
                QueryOperators.OPERATOR_AND -> result && previousResult
                QueryOperators.OPERATOR_OR -> result || previousResult
                else -> throw InvalidQueryOperator(it.toString())
            }
        }

        return result
    }

    /**
     *
     */
    override fun orWhereEq(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.EQUAL_TO, value, QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereNotEq(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.NOT_EQUAL_TO, value, QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereNull(key: String): QueryApi {
        makeWhere(key, QueryOperators.EQUAL_TO_NULL, "", QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereNotNull(key: String): QueryApi {
        makeWhere(key, QueryOperators.EQUAL_TO_NOT_NULL, "", QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereLess(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.LESS_THAN, value, QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereLessEq(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.LESS_THAN_EQUAL, value, QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereGreater(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.GREATER_THAN, value, QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereGreaterEq(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.GREATER_THAN_EQUAL, value, QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereStartsWith(key: String, value: String): QueryApi {
        makeWhere(key, QueryOperators.START_WITH, value, QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereEndsWith(key: String, value: String): QueryApi {
        makeWhere(key, QueryOperators.END_WITH, value, QueryOperators.OPERATOR_OR)
        return this
    }

    override fun orWhereContains(key: String, value: String): QueryApi {
        makeWhere(key, QueryOperators.CONTAINS, value, QueryOperators.OPERATOR_OR)
        return this
    }

    override fun andWhereEq(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.EQUAL_TO, value, QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereNotEq(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.NOT_EQUAL_TO, value, QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereNull(key: String): QueryApi {
        makeWhere(key, QueryOperators.EQUAL_TO_NULL, "", QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereNotNull(key: String): QueryApi {
        makeWhere(key, QueryOperators.EQUAL_TO_NOT_NULL, "", QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereLess(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.LESS_THAN, value, QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereLessEq(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.LESS_THAN_EQUAL, value, QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereGreater(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.GREATER_THAN, value, QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereGreaterEq(key: String, value: Any): QueryApi {
        makeWhere(key, QueryOperators.GREATER_THAN_EQUAL, value, QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereStartsWith(key: String, value: String): QueryApi {
        makeWhere(key, QueryOperators.START_WITH, value, QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereEndsWith(key: String, value: String): QueryApi {
        makeWhere(key, QueryOperators.END_WITH, value, QueryOperators.OPERATOR_AND)
        return this
    }

    override fun andWhereContains(key: String, value: String): QueryApi {
        makeWhere(key, QueryOperators.CONTAINS, value, QueryOperators.OPERATOR_AND)
        return this
    }

    /**
     *  get returns the results as JsonArray
     */
    override fun get(): JsonArray {

        if (conditions.isEmpty() && queryObject != null) {
            return queryObject!!
        }

        val result = JsonArray()
        queryObject.forEach {
            if (processQuery(it.asJsonObject)) {
                result.add(it.asJsonObject)
            }
        }

        queryObject = JsonArray()

        return result
    }

    /**
     *  summation of all values
     */
    override fun sum(key: String): Double {
        var result: Double = 0.0
        get().forEach {
            try {
                result += it.asJsonObject.get(key).asDouble
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     *  find maximum value
     */
    override fun max(key: String): Double {
        var result: Double = Double.MIN_VALUE
        get().forEach {
            try {
                val value = it.asJsonObject.get(key).asDouble
                result = if (value > result) value else result
            } catch (e: Exception) {
            }
        }

        return result
    }

    /**
     *  find minimum value
     */
    override fun min(key: String): Double {
        var result: Double = Double.MAX_VALUE
        get().forEach {
            try {
                val value = it.asJsonObject.get(key).asDouble
                result = if (value < result) value else result
            } catch (e: Exception) {
            }
        }

        return result
    }

    /**
     *  find average value
     */
    override fun avg(key: String): Double {
        val data = get()
        var sum: Double = 0.0
        data.forEach {
            try {
                sum += it.asJsonObject.get(key).asDouble
            } catch (e: Exception) {
            }
        }

        return sum / data.size()
    }
}