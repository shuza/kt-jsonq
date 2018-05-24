package exceptions

class InvalidJsonException(var errorMessage: String) : Exception(errorMessage)