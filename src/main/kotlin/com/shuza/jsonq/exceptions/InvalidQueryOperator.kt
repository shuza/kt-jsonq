package com.shuza.jsonq.exceptions

class InvalidQueryOperator(operator: String)
    : Exception("operator $operator is not supported")