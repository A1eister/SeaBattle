//package org.example
//
//fun main() {
//
//    val field = Array(10) { Array(10) { 0 } }
//    val range = 0..9
//    val index1 = range.random()
//    val index2 = range.random()
//    val orientationRange = 0..1
//    val directionRange = 0..1
//    val orientation = orientationRange.random()
//    val direction = directionRange.random()
//    val oneDeckShip = 0
//    val twoDeckShip = 1
//    val threeDeckShip = 2
//    val fourDeckShip = 3
//
//    var isActive = true
//    while (isActive) {
//        if (orientation == 0) {
//            if (direction == 0) {
//                if (index1 - 3 < 0) {
//                    continue
//                }
//                for (i in 0..fourDeckShip) {
//                    field[index1 - i][index2] = 1
//                }
//                isActive=false
//            } else {
//                if (index1 + 3 > 9) {
//                    continue
//                }
//                for (i in 0..fourDeckShip) {
//                    field[index1 + i][index2] = 1
//                }
//                isActive=false
//            }
//        } else {
//            if (direction == 0) {
//                if (index2 - 3 < 0) {
//                    continue
//                }
//                for (i in 0..fourDeckShip) {
//                    field[index1][index2 - i] = 1
//                }
//                isActive=false
//            } else {
//                if (index2 + 3 > 9) {
//                    continue
//                }
//                for (i in 0..fourDeckShip) {
//                    field[index1][index2 + i] = 1
//                }
//                isActive=false
//            }
//        }}
//
//        for (row in field) {
//            for (cell in row) {
//                print("$cell \t")
//            }
//            println()
//        }
//        println()
//
//
//
//}
//
import kotlin.random.Random

fun main() {
    val field = Array(10) { Array(10) { 0 } }
    val range = 0..9
    val orientationRange = 0..1
    val directionRange = 0..1

    val ships = listOf(
        4 to 1, // размер 4 — 1 штука
        3 to 2, // размер 3 — 2 штуки
        2 to 3, // размер 2 — 3 штуки
        1 to 4  // размер 1 — 4 штуки
    )

    fun isAreaFree(field: Array<Array<Int>>, x: Int, y: Int): Boolean {
        for (dx in -1..1) {
            for (dy in -1..1) {
                val nx = x + dx
                val ny = y + dy
                if (nx in 0..9 && ny in 0..9) {
                    if (field[nx][ny] != 0) return false
                }
            }
        }
        return true
    }

    fun markAroundShip(field: Array<Array<Int>>, positions: List<Pair<Int, Int>>) {
        for ((x, y) in positions) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    val nx = x + dx
                    val ny = y + dy
                    if (nx in 0..9 && ny in 0..9 && field[nx][ny] == 0) {
                        field[nx][ny] = 2
                    }
                }
            }
        }
    }
//нет цикла вайл!!!
    for ((size, count) in ships) {
        repeat(count) {
            var isActive = true
            while (isActive) {
                val index1 = range.random()
                val index2 = range.random()
                val orientation = orientationRange.random()
                val direction = directionRange.random()

                var canPlace = true
                val positions = mutableListOf<Pair<Int, Int>>()

                if (orientation == 0) { // вертикально
                    if (direction == 0) { // вверх
                        if (index1 - (size - 1) < 0) continue

                        for (i in 0 until size) {
                            val x = index1 - i
                            val y = index2
                            if (!isAreaFree(field, x, y)) {
                                canPlace = false
                                isActive=false
                            }
                            positions.add(x to y)
                        }

                    } else { // вниз
                        if (index1 + (size - 1) > 9) continue

                        for (i in 0 until size) {
                            val x = index1 + i
                            val y = index2
                            if (!isAreaFree(field, x, y)) {
                                canPlace = false
                                isActive=false
                            }
                            positions.add(x to y)
                        }
                    }
                } else { // горизонтально
                    if (direction == 0) { // влево
                        if (index2 - (size - 1) < 0) continue

                        for (i in 0 until size) {
                            val x = index1
                            val y = index2 - i
                            if (!isAreaFree(field, x, y)) {
                                canPlace = false
                                isActive=false
                            }
                            positions.add(x to y)
                        }

                    } else { // вправо
                        if (index2 + (size - 1) > 9) continue

                        for (i in 0 until size) {
                            val x = index1
                            val y = index2 + i
                            if (!isAreaFree(field, x, y)) {
                                canPlace = false
                                isActive=false
                            }
                            positions.add(x to y)
                        }
                    }
                }

                if (canPlace) {
                    // Ставим сам корабль
                    for ((x, y) in positions) {
                        field[x][y] = 1
                    }
                    // Ставим двойки вокруг корабля
                    markAroundShip(field, positions)

                    isActive = false
                }
            }
        }
    }

    // Печать поля
    for (row in field) {
        for (cell in row) {
            print("$cell\t")
        }
        println()
    }
    println()
}
fun playerMove(field: Array<Array<Int>>) {
    val letters = "ABCDEFGHIJ"

    while (true) {
        print("Введите координаты (например, A1): ")
        val input = readlnOrNull()?.trim()?.uppercase() ?: continue

        if (input.length < 2 || input.length > 3) {
            println("Неверный формат. Попробуйте снова.")
            continue
        }

        val letter = input[0]
        val numberPart = input.substring(1)

        val x = letters.indexOf(letter)
        val y = numberPart.toIntOrNull()?.minus(1) ?: -1

        if (x !in 0..9 || y !in 0..9) {
            println("Координаты вне поля. Попробуйте снова.")
            continue
        }

        when (field[x][y]) {
            1 -> {
                println("Попадание! 🔥")
                field[x][y] = 3 // 3 = подбитая часть корабля
                break
            }
            0, 2 -> {
                println("Мимо. 🌊")
                field[x][y] = 4 // 4 = промах
                break
            }
            3, 4 -> {
                println("Вы уже стреляли сюда. Попробуйте другую клетку.")
            }
            else -> {
                println("Ошибка. Попробуйте снова.")
            }
        }
    }
}
