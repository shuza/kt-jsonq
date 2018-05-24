import models.QueryOperators
import kotlin.properties.Delegates

class JsonQ(filePath: String) {
    private var queryHelper: QueryHelper by Delegates.notNull()

    init {
        queryHelper = QueryHelper(filePath)
    }

    /**
     *  From builds a select clause
     *  @param  objectKey    key of the node e.g: "users"
     */
    fun from(objectKey: String): JsonQ {
        queryHelper.queryObject = queryHelper.rawJsonData.get(objectKey).asJsonArray
        return this
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
}