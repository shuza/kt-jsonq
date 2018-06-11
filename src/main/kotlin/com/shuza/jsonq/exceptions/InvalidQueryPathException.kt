package com.shuza.jsonq.exceptions

class InvalidQueryPathException(path: String)
    : Exception("$path is not valid")