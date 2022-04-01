package search
import java.io.File

val peoples = mutableListOf<String>()
var wordsMap = mutableMapOf<String, MutableList<Int>>()
var isfinsh = false
fun main(args: Array<String>) {
    val fileName = args[1]
    File(fileName).forEachLine { peoples.add(it) }
    for (index in peoples.indices) {
        peoples[index].split(" ").forEach {
            if (wordsMap.containsKey(it.lowercase()))
                wordsMap[it.lowercase()]?.add(index)
            else
                wordsMap.put(it.lowercase(), mutableListOf(index))
        }
    }
    //println(wordsMap)
    do {
        printMenu()
        val menuNumber = readln().split(" ")[0].toInt()
        when(menuNumber) {
            1 -> { findAperson() }
            2 -> { printAllPeople() }
            0 -> {
                println("Bye!")
                break
            }
            else -> {
                println("Incorrect option! Try again.")
            }
        }        
    }while(true)
}
fun printMenu() {
    println("""
=== Menu ===
1. Find a person
2. Print all people
0. Exit""")
}
fun printAllPeople() {
    println("=== List of people ===")
    peoples.forEach { println(it) }
}
fun findAperson() {
    println("Select a matching strategy: ALL, ANY, NONE")
    val strategy = readln().split(" ")[0]
    if (strategy != "ALL" && strategy != "ANY" && strategy != "NONE") {
        println("error strategy")
        return
    }
    println("\nEnter a name or email to search all suitable people.")
    val seachPeopleList: List<String> = readln().split(" ")
    var resultMutableMap: MutableMap<String, MutableList<Int>> = mutableMapOf()
    var resultMutableListList: MutableList<MutableList<Int>> = mutableListOf()
    var resultMutableList_ALL: MutableList<Int> = mutableListOf()
    var resultMutableListMap: MutableMap<Int, Int> = mutableMapOf()
    var resultMutableSet: MutableSet<Int> = mutableSetOf()
    wordsMap.forEach { if (it.key in seachPeopleList) resultMutableListList.add(it.value) }
    resultMutableListList.forEach { it.forEach { resultMutableSet.add(it) } }
    resultMutableListList.forEach { it.forEach { resultMutableListMap[it] = (resultMutableListMap[it]?: 0) + 1 } }
    println("resultMutableListList:"+resultMutableListList)
    println("resultMutableSet:"+resultMutableSet)
    println("resultMutableListMap:"+resultMutableListMap)
    when (strategy) {
        "ALL" -> {
            val list = resultMutableListMap.filter { it.value == resultMutableListList.size }
            if(list.isNullOrEmpty()){
                println("No matching people found.")
            } else {
                println("${list.size} persons found:")
                list.forEach { println(peoples[it.key]) }
            }
        }
        "ANY" -> {
            if(resultMutableSet.isNullOrEmpty()){
                println("No matching people found.")
            } else {
                println("${resultMutableSet.size} persons found:")
                resultMutableSet.forEach { println(peoples[it]) }
            }
        }
        "NONE" -> {
            if(resultMutableSet.isNullOrEmpty()){
                println("${peoples.size} persons found:")
                peoples.forEach { println(it) }
            } else {
                println("${peoples.size - resultMutableSet.size} persons found:")
                for (index in peoples.indices) {
                    if (index !in resultMutableSet)
                        println(peoples[index])
                }
            }
        }
        else -> println("error strategy")
    }
}