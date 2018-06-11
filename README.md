Kt-JsonQ
===============
[![](https://jitpack.io/v/ninja.sakib/kotlin-jsonq.svg)](https://jitpack.io/#shuza/kt-jsonq/v0.2)

**kt-jsonq** is a simple, elegant kotlin library to Query over any type of JSON Data. It'll make your life easier by giving the flavour of an ORM-like query on your JSON.

This package is inspired from the awesome [jsonq](https://github.com/nahid/jsonq) package.



Gradle,
```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
And
```gradle
dependencies {
    implementation 'com.github.shuza:kt-jsonq:v1.0'
}
```

# Usage
You can start using this package right away by importing your Json data from a file:
```kotlin
val filePath = "data.json"
val inputStream = FileInputStream(filePath)
val jsonq = JsonQ(inputStream)
```
You can start Query your data using the various query methods such as **whereEq**, **whereNull**, **whereLess**, **whereStartsWith**, **orWhereNotNull**, **orWhereGreater**, **andWhereGreaterEq**, **andWhereEndsWith** and so on. Also you can aggregate your data after query using **sum**, **max**, **min** etc.

Let's see a quick example:

```kotlin
//  data.json
{
	"name": "products",
	"description": "Features product list",
	"vendor":{
		"name": "Computer Source BD",
		"email": "info@example.com",
		"website":"www.example.com"
	},
	"users":[
		{"id":1, "name":"Johura Akter Sumi", "location": "Barisal"},
		{"id":2, "name":"Mehedi Hasan Nahid", "location": "Barisal"},
		{"id":3, "name":"Ariful Islam", "location": "Barisal"},
		{"id":4, "name":"Suhel Ahmed", "location": "Sylhet"},
		{"id":5, "name":"Firoz Serniabat", "location": "Gournodi"},
		{"id":6, "name":"Musa Jewel", "location": "Barisal", "visits": [
			{"name": "Sylhet", "year": 2011},
			{"name": "Cox's Bazar", "year": 2012},
			{"name": "Bandarbar", "year": 2014}
		]}
	],
	"products": [
		{"id":1, "user_id": 2, "city": "bsl", "name":"iPhone", "cat":1, "price": 80000},
		{"id":2, "user_id": 2, "city": null, "name":"macbook pro", "cat": 2, "price": 150000},
		{"id":3, "user_id": 2, "city": "dhk", "name":"Redmi 3S Prime", "cat": 1, "price": 12000},
		{"id":4, "user_id": 1, "city": null, "name":"Redmi 4X", "cat":1, "price": 15000},
		{"id":5, "user_id": 1, "city": "bsl", "name":"macbook air", "cat": 2, "price": 110000},
		{"id":6, "user_id": 2, "city": null, "name":"macbook air 1", "cat": 2, "price": 81000}
	]
}
```
Now let's do some query in this json data
```kotlin
val result = jsonQ.from("products")
            .whereEq("cat", 2)
            .build()
println(result)

/**
******** This will print
**/
[{
	"id": 2,
	"user_id": 2,
	"city": null,
	"name": "macbook pro",
	"cat": 2,
	"price": 150000
}, {
	"id": 5,
	"user_id": 1,
	"city": "bsl",
	"name": "macbook air",
	"cat": 2,
	"price": 110000
}, {
	"id": 6,
	"user_id": 2,
	"city": null,
	"name": "macbook air 1",
	"cat": 2,
	"price": 81000
}]
```
Let's say we want to get the Summation of price of the Queried result. We can do it easily by calling the **sum()** method instead of **get()**
```kotlin
val result = jsonQ.from("products")
            .whereEq("cat", 2)
	    .build()
            .sum("price")
println(result)

/**
**********  It will print
*/
341000
```
It has reactive programming support. Let's see some example
```kotlin
jsonq.from("users")
            .whereLess("id", 3)
	    .build()
            .rxSum("price")
            .subscribe(object : SingleObserver<Double> {
                override fun onSuccess(t: Double) {
                    println(t)
                }

                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    println(e.message)
                }
            })

/****   It will print   ******/
433150.0
```
cool, huh?

Let's explore the full API to see what else magic this library can do for you. Shall we?

# API
Following API examples are shown based on the sample JSON data given [here](https://github.com/shuza/kt-jsonq/blob/master/data.json). To get a better idea of the examples see that JSON data first. Also detailed examples of each API can be found [upcoming]().

**List of API:**
* [whereEq]()
* [whereNotEq]()
* [whereNull]()
* [whereNotNull]()
* [whereLess]()
* [whereLessEq]()
* [whereGreater]()
* [whereGreaterEq]()
* [whereStartsWith]()
* [whereEndsWith]()
* [whereContains]()
* [or]()
* [and]()
* [rx]()
* [get]()
* [sum]()
* [max]()
* [min]()
* [avg]()
* [count]()

## Buds and Issues
If you encounter any bugs or issues, feel free to [open an issue at
github](https://github.com/shuza/kt-jsonq/issues).

Also, you can shoot me an email to
<mailto:shuza.sa@gmail.com> for hugs or bugs.

## Credit

Speical thanks to [Nahid Bin Azhar](https://github.com/nahid) for the inspiration and guidance for the package.

## Contributions

If your PR is successfully merged to this project, feel free to add yourself in the list of contributors.
See all the [contributors](CONTRIBUTORS.md).
