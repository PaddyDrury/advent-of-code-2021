package util

fun <T> Iterable<T>.chunkWhen(condition: (T) -> Boolean): List<List<T>> = this.fold(mutableListOf(mutableListOf<T>())) { acc, t ->
    if (condition.invoke(t)) {
        acc.add(mutableListOf())
    } else {
        acc.last().add(t)
    }
    acc
}.filter {
    it.isNotEmpty()
}