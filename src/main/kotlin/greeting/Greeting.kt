package greeting

private const val shoutingFormula = "HELLO %s!"
private const val shoutingFormulaWithAnd = " AND HELLO %s!"

private const val greetingFormula = "Hello, %s."
private const val unknownName = "my friend"

private const val greetingStart = "Hello, %s"
private const val greetingEnd = "%s and %s."
private const val separator = ", "

fun greet(name: String = unknownName): String {
    if (name.toUpperCase() == name) {
        return String.format(shoutingFormula, name)
    }
    return String.format(greetingFormula, name)
}

fun greet(list: List<String>): String {
    val expandList = expandList(list)
    val (lowercase, uppercase) = splitListForGreetAndShout(expandList)

    val greetingResult = greetingListOfPeople(lowercase)
    val shoutingResult = shoutingToListOfPeople(uppercase, lowercase.isEmpty())

    return "$greetingResult $shoutingResult".trim()
}

fun greetingListOfPeople(list: List<String>): String {
    if (list.isEmpty()) return ""
    if (list.size == 1) return greet(list[0])

    val takeLast = list.takeLast(2)
    val endOfSentence = String.format(greetingEnd, takeLast[0], takeLast[1])

    val beginning = list.dropLast(2)
    val reversed = beginning.asReversed()

    val stringBuilder = StringBuilder(endOfSentence)

    for (s in reversed) {
        stringBuilder.insert(0, s + separator)
    }

    return String.format(greetingStart, stringBuilder.toString())
}

fun shoutingToListOfPeople(list: List<String>, firstElement: Boolean): String {
    if (list.isEmpty()) return ""

    val stringBuilder = StringBuilder()

    if (firstElement)
        stringBuilder.append(String.format(shoutingFormula, list[0]))

    val tail = if (firstElement)
        list.drop(1)
    else
        list

    for (name in tail)
        stringBuilder.append(String.format(shoutingFormulaWithAnd, name))

    return stringBuilder.toString().trim()
}

fun splitListForGreetAndShout(mixedList: List<String>): Pair<List<String>, List<String>> {
    val partition = mixedList.partition { it.toUpperCase() != it }
    return Pair(partition.first, partition.second)
}

fun expandList(list: List<String>): List<String> {
    val expandedListOfNames = mutableListOf<String>()

    for (name in list)
        if (name.contains(separator) && !isEscaped(name))
            expandedListOfNames.addAll(name.split(",").map { c -> c.trim() })
        else expandedListOfNames.add(name.trim('\"'))

    return expandedListOfNames
}

fun isEscaped(name: String): Boolean {
    return name.startsWith("\"") && name.endsWith("\"")
}
