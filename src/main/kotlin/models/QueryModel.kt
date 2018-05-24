package models

class QueryModel {

    constructor(key: String, condition: Int, value: Any, previousCondition: Int) {
        this.key = key
        this.condition = condition
        this.value = value
        this.previousCondition = previousCondition
    }

    var key = ""
    var condition = -1
    var value: Any = ""
    var previousCondition = QueryOperators.OPERATOR_OR
}