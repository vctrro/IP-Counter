import java.io.File
import kotlin.system.measureNanoTime

fun main() {

    val fileName = "ip1.txt"
    val ipTree = IPTree()
    val ipTree1 = IPTree()
    val result = mutableSetOf<String>()
    val runTime0 = measureNanoTime {
        File(fileName).forEachLine {
            it.split(".").let {
                ipTree1.inflateTree(it[0].toUByte(), it[1].toUByte(), it[2].toUByte(), it[3].toUByte())
            }
        }
    }
    IPTree.counter = 0
    val runTime1 = measureNanoTime {
        File(fileName).forEachLine {
            result.add(it)
        }
    }

    val runTime = measureNanoTime {
        File(fileName).forEachLine {
            it.split(".").let {
                ipTree.inflateTree(it[0].toUByte(), it[1].toUByte(), it[2].toUByte(), it[3].toUByte())
            }
        }
    }
    println(runTime1.toDouble()/1000000000)
    println(result.size)
    println(runTime.toDouble()/1000000000)
    println(IPTree.counter)
}

open class IPTree {
    open class Octet(val value: UByte) {
        var left: Octet? = null
        var right: Octet? = null
        val subTree by lazy {
            counter--
            IPTree()
        }
    }
    private var root/*: Octet? = null*/ = Octet(127.toUByte())
    private var value127 = false

    companion object {
        var counter = 0
    }

    /*class LastTree : IPTree() {
            companion object {
                var counter = 0
            }

            class LastOctet(value: UByte) : Octet(value) {
                var lastSubTree = LastTree()
            }

        private fun newOct(current: LastOctet?, value: UByte): LastOctet {
            if (current == null) {
                counter++
                return LastOctet(value)
            }
            if (value < current.value) {
                current.left = newOct(current.left, value)
            }
            if (value > current.value) {
                current.right = newOct(current.right, value)
            }
            return current
        }
    }*/



    //Распределяем узлы по уникальному значению (бинарное дерево поиска)
    private fun newOctet(current: Octet?, value: UByte): Octet {
        if (current == null) {
            counter++
            return Octet(value)
        }
        if (value == 127.toUByte()) {
            if (!value127) {
                counter++
                value127 = true
            }
            return root
        }
        if (value < current.value) {
            if (current.left == null)
            current.left = newOctet(current.left, value)
            return current.left!!
        }
        if (value > current.value) {
            current.right = newOctet(current.right, value)
            return current.right!!
        }
        //counter++
        return current
    }

    //Наполняем  дерево
    fun inflateTree(octet1: UByte, octet2: UByte, octet3: UByte, octet4: UByte) {
        newOctet(root, octet1).apply {
            subTree.newOctet(subTree.root, octet2).apply {
                subTree.newOctet(subTree.root, octet3).apply {
                    subTree.newOctet(subTree.root, octet4)
                }
            }
        }
    }
}