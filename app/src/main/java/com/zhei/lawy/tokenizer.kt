package com.zhei.lawy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.io.File

const val save_path_tokens = "C:\\Users\\esteb\\OneDrive\\Escritorio\\lawy_ia_py\\vocabulary.json"
const val doc_to_read = "C:\\Users\\esteb\\OneDrive\\Escritorio\\lawy_ia_py\\const_CO.txt"

fun main () { tokenizerToAdd() }


fun convertTextToListWords() : List<String>
{
    val textRed = File(doc_to_read).readText()
    var chain = ""
    val listFilled = mutableListOf("")
    textRed.forEach { char ->
        if (char != ' ') {
            chain += char
        } else {
            listFilled.add(chain)
            chain = ""
        }
    }
    return listFilled
}


fun tokenizerToAdd ()
{
    val jsonFile = File(save_path_tokens)

    val first = (65..90).map { Char(it) }
    val second = (97..122).map { Char(it) }
    val three = (160..223).map { Char(it) }
    val four = (224..400).map { Char(it) }

    println(first)
    println(second)
    println(three)
    println(four)

    if (jsonFile.exists()) {
        convertTextToListWords().forEach { vocab ->

            val jString = jsonFile.readText()
            val jElement = Json.parseToJsonElement(jString) as JsonObject
            val jS = jElement.toMutableMap()

            var newChar = ""
            vocab.forEach { character ->
                if (character in first || character in second || character in three || character in four) {
                    newChar += character
                }
            }

            println(jElement)

            /*jElement.forEach { (key, value) ->
                if (key != newChar) {
                    val lastvalue = JsonPrimitive((jS[jElement.keys.last()]?.jsonPrimitive?.long ?: 0) + 1)
                    if (value != lastvalue) {
                        jS[newChar] = lastvalue
                        jsonFile.writeText(Json.encodeToString(JsonObject(jS)))
                    }
                }
            }*/

            newChar = ""
        }
    }
}