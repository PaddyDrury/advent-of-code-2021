package util

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

inline fun <reified T> loadResource(resource: String): String = T::class.java.getResource(resource).readText()

inline fun <reified T> loadInputFromResource(resource: String): List<String> = loadResource<T>(resource).lines()

fun loadInputFromServer(year: String, day: String): List<String> =
    HttpClient {
        this.defaultRequest {
            this.cookie("session", Thread.currentThread().contextClassLoader.getResource("session-cookie").readText())
        }
    }.let {
        runBlocking {
            withContext(Dispatchers.Default) {
                it.get<String>("https://adventofcode.com/$year/day/$day/input")
            }
        }
    }.trim().lines()