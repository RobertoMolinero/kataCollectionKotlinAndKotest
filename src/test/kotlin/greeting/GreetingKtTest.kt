package greeting

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe

class GreetingKtTest : FunSpec({

    test("Requirement 1: The method greet(name) interpolates 'name' in a simple greeting.") {
        table(
            headers("name", "result"),
            row("Bob", "Hello, Bob."),
            row("Jill", "Hello, Jill."),
            row("Jane", "Hello, Jane."),
            row("Amy", "Hello, Amy."),
            row("Brian", "Hello, Brian."),
            row("Charlotte", "Hello, Charlotte.")
        ).forAll { name, result ->
            greet(name) shouldBe result
        }
    }

    test("Requirement 2: When name is 'null', then the method should return the string \"Hello, my friend.\"") {
        greet() shouldBe "Hello, my friend."
    }

    test("Requirement 3: When name is all uppercase, then the method should shout back to the user.") {
        table(
            headers("name", "result"),
            row("JERRY", "HELLO JERRY!"),
            row("JILL", "HELLO JILL!"),
            row("JANE", "HELLO JANE!"),
            row("AMY", "HELLO AMY!")
        ).forAll { name, result ->
            greet(name) shouldBe result
        }
    }

    test("Requirement 4: Handle two names of input.") {
        table(
            headers("list", "result"),
            row(listOf("Jill", "Jane"), "Hello, Jill and Jane."),
            row(listOf("Amy", "Brian"), "Hello, Amy and Brian."),
            row(listOf("Bob", "Charlotte"), "Hello, Bob and Charlotte.")
        ).forAll { list, result ->
            greet(list) shouldBe result
        }
    }

    test("Requirement 5: Handle an arbitrary number of names as input for greeting.") {
        table(
            headers("list", "result"),
            row(listOf(), ""),
            row(listOf("Amy"), "Hello, Amy."),
            row(listOf("Amy", "Brian"), "Hello, Amy and Brian."),
            row(listOf("Amy", "Brian", "Charlotte"), "Hello, Amy, Brian and Charlotte."),
            row(listOf("Amy", "Brian", "Charlotte", "Jill"), "Hello, Amy, Brian, Charlotte and Jill."),
            row(
                listOf("Amy", "Brian", "Charlotte", "Jill", "Jane"),
                "Hello, Amy, Brian, Charlotte, Jill and Jane."
            ),
            row(
                listOf("Amy", "Brian", "Charlotte", "Jill", "Jane", "Bob"),
                "Hello, Amy, Brian, Charlotte, Jill, Jane and Bob."
            )
        ).forAll { list, result ->
            greet(list) shouldBe result
        }
    }

    test("Requirement 6: Allow mixing of normal and shouted names by separating the response into two greetings.") {
        table(
            headers("list", "result"),
            row(listOf("Amy", "BRIAN", "Charlotte"), "Hello, Amy and Charlotte. AND HELLO BRIAN!"),
            row(
                listOf("Amy", "BOB", "BRIAN", "Charlotte"),
                "Hello, Amy and Charlotte. AND HELLO BOB! AND HELLO BRIAN!"
            ),
            row(listOf("Amy", "Brian", "CHARLOTTE"), "Hello, Amy and Brian. AND HELLO CHARLOTTE!"),
            row(
                listOf("Amy", "BOB", "Brian", "CHARLOTTE"),
                "Hello, Amy and Brian. AND HELLO BOB! AND HELLO CHARLOTTE!"
            ),
            row(listOf("Amy", "Brian"), "Hello, Amy and Brian."),
            row(listOf("BOB", "CHARLOTTE"), "HELLO BOB! AND HELLO CHARLOTTE!"),
            row(listOf("Amy", "CHARLOTTE"), "Hello, Amy. AND HELLO CHARLOTTE!"),
        ).forAll { list, result ->
            greet(list) shouldBe result
        }
    }

    test("Requirement 6a: A mixed list is correctly split into upper and lower case") {
        table(
            headers("list", "result"),
            row(listOf("Amy", "BRIAN", "Charlotte"), Pair(listOf("Amy", "Charlotte"), listOf("BRIAN"))),
            row(
                listOf("Amy", "BOB", "BRIAN", "Charlotte"),
                Pair(listOf("Amy", "Charlotte"), listOf("BOB", "BRIAN"))
            ),
            row(listOf("Amy", "Brian", "CHARLOTTE"), Pair(listOf("Amy", "Brian"), listOf("CHARLOTTE"))),
            row(
                listOf("Amy", "BOB", "Brian", "CHARLOTTE"),
                Pair(listOf("Amy", "Brian"), listOf("BOB", "CHARLOTTE"))
            ),
            row(listOf("Amy", "Brian"), Pair(listOf("Amy", "Brian"), listOf())),
            row(listOf("BOB", "CHARLOTTE"), Pair(listOf(), listOf("BOB", "CHARLOTTE"))),
            row(listOf("Amy", "CHARLOTTE"), Pair(listOf("Amy"), listOf("CHARLOTTE")))
        ).forAll { list, result ->
            splitListForGreetAndShout(list) shouldBe result
        }
    }

    test("Requirement 6b: Handle an arbitrary number of names as input for shouting.") {
        table(
            headers("list", "firstElement", "result"),
            row(listOf(), false, ""),
            row(listOf(), true, ""),
            row(listOf("JILL"), false, "AND HELLO JILL!"),
            row(listOf("JILL"), true, "HELLO JILL!"),
            row(listOf("JILL", "JANE"), false, "AND HELLO JILL! AND HELLO JANE!"),
            row(listOf("JILL", "JANE"), true, "HELLO JILL! AND HELLO JANE!"),
            row(listOf("BOB", "JILL", "JANE"), false, "AND HELLO BOB! AND HELLO JILL! AND HELLO JANE!"),
            row(listOf("BOB", "JILL", "JANE"), true, "HELLO BOB! AND HELLO JILL! AND HELLO JANE!"),
            row(
                listOf("AMY", "BOB", "JILL", "JANE"),
                false,
                "AND HELLO AMY! AND HELLO BOB! AND HELLO JILL! AND HELLO JANE!"
            ),
            row(
                listOf("AMY", "BOB", "JILL", "JANE"),
                true,
                "HELLO AMY! AND HELLO BOB! AND HELLO JILL! AND HELLO JANE!"
            ),
            row(
                listOf("BRIAN", "AMY", "BOB", "JILL", "JANE"),
                false,
                "AND HELLO BRIAN! AND HELLO AMY! AND HELLO BOB! AND HELLO JILL! AND HELLO JANE!"
            ),
            row(
                listOf("BRIAN", "AMY", "BOB", "JILL", "JANE"),
                true,
                "HELLO BRIAN! AND HELLO AMY! AND HELLO BOB! AND HELLO JILL! AND HELLO JANE!"
            ),
            row(
                listOf("CHARLOTTE", "BRIAN", "AMY", "BOB", "JILL", "JANE"),
                false,
                "AND HELLO CHARLOTTE! AND HELLO BRIAN! AND HELLO AMY! AND HELLO BOB! AND HELLO JILL! AND HELLO JANE!"
            ),
            row(
                listOf("CHARLOTTE", "BRIAN", "AMY", "BOB", "JILL", "JANE"),
                true,
                "HELLO CHARLOTTE! AND HELLO BRIAN! AND HELLO AMY! AND HELLO BOB! AND HELLO JILL! AND HELLO JANE!"
            )
        ).forAll { list, firstElement, result ->
            shoutingToListOfPeople(list, firstElement) shouldBe result
        }
    }

    test("Requirement 7: If any entries in name are a string containing a comma, split it as its own input.") {
        table(
            headers("list", "result"),
            row(listOf("Bob", "Charlie, Dianne"), "Hello, Bob, Charlie and Dianne."),
            row(listOf("Bob", "Charlie, Dianne, Amy"), "Hello, Bob, Charlie, Dianne and Amy."),
            row(listOf("Bob, Charlotte", "Charlie, Dianne"), "Hello, Bob, Charlotte, Charlie and Dianne."),
            row(listOf("Bob, Charlotte", "Dianne"), "Hello, Bob, Charlotte and Dianne."),
            row(listOf("BOB, CHARLOTTE"), "HELLO BOB! AND HELLO CHARLOTTE!"),
            row(listOf("Amy, BOB", "Brian, CHARLOTTE"), "Hello, Amy and Brian. AND HELLO BOB! AND HELLO CHARLOTTE!")
        ).forAll { list, result ->
            greet(list) shouldBe result
        }
    }

    test("Requirement 7a: If any entries in name are a string containing a comma, the list will be expanded.") {
        table(
            headers("list", "result"),
            row(listOf("Bob", "Charlie, Dianne"), listOf("Bob", "Charlie", "Dianne")),
            row(listOf("Bob", "Charlie, Dianne, Amy"), listOf("Bob", "Charlie", "Dianne", "Amy")),
            row(listOf("Bob, Charlotte", "Charlie, Dianne"), listOf("Bob", "Charlotte", "Charlie", "Dianne")),
            row(listOf("Bob, Charlotte", "Dianne"), listOf("Bob", "Charlotte", "Dianne")),
            row(listOf("BOB, CHARLOTTE"), listOf("BOB", "CHARLOTTE")),
            row(listOf("Amy, BOB", "Brian, CHARLOTTE"), listOf("Amy", "BOB", "Brian", "CHARLOTTE"))
        ).forAll { list, result ->
            expandList(list) shouldBe result
        }
    }

    test("Requirement 8: Allow the input to escape intentional commas introduced by Requirement 7.") {
        table(
            headers("list", "result"),
            row(listOf("Bob", "\"Charlie, Dianne\""), "Hello, Bob and Charlie, Dianne."),
            row(listOf("\"Bob, Charlotte\"", "\"Charlie, Dianne\""), "Hello, Bob, Charlotte and Charlie, Dianne."),
            row(listOf("\"Bob, Charlotte\"", "Charlie"), "Hello, Bob, Charlotte and Charlie."),
            row(listOf("\"BOB, CHARLOTTE\""), "HELLO BOB, CHARLOTTE!"),
            row(listOf("\"Bob, Charlotte\""), "Hello, Bob, Charlotte."),
            row(listOf("\"BOB, Charlotte\""), "Hello, BOB, Charlotte."),
            row(listOf("\"Bob, CHARLOTTE\""), "Hello, Bob, CHARLOTTE.")
        ).forAll { list, result ->
            greet(list) shouldBe result
        }
    }
})
