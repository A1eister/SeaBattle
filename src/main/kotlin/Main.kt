fun main() {
    val myField = Array(10) { Array(10) { 0 } }
    val pcField = Array(10) { Array(10) { 0 } }
    val range = 0..9
    val orientationRange = 0..1
    val directionRange = 0..1

    val ships = listOf(
        4 to 1, // размер 4 — 1 штука
        3 to 2, // размер 3 — 2 штуки
        2 to 3, // размер 2 — 3 штуки
        1 to 4  // размер 1 — 4 штуки
    )

    createField(myField, ships, range, orientationRange, directionRange)
    createField(pcField, ships, range, orientationRange, directionRange)

    var gameOver = false

    while (!gameOver) {
        println("Ваше поле:")
        printField(myField)

        println("\nПоле компьютера:")
        printPcField(pcField)

        println("\nВаш ход:")
        playerMove(pcField)

        if (checkWin(pcField)) {
            println("Вы победили!")
            gameOver = true
        }

        println("\nХод компьютера:")
        aiMove(myField)

        if (checkWin(myField)) {
            println("💥 Компьютер победил!")
            gameOver = true
        }
    }
}

fun isAreaFree(field: Array<Array<Int>>, x: Int, y: Int): Boolean {
    for (dx in -1..1) {
        for (dy in -1..1) {
            val nx = x + dx
            val ny = y + dy
            if (nx in 0..9 && ny in 0..9) {
                if (field[nx][ny] == 1) return false
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

fun playerMove(field: Array<Array<Int>>) {
    val letters = "АБВГДЕЖЗИК"
    while (true) {
        print("Введите координаты (например, A1): ")
        val input = readlnOrNull()?.trim()?.uppercase() ?: continue

        if (!validation(input)){
            continue
        }

        val y = letters.indexOf(input[0])
        val x = input.substring(1).toIntOrNull()?.minus(1) ?: -1

        when (field[x][y]) {
            1 -> {
                println("Попадание!")
                field[x][y] = 3 // 3 = подбитая часть корабля
                break
            }

            0, 2 -> {
                println("Мимо")
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

fun aiMove(field: Array<Array<Int>>) {
    val letters = "АБВГДЕЖЗИК"
    val PcShot = mutableListOf<Pair<Int, Int>>()

    for (i in 0..9) {
        for (j in 0..9) {
            if (field[i][j] == 0 || field[i][j] == 2 || field[i][j] == 1) {
                PcShot.add(i to j)
            }
        }
    }

    if (PcShot.isEmpty()) return

    val (x, y) = PcShot.random()

    when (field[x][y]) {
        1 -> {
            println("Компьютер попал в ${letters[y]}${x + 1}!")
            field[x][y] = 3
        }
        0, 2 -> {
            println("Компьютер промахнулся в ${letters[y]}${x + 1}.")
            field[x][y] = 4
        }
    }
}


fun printField(field: Array<Array<Int>>) {
    println("    А Б В Г Д Е Ж З И К")
    for (i in 0..9) {
        print("${(i + 1).toString().padStart(2)}| ")
        for (j in 0..9) {
            print("${field[i][j]} ")
        }
        println()
    }
}

fun printPcField(field: Array<Array<Int>>) {
    println("    А Б В Г Д Е Ж З И К")
    for (i in 0..9) {
        print("${i.toString().padStart(2)}| ")
        for (j in 0..9) {
            val cell = when (field[i][j]) {
                3 -> 3 // попадание
                4 -> 4 // промах
                else -> 0 // остальное скрыто
            }
            print("$cell ")
        }
        println()
    }
}
fun createField(
    field: Array<Array<Int>>,
    ships: List<Pair<Int, Int>>,
    range: IntRange,
    orientationRange: IntRange,
    directionRange: IntRange
) {
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
                }
                isActive = !canPlace
            }
        }
    }
}
fun checkWin(field: Array<Array<Int>>): Boolean {
    for (row in field) {
        if (row.contains(1)) return false
    }
    return true
}
fun validation (input: String): Boolean{
    val letters = "АБВГДЕЖЗИК"
    var y = 1
    if (input.length < 2 || input.length > 3) {
    println("Неверный формат. Попробуйте снова.")
        return false
    }
    if (input.length == 3) {
        if (input[1].digitToIntOrNull() !=1 && input[2].digitToIntOrNull() !=0){

        println("Неверно. Попробуйте снова.")
        return false
        } else y = 9
    }else {
        val numberPart = input[1]
        y = numberPart.digitToIntOrNull()?.minus(1) ?: -1
    }


        val letter = input[0]


    val x = letters.indexOf(letter)

if (x !in 0..9 || y !in 0..9) {
    println("Координаты вне поля. Попробуйте снова.")
    return false
}
    return true
}

// функции внутри других функций не инициализируются. Всегда каждая функция объявляется сама по себе, отдельно от других.


// 1! - сделать верную обработку А10 в playerMove
// 2! - сделать цикл с ходами (только игрока)
// 3! - ход компуктера после твоего промаха
