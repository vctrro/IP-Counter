import java.io.File
import kotlin.system.measureNanoTime

fun main() {

    val fileName = "ip.txt"
    val ipTree = BinaryTree()
    val ipTree1 = BinaryTree()
    val result = mutableSetOf<String>()
    val result1 = mutableSetOf<String>()
    File(fileName).forEachLine {
        it.split(".").let {
            ipTree1.addIP(it[0].toLong()*it[1].toLong()*it[2].toLong()*it[3].toLong())
        }
    }
    ipTree1.counter = 0

    val runTime0 = measureNanoTime {
        File(fileName).forEachLine {
            ipTree1.addIP(it.filterNot { it == '.' }.toLong())
        }
    }
    println(runTime0.toDouble()/1000000000)
    println(ipTree1.counter)

    val runTime1 = measureNanoTime {
        File(fileName).forEachLine {
            result.add(it)
        }
    }

    val runTime = measureNanoTime {
        File(fileName).forEachLine {
            it.split(".").let {
                ipTree.addIP(it[0].toLong().shl(24)+it[1].toLong().shl(16)+it[2].toLong().shl(8)+it[3].toLong())
            }
        }
    }
    println(runTime1.toDouble()/1000000000)
    println(result.size)
    println(runTime.toDouble()/1000000000)
    println(ipTree.counter)
}

class BinaryTree {
    class IP(val ipValue: Long) {
        var left: IP? = null
        var right: IP? = null
    }
    private var root: IP? = null
    var counter = 0

    //Распределяем узлы по уникальному значению IP (бинарное дерево поиска)
    private fun newIP(current: IP?, ipValue: Long): IP {
        if (current == null) {
            counter++
            return IP(ipValue)
        }
        if (ipValue < current.ipValue) {
            current.left = newIP(current.left, ipValue)
        }
        if (ipValue > current.ipValue) {
            current.right = newIP(current.right, ipValue)
        }
        return current
    }

    //Наполняем  дерево
    fun addIP(ipValue: Long) {
        root = newIP(root, ipValue)
    }
}