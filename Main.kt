package calculator
import java.math.BigInteger

var readline: MutableList<String> = mutableListOf<String>()
var numberlist: MutableList<String> = mutableListOf<String>()
var operatorlist: MutableList<String> = mutableListOf<String>()
var numberMutableMap: MutableMap<String, String> = mutableMapOf<String, String>()
val regex1 = "[+-]?\\d+".toRegex()
val regex2 = "/\\w+".toRegex()
val regexNumOrOp = "(\\(*[+-]?\\d+\\)*|[+-/*]+)".toRegex()
val regexNum = "\\(*[+-]?[0-9]+\\)*".toRegex()
val regexVar = "\\(*[a-zA-Z]+\\)*".toRegex()
val regexOp = "[-+*/]+".toRegex()

fun main(args: Array<String>) {
    do {
        val readstring = readln()
        if (readstring.contains("=")) {
            assignment(readstring)
        } else {
            readline = readstring.split("\\s+".toRegex()).toMutableList()
            numberlist.clear()
            operatorlist.clear()
            //println(readline)
            if(readline.size>2)
            readline = readline.filter { it != "" }.toMutableList()
            //println(readline)
            when (readline[0]) {
                "/help" -> println("The program calculates the sum of numbers")
                "/exit" -> break
                else -> {
                    if (readline[0] != "") {
                        //println(readline[0])
                        if(readline.size == 1) {
                            if (regexVar.matches(readline[0])) {
                                println(numberMutableMap[readline[0]]?: "Unknown variable")
                            } else if (regexNum.matches(readline[0])) {
                                println(BigInteger(readline[0]))
                            } else if (regex2.matches(readline[0])) {
                                println("Unknown command")
                            } else {
                                println("Invalid identifier")
                            }
                        } else {
                            if (isExpression()) {
                                val suffix = infillToSuffix(readline)
                                //println(readline)
                                if(suffix == null) continue
                                //println("infill:"+readline)
                                //println("suffix:"+suffix)
                                runcalForSuffix(suffix)
                                //runcal1()
                                //runcal2(readline)
                            } else println("Invalid expression")
                        }
                    }
                }
            }
        }
    } while(true)
    println("Bye!")
}
fun runcalForSuffix(suffix: MutableList<String>) {
    var LIFO_sum: MutableList<String> = mutableListOf()
    suffix.forEach {
        if ("[+-]?[0-9]+".toRegex().matches(it)){
            LIFO_sum.add(it)
        } else {
            val value2 = BigInteger(LIFO_sum.last())
            LIFO_sum.removeLast()
            val value1 = BigInteger(LIFO_sum.last())
            LIFO_sum.removeLast()
            when (it) {
                "+" -> LIFO_sum.add((value1 + value2).toString())
                "-" -> LIFO_sum.add((value1 - value2).toString())
                "*" -> LIFO_sum.add((value1 * value2).toString())
                "/" -> LIFO_sum.add((value1 / value2).toString())
                else -> {
                    println("Invalid expression")
                    return
                }
            }
        }
        //println("LIFO_sum:"+LIFO_sum)
    }
    println(LIFO_sum.first())
}
fun matchop(op1: String, op2: String): Boolean {
    return when (op1) {
        "*" -> if (op2 == "*" || op2 == "/") false else true
        "/" -> if (op2 == "*" || op2 == "/") false else true
        "+" -> false
        "-" -> false
        else -> false
    }

}
fun infillToSuffix(infill: MutableList<String>): MutableList<String>? {
    //var infill: MutableList<String> = readline.split("\\s+".toRegex()).toMutableList()
    var LIFO_operator: MutableList<String> = mutableListOf<String>()
    var LIFO_Suffix: MutableList<String> = mutableListOf<String>()

    //println("infillToSuffix:"+ infill)
    infill.forEach {
        when {
            regexVar.matches(it) || regexNum.matches(it) -> {
                val opleft = it.filter { it == '(' }
                val opright = it.filter { it == ')' }
                val value = it.replace("(", "").replace(")", "")
                LIFO_Suffix.add(value)
                if (opleft.length > 0)  {
                    repeat (opleft.length) {
                        LIFO_operator.add("(")
                    }
                } else if (opright.length > 0 ) {
                    repeat (opright.length) {
                        while (LIFO_operator.isNotEmpty()  && LIFO_operator.last() != "(") {
                            LIFO_Suffix.add(LIFO_operator.last())
                            LIFO_operator.removeLast()
                        }
                        if(LIFO_operator.isEmpty()) {
                            println("Invalid expression")
                            return null
                        }
                        LIFO_operator.removeLast()
                    }
                }
            }
            regexOp.matches(it) -> {
                while (true) {
                    if (LIFO_operator.size == 0 || LIFO_operator.last() == "(" || matchop(it, LIFO_operator.last())) {
                        LIFO_operator.add(it)
                        break
                    } else if (!matchop(it, LIFO_operator.last())){
                        LIFO_Suffix.add(LIFO_operator.last())
                        LIFO_operator.removeLast()
                    }
                }
            }
            else -> {
                println("Invalid assignment 00")
                return null
            }
        }
        //println("it:"+it)
        //println("LIFO_operator:"+LIFO_operator)
        //println("LIFO_Suffix:"+LIFO_Suffix)
    }
    while (LIFO_operator.size > 0) {
        LIFO_Suffix.add(LIFO_operator.last())
        LIFO_operator.removeLast()
    }
    return LIFO_Suffix
}
fun assignment(readstr: String) {
    var op = readstr.split("=").toMutableList()
    if (op.size != 2) {
        println("Invalid assignment")
        return
    } else {
        //println(op[0])
        op[0] = op[0].replace("\\s+".toRegex(), "")
        //println(op[0])
        if (!regexVar.matches(op[0])) {
            println("Invalid identifier")
            return
        }
        var op2 = op[1].split("\\s+".toRegex()).toMutableList()
        op2 = op2.filter { it != "" }.toMutableList()
        if (op2.size % 2 == 0 || op2.size == 2) {
            println("Invalid assignment")
            return
        } else if (op2.size == 1) {
            op[1] = op[1].replace("\\s+".toRegex(), "")
            if (regexNum.matches(op[1]) ) {
                numberMutableMap.set(op[0], op[1])
            } else if (regexVar.matches(op[1])) {
                if (numberMutableMap.contains(op[1]))
                    numberMutableMap.set(op[0], numberMutableMap.getValue(op[1]))
                else {
                    println("Unknown variable")
                    return
                }
            } else {
                println("Invalid assignment")
                return
            }
        } else {
            op2.forEachIndexed { index, s ->
                op2[index] = op2[index].replace("\\s+".toRegex(), "")
            }
            var sum = BigInteger.ZERO
            var isplus = true
            op2.forEachIndexed { index, s ->
                if (regexNum.matches(op2[index])) {
                    if (isplus) sum += BigInteger(op2[index])
                    else sum -= BigInteger(op2[index])
                } else if (regexVar.matches(op2[index])) {
                    if (numberMutableMap.contains(op2[index])) {
                        if (isplus) sum += BigInteger(numberMutableMap[op2[index]]!!)
                        else sum -= BigInteger(numberMutableMap[op2[index]]!!)
                    } else {
                        println("Unknown variable")
                        return
                    }
                } else if (regexOp.matches(op2[index])) {
                    while(op2[index].length>1){
                        op2[index] = op2[index].replace("++", "+")
                                .replace("--", "+")
                                .replace("+-", "-")
                                .replace("-+", "-")
                    }
                    isplus = when (op2[index]) {
                        "-" -> false
                        "+" -> true
                        else -> true
                    }
                } else {
                    println("Invalid assignment")
                    return
                }
            }
            numberMutableMap.set(op[0], sum.toString())
        }
    }
}
fun isExpression(): Boolean {
    //println("isExpression:"+ readline)
    readline.forEachIndexed  { i, _ ->
        if ("[+-]+".toRegex().matches(readline[i])) {
            while (readline[i].length > 1) {
                readline[i] = readline[i].replace("--", "+").
                replace("++", "+").
                replace("+-", "-").
                replace("-+", "-")
            }
        }
    }
    //println("isExpression 00 :"+ readline)
    readline.forEachIndexed { index, s ->
        if (regexVar.matches(readline[index])) {
            if (numberMutableMap.contains(readline[index]))
                readline[index] = numberMutableMap[readline[index]].toString()
            else {
                println("Unknown variable")
                return false
            }
        }
    }
    readline.forEach { if(!regexNumOrOp.matches(it)) return false }
    readline.forEachIndexed { i, _ ->
        if(i < readline.size-1 && regexNum.matches(readline[i]) && regexNum.matches(readline[i+1]))
            return false
    }
    return true
}
fun runcal1() {
    var sum = 0
    if (readline.size > 2) {
        for (index in readline.indices) {
            if (index % 2 == 0)
                numberlist.add(readline[index])
            else
                operatorlist.add(readline[index])
        }
        //println(numberlist)
        //println(operatorlist)
        sum = numberlist[0].toInt()
        for (index in 1..(numberlist.size-1)) {
            if (ispuls(operatorlist[index-1]))
                sum += numberlist[index].toInt()
            else
                sum -= numberlist[index].toInt()
            //println("index:"+index+" sum:"+sum)
        }
    } else {
        sum = readline[0].toInt()
    }
    println(sum)
}
fun runcal2(readline: MutableList<String>) {
    readline.forEachIndexed  { i, _ ->
        if (readline[i].toIntOrNull() == null) {
            while (readline[i].length > 1) {
                readline[i] = readline[i].replace("--", "+").
                replace("++", "+").
                replace("+-", "-").
                replace("-+", "-")
            }
        }
    }
    readline.forEachIndexed { i, _ ->
        if (readline[i] == "-" && readline[i + 1].toInt() < 0) {
            readline[i + 1] = readline[i + 1].substringAfter("-")
        } else if (readline[i] == "-" && readline[i + 1].toInt() > 0) {
            readline[i + 1] = "-${readline[i + 1]}"
        }
    }
    readline.removeAll(listOf("+", "-"))
    println(readline.sumOf { it.toInt() })
}
fun ispuls(operator: String): Boolean {
    val op = operator.filter { it == '-' }
    //println(op)
    //println("len:"+op.length)
    return op.length % 2 == 0
}