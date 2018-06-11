package com.shuza.jsonq.models

sealed class QueryMapper

object QueryOperators : QueryMapper() {
    const val EQUAL_TO = 0
    const val NOT_EQUAL_TO = 1
    const val EQUAL_TO_NULL = 2
    const val EQUAL_TO_NOT_NULL = 3

    const val LESS_THAN = 4
    const val LESS_THAN_EQUAL = 5

    const val GREATER_THAN = 6
    const val GREATER_THAN_EQUAL = 7

    const val START_WITH = 8
    const val END_WITH = 9
    const val CONTAINS = 10

    const val OPERATOR_AND = 111
    const val OPERATOR_OR = 222
}