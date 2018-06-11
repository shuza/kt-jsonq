package com.shuza.jsonq

import com.google.gson.JsonObject
import com.shuza.jsonq.exceptions.InvalidQueryOperator
import com.shuza.jsonq.ext.*
import com.shuza.jsonq.models.QueryModel
import com.shuza.jsonq.models.QueryOperators

class QueryHelper{
    /**
     *  processQuery will take JsonObject and test with all the query conditions
     *  @param  obj         it will testes
     *  @return Boolean     true or false
     */
    internal fun processQuery(obj: JsonObject, conditions: ArrayList<QueryModel>): Boolean {
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
}