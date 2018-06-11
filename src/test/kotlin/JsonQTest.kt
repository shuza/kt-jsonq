import com.shuza.jsonq.JsonQ
import com.shuza.jsonq.ext.*
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.junit.Assert
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
    fun testSum() {
        val correctValue = 323000
        val sum = jsonq.from("products")
                .whereEq("user_id", 2)
                .build()
                .sum("price")
        Assert.assertEquals(correctValue, sum.toInt())
    }

    @Test
    fun testAvg() {
        val correctValue = 80750
        val avg = jsonq.from("products")
                .whereEq("user_id", 2)
                .build()
                .avg("price")
        Assert.assertEquals(correctValue, avg.toInt())
    }

    @Test
    fun testMax() {
        val correctValue = 150000
        val max = jsonq.from("products")
                .whereEq("user_id", 2)
                .build()
                .max("price")
        Assert.assertEquals(correctValue, max.toInt())
    }

    @Test
    fun testMin() {
        val correctValue = 150
        val min = jsonq.from("products")
                .build()
                .min("price")
        Assert.assertEquals(correctValue, min.toInt())
    }

    @Test
    fun testCount() {
        val correctValue = 6
        val size = jsonq.from("users")
                .build()
                .size()
        Assert.assertEquals(correctValue, size)
    }

    @Test
    fun testObject() {
        val correctValue = "Computer Source BD"
        val result = jsonq.from("vendor")
                .whereEq("name", correctValue)
                .build()
        Assert.assertEquals(result.size(), 1)
    }

    @Test
    fun testAnd() {
        val correctValue = 150000
        val result = jsonq.from("products")
                .whereEq("user_id", 2)
                .and()
                .whereEq("name", "macbook pro")
                .build()
                .sum("price")
        Assert.assertEquals(correctValue, result.toInt())
    }

    @Test
    fun testOr() {
        val correctValue = 433150
        val result = jsonq.from("products")
                .whereEq("user_id", 2)
                .or()
                .whereEq("user_id", 1)
                .build()
                .sum("price")
        Assert.assertEquals(correctValue, result.toInt())
    }

    @Test
    fun testRxOr() {
        val correctValue = 433150
        jsonq.from("products")
                .whereEq("user_id", 2)
                .or()
                .whereEq("user_id", 1)
                .build()
                .rxSum("price")
                .subscribe(object : SingleObserver<Double> {
                    override fun onSuccess(t: Double) {
                        Assert.assertEquals(correctValue, t.toInt())
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Assert.fail(e.message)
                    }
                })
    }
}