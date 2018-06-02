import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Single
import models.QueryOperators
import java.io.InputStream
import kotlin.properties.Delegates

class JsonQ {
    private var queryHelper: QueryHelper by Delegates.notNull()

    constructor(inputStream: InputStream) {
        queryHelper = QueryHelper(inputStream)
    }

    constructor(jsonString: String) {
        queryHelper = QueryHelper(jsonString)
    }

    constructor(jsonObj: JsonObject) {
        queryHelper = QueryHelper(jsonObj)
    }

    /**
     *  From builds a SELECT FROM clause
     *  @param  objectKey    key of the node e.g: "users"
     */
    fun from(objectKey: String): JsonQ {
        val result = find(objectKey)
        if (result is JsonArray) {
            queryHelper.queryArrayObject = result
        } else if (result is JsonObject) {
            val tmp = JsonArray()
            tmp.set(0, result)
            queryHelper.queryArrayObject = tmp
        }
        queryHelper.queryArrayObject = queryHelper.rawJsonData.get(objectKey).asJsonArray
        return this
    }

    /**
     *  at builds a select clause like from method
     */
    fun at(objectKey: String): JsonQ {
        return from(objectKey)
    }

    fun find(path: String): Any {
        return queryHelper.find(path)
    }

    /**
     *  make query  SELECT * WHERE key = value
     *  @param  key     column
     *  @param  value   value of the column
     */
    fun whereEq(key: String, value: Any): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.EQUAL_TO, value, QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  SELECT * WHERE key != value
     *  @param  key     column
     *  @param  value   value of the column
     */
    fun whereNotEq(key: String, value: Any): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.NOT_EQUAL_TO, value, QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  SELECT * WHERE key = NULL
     *  @param  key     column
     */
    fun whereNull(key: String): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.EQUAL_TO_NULL, "", QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  SELECT * WHERE key != NULL
     *  @param  key     column
     */
    fun whereNotNull(key: String): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.EQUAL_TO_NOT_NULL, "", QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  SELECT * WHERE key < value
     *  @param  key     column name
     *  @param  value   column value
     */
    fun whereLess(key: String, value: Any): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.LESS_THAN, value, QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  SELECT * WHERE key <= value
     *  @param  key     column name
     *  @param  value   column value
     */
    fun whereLessEq(key: String, value: Any): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.LESS_THAN_EQUAL, value, QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  SELECT * WHERE key > value
     *  @param  key     column name
     *  @param  value   column value
     */
    fun whereGreater(key: String, value: Any): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.GREATER_THAN, value, QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  SELECT * WHERE key >= value
     *  @param  key     column name
     *  @param  value   column value
     */
    fun whereGreaterEq(key: String, value: Any): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.GREATER_THAN_EQUAL, value, QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  WHERE key LIKE 'value%'
     *  @param  key     column name
     *  @param  value   starting sequence
     */
    fun whereStartsWith(key: String, value: String): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.START_WITH, value, QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  WHERE key LIKE '%value'
     *  @param  key     column name
     *  @param  value   ending sequence
     */
    fun whereEndsWith(key: String, value: String): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.END_WITH, value, QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    /**
     *  make query  WHERE key LIKE '%value%'
     *  @param  key     column name
     *  @param  value   sequence
     */
    fun whereContains(key: String, value: String): QueryApi {
        queryHelper.makeWhere(key, QueryOperators.CONTAINS, value, QueryOperators.OPERATOR_OR)
        return queryHelper
    }

    fun sum(key: String) = queryHelper.sum(key)
    fun max(key: String) = queryHelper.max(key)
    fun min(key: String) = queryHelper.min(key)
    fun avg(key: String) = queryHelper.avg(key)
    fun count() = queryHelper.count()
    fun size() = queryHelper.size()
    fun first() = queryHelper.first()
    fun last() = queryHelper.last()
    fun nth(position: Int) = queryHelper.nth(position)
    fun exists() = queryHelper.exists()

    fun rxGet() = queryHelper.rxGet()
    fun rxSum(key: String) = queryHelper.rxSum(key)
    fun rxMax(key: String) = queryHelper.rxMax(key)
    fun rxMin(key: String) = queryHelper.rxMin(key)
    fun rxAvg(key: String) = queryHelper.rxAvg(key)
}