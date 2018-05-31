package exceptions

class InvalidQueryPathException(path: String)
    : Exception("$path is not valid")