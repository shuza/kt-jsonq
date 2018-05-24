package utils

import com.google.gson.JsonObject

/**
 *  check == condition with same date type
 *  if exception arise condition will be failed
 */
fun JsonObject.isEqual(key: String, value: Any): Boolean {
    return try {
        when (value) {
            is Int -> get(key).asInt == value
            is Double -> get(key).asDouble == value
            is Float -> get(key).asFloat == value
            is String -> get(key).asString.contentEquals(value)

            else -> false
        }
    } catch (e: Exception) {
        false
    }
}

/**
 *  check != condition with same date type
 *  if exception arise condition will be failed
 */
fun JsonObject.isNotEqual(key: String, value: Any): Boolean {
    return try {
        when (value) {
            is Int -> get(key).asInt != value
            is Double -> get(key).asDouble != value
            is Float -> get(key).asFloat != value
            is String -> get(key).asString.contentEquals(value)

            else -> false
        }
    } catch (e: Exception) {
        false
    }
}

/**
 *  check < condition with same date type
 *  for string it will check the length
 *  if exception arise condition will be failed
 */
fun JsonObject.isLess(key: String, value: Any): Boolean {
    return try {
        when (value) {
            is Int -> get(key).asInt < value
            is Double -> get(key).asDouble < value
            is Float -> get(key).asFloat < value
            is String -> get(key).asString.length < value.length

            else -> false
        }
    } catch (e: Exception) {
        false
    }
}


/**
 *  check <= condition with same date type
 *  for string it will check the length
 *  if exception arise condition will be failed
 */
fun JsonObject.isLessEq(key: String, value: Any): Boolean {
    return try {
        when (value) {
            is Int -> get(key).asInt <= value
            is Double -> get(key).asDouble <= value
            is Float -> get(key).asFloat <= value
            is String -> get(key).asString.length <= value.length

            else -> false
        }
    } catch (e: Exception) {
        false
    }
}


/**
 *  check > condition with same date type
 *  for string it will check the length
 *  if exception arise condition will be failed
 */
fun JsonObject.isGreater(key: String, value: Any): Boolean {
    return try {
        when (value) {
            is Int -> get(key).asInt > value
            is Double -> get(key).asDouble > value
            is Float -> get(key).asFloat > value
            is String -> get(key).asString.length > value.length

            else -> false
        }
    } catch (e: Exception) {
        false
    }
}

/**
 *  check >= condition with same date type
 *  for string it will check the length
 *  if exception arise condition will be failed
 */
fun JsonObject.isGreaterEq(key: String, value: Any): Boolean {
    return try {
        when (value) {
            is Int -> get(key).asInt >= value
            is Double -> get(key).asDouble >= value
            is Float -> get(key).asFloat >= value
            is String -> get(key).asString.length >= value.length

            else -> false
        }
    } catch (e: Exception) {
        false
    }
}

/**
 *  check if column value starts with the sequence
 *  if exception arise condition will be failed
 *  @param  key     column name
 *  @param  value   starting sequence
 */
fun JsonObject.startsWith(key: String, value: String): Boolean {
    return try {
        get(key).asString.startsWith(value)
    } catch (e: Exception) {
        false
    }
}

/**
 *  check if column value ends with the sequence
 *  if exception arise condition will be failed
 *  @param  key     column name
 *  @param  value   ending sequence
 */
fun JsonObject.endsWith(key: String, value: String): Boolean {
    return try {
        get(key).asString.endsWith(value)
    } catch (e: Exception) {
        false
    }
}

/**
 *  check if sequence exists
 *  if exception arise condition will be failed
 *  @param  key     column name
 *  @param  value   sequence
 */
fun JsonObject.isExists(key: String, value: String): Boolean {
    return try {
        get(key).asString.endsWith(value)
    } catch (e: Exception) {
        false
    }
}