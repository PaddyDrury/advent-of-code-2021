package util


typealias StringGrid = List<String>

fun StringGrid.transpose(): StringGrid = this.indices.map { idx -> String(this.map { it[idx] }.toCharArray()) }
fun StringGrid.flipHorizontal(): StringGrid = this.map(String::reversed)
fun StringGrid.rotate(): StringGrid = this.transpose().flipHorizontal()
fun StringGrid.leftSide(): String = this.map { it.first() }.toString()
fun StringGrid.rightSide(): String = this.map { it.last() }.toString()
fun StringGrid.top(): String = this.first()
fun StringGrid.bottom(): String = this.last()
fun StringGrid.matchesRight(rightHandGrid: StringGrid) = this.rightSide() == rightHandGrid.leftSide()
fun StringGrid.matchesBelow(belowGrid: StringGrid) = this.bottom() == belowGrid.top()
fun StringGrid.print() = this.forEach(::println)
fun StringGrid.orientations(): List<StringGrid> = listOf(
    this,
    this.rotate(),
    this.rotate().rotate(),
    this.rotate().rotate().rotate(),
    this.flipHorizontal(),
    this.flipHorizontal().rotate(),
    this.flipHorizontal().rotate().rotate(),
    this.flipHorizontal().rotate().rotate().rotate(),
)
fun StringGrid.sides(): List<String> = listOf(this.leftSide(), this.rightSide(), this.top(), this.bottom())
fun StringGrid.allSidesForAllOrientations(): List<String> = this.orientations().flatMap { it.sides() }
fun List<List<StringGrid>>.merge(): StringGrid = this.map { it.mergeHorizontal() }.mergeVertical()
fun List<StringGrid>.mergeHorizontal(): StringGrid = this.reduce { acc, it -> acc.zip(it).map { it.first + it.second } }
fun List<StringGrid>.mergeVertical() = this.reduce { acc, it -> acc + it }
fun StringGrid.count(char: Char) = this.sumOf { it.count { it == char } }
fun StringGrid.removeBorder(borderThickness: Int = 1) = this.drop(borderThickness).dropLast(borderThickness).map { it.substring(borderThickness, it.length - borderThickness) }
fun StringGrid.rows() = this
fun StringGrid.columns() = (0 until this.first().length).map { idx ->
    this.map { it[idx] }
}.map {
    String(it.toCharArray())
}

typealias Grid<T> = List<List<T>>

fun <T> Grid<T>.toColumns() = this.first().indices.map { idx ->
    this.map { it[idx] }
}

