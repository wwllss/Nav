package zy.nav.util

import kotlin.random.Random

/**
 * created by zhangyuan on 2020/5/28
 */
class Person(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val age: Int = Random.nextInt(100)
) {
    companion object {

        fun testData() = Person(1, 1)

        fun testDataMap() = HashMap<String, List<Person>>().apply {
            put("1", listOf(Person(1, 1)))
            put("2", listOf(Person(2, 1), Person(2, 2)))
            put("3", listOf(Person(3, 1), Person(3, 2), Person(3, 3)))
        }

    }
}