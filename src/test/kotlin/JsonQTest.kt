import org.junit.Before
import org.junit.Test

class JsonQTest {
    private lateinit var jsonq: JsonQ

    @Before
    fun setUp() {
        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("data.json")
        jsonq = JsonQ(inputStream)
    }

    @Test
    fun rxFindTest(){
        jsonq.from("products")
                .whereEq("user_id", 2)
                .rxGet()
    }
}