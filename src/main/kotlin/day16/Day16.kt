package day16


import day16.LiteralPacket.Companion.isLiteralPacket
import day16.LiteralPacket.Companion.parseLiteralPacket
import day16.OperatorPacket.Companion.parseOperatorPacket
import day16.Packet.Companion.parseAll
import day16.Packet.Companion.parseMany
import day16.Packet.Companion.parsePacket
import util.AocDay
import util.loadInputFromServer

fun main() {
    Day16(loadInputFromServer("2021", "16").first()).printTheAnswers()
}

class Day16(input: String) : AocDay {
    private val bitsTransmission = input
        .map {
            it.digitToInt(16)
                .toString(2)
                .padStart(4, '0')
        }.joinToString("").let {
            parsePacket(it)
        }

    override fun part1() = bitsTransmission.sumOfAllVersions()

    override fun part2() = bitsTransmission.value
}

interface Packet {
    val version: Int
    val typeId: Int
    val length: Int
    val value: Long

    companion object {
        fun parsePacket(packetBinary: String) =
            if (isLiteralPacket(packetBinary)) parseLiteralPacket(packetBinary) else parseOperatorPacket(packetBinary)

        private fun parseAndSeekNext(packetBinary: String) =
            parsePacket(packetBinary).let { packet -> packet to packetBinary.drop(packet.length) }

        private fun packetSequence(packetBinary: String): Sequence<Packet> =
            generateSequence(parseAndSeekNext(packetBinary)) { (_, remaining) ->
                if (remaining.isNotBlank()) parseAndSeekNext(remaining) else null
            }.map {
                it.first
            }

        fun parseAll(packetBinary: String) = packetSequence(packetBinary).toList()

        fun parseMany(packetBinary: String, count: Int) = packetSequence(packetBinary).take(count).toList()
    }

    fun sumOfAllVersions(): Int
}

data class LiteralPacket(
    override val version: Int,
    override val length: Int,
    override val value: Long,
) : Packet {
    companion object {
        private const val TYPE_ID = 4
        fun isLiteralPacket(packetBinary: String) = packetBinary.packetType() == TYPE_ID
        fun parseLiteralPacket(packetBinary: String) =
            packetBinary.drop(6).let { value ->
                var lastChunkFound = false
                value.chunked(5).takeWhile { chunk -> !lastChunkFound.also { lastChunkFound = chunk.first() == '0' } }
                    .let { valueChunks ->
                        LiteralPacket(
                            version = packetBinary.packetVersion(),
                            length = 6 + (valueChunks.size * 5),
                            value = valueChunks.joinToString("") { it.drop(1) }.toLong(2)
                        )
                    }
            }
    }

    override val typeId: Int
        get() = TYPE_ID

    override fun sumOfAllVersions() = version
}

data class OperatorPacket(
    override val version: Int,
    override val typeId: Int,
    val subPackets: List<Packet>,
    val headerLength: Int,
) : Packet {
    override val length: Int
        get() = headerLength + subPackets.sumOf { it.length }

    override val value: Long
        get() = when (typeId) {
            0 -> subPackets.sumOf { it.value }
            1 -> subPackets.map { it.value }.reduce { a, b -> a * b }
            2 -> subPackets.minOf { it.value }
            3 -> subPackets.maxOf { it.value }
            5 -> subPackets.map { it.value }.let { if (it.first() > it.last()) 1 else 0 }
            6 -> subPackets.map { it.value }.let { if (it.first() < it.last()) 1 else 0 }
            7 -> subPackets.map { it.value }.let { if (it.first() == it.last()) 1 else 0 }
            else -> error("Invalid type ID $typeId")
        }

    override fun sumOfAllVersions(): Int = version + subPackets.sumOf { it.sumOfAllVersions() }

    companion object {
        fun parseOperatorPacket(packetBinary: String) = packetBinary.drop(6).take(1).let { lengthType ->
            when (lengthType) {
                "0" -> OperatorPacket(
                    version = packetBinary.packetVersion(),
                    typeId = packetBinary.packetType(),
                    headerLength = 6 + 1 + 15,
                    subPackets = parseAll(packetBinary.drop(6 + 1 + 15).take(packetBinary.subPacketsLength()))
                )
                else -> OperatorPacket(
                    version = packetBinary.packetVersion(),
                    typeId = packetBinary.packetType(),
                    headerLength = 6 + 1 + 11,
                    subPackets = parseMany(packetBinary.drop(6 + 1 + 11), packetBinary.subPacketsCount())
                )
            }
        }
    }
}

fun String.packetVersion() = take(3).toInt(2)
fun String.packetType() = drop(3).take(3).toInt(2)
fun String.subPacketsLength() = drop(7).take(15).toInt(2)
fun String.subPacketsCount() = drop(7).take(11).toInt(2)
