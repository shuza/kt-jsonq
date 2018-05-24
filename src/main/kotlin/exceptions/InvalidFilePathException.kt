package exceptions

class InvalidFilePathException(var errorMessage: String) : Exception(errorMessage)