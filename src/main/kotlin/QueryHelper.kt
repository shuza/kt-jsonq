import com.google.gson.*
import exceptions.InvalidFilePathException
import exceptions.InvalidJsonException
import exceptions.InvalidQueryOperator
import exceptions.InvalidQueryPathException
import models.QueryModel
import models.QueryOperators
import ext.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import java.io.FileNotFoundException
import java.io.InputStream

internal open class QueryHelper : QueryApi {
    //  store the raw json object
    internal var rawJsonData: JsonObject = JsonObject()

    //  store the json object in which we want to query
    internal var queryArrayObject: JsonArray = JsonArray()

    //  store all the conditions
    private var conditions = arrayListOf<QueryModel>()

    /**
     *  load json object from file
     *  @param  filePath     path of the file
     */
    constructor(inputStream: InputStream) {
        try {
            val inputString = inputStream.bufferedReader().use { it.readText() }

            val parser = JsonParser()
            this.rawJsonData = parser.parse(inputString) as JsonObject
        } catch (e: FileNotFoundException) {
            throw InvalidFilePathException(e.message!!)
        } catch (e: JsonParseException) {
            throw InvalidJsonException(e.message!!)
        }
    }

    constructor(jsonObj: JsonObject) {
        this.rawJsonData = jsonObj
    }

    constructor(jsonByteArray: ByteArray) {
        this.rawJsonData = JsonParser().parse(jsonByteArray.toString()) as JsonObject
    }

    constructor(jsonString: String) {
        this.rawJsonData = JsonParser().parse(jsonString) as JsonObject
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

        if (conditions.isEmpty() && queryArrayObject != null) {
            return queryArrayObject!!
        }

        val result = JsonArray()
        queryArrayObject.forEach {
            if (processQuery(it.asJsonObject)) {
                result.add(it.asJsonObject)
            }
        }

        queryArrayObject = JsonArray()

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

    /**
     *  returns size of the result
     */
    override fun count(): Int {
        return size()
    }

    /**
     *  returns size of the result
     */
    override fun size(): Int {
        return get().size()
    }

    /**
     *  returns first element of result
     */
    override fun first(): JsonObject {
        return get().get(0).asJsonObject
    }

    /**
     *  returns last element of result
     */
    override fun last(): JsonObject {
        val result = get()
        val position = if (result.size() > 1) result.size() - 1 else 0
        return result.get(position).asJsonObject
    }

    /**
     *  returns n-th element of result
     */
    override fun nth(position: Int): JsonObject {
        return get().get(position).asJsonObject
    }

    /**
     *  check not Empty || not Null || not Empty Array || not Empty Object
     */
    override fun exists(): Boolean {
        return get().size() > 0
    }

    /**
     *  find nested object
     */
    fun find(path: String): Any {
        val pathList = path.split(".")
        var jsonObj = rawJsonData
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

    override fun rxGet(): Single<JsonArray> {
        return Single.fromCallable { get() }
    }

    override fun rxSum(key: String): Single<Double> {
        return Single.fromCallable { sum(key) }
    }

    override fun rxMax(key: String): Single<Double> {
        return Single.fromCallable { max(key) }
    }

    override fun rxMin(key: String): Single<Double> {
        return Single.fromCallable { min(key) }
    }

    override fun rxAvg(key: String): Single<Double> {
        return Single.fromCallable { avg(key) }
    }
}