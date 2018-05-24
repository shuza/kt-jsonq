fun main(arg: Array<String>) {
    val FILE_PATH = "data.json"

    val jsonQ = JsonQ(FILE_PATH)

    val result = jsonQ.from("products")
            .whereNotEq("user_id", 2)
            .andWhereStartsWith("name", "mac")
            .get()
    println(result)

    println("*******    Finish      **********")
}