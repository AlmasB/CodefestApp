package codefest.client

import codefest.common.data.Challenge
import javafx.beans.binding.Bindings.isEmpty
import javafx.collections.FXCollections
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import java.io.File
import java.net.URI
import java.net.URLClassLoader
import java.util.*
import javax.tools.DiagnosticCollector
import javax.tools.JavaFileObject
import javax.tools.SimpleJavaFileObject
import javax.tools.ToolProvider


/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SubmitController {

    @FXML
    private lateinit var labelChallenge: Label
    @FXML
    private lateinit var areaOutput: TextArea
    @FXML
    private lateinit var areaInput: TextArea
    @FXML
    private lateinit var btnSubmit: Button
    @FXML
    private lateinit var btnPrev: Button
    @FXML
    private lateinit var btnNext: Button

    private val challenges = FXCollections.observableArrayList<Challenge>()
    private lateinit var selectedChallenge: Challenge
    private var challengeIndex = 0

    fun initialize() {
        btnSubmit.disableProperty().bind(isEmpty(challenges))
        btnPrev.disableProperty().bind(isEmpty(challenges))
        btnNext.disableProperty().bind(isEmpty(challenges))

        Server.requestChallenges {
            onSuccess = {
                challenges += it.challenges
                selectChallenge(it.challenges.first())
            }
        }
    }

    private fun selectChallenge(challenge: Challenge) {
        selectedChallenge = challenge

        var text = "Challenge N${challenge.id}\n\n"

        challenge.params.forEach {
            text += "challenge(${it.inputs.joinToString(", ")}) is ${it.output}\n"
        }

        labelChallenge.text = text

        val sb = StringBuilder(64)
        sb.append("public class Solution {\n")
        sb.append("    ${challenge.signature} {\n")
        sb.append("        \n")
        sb.append("    }\n")
        sb.append("}\n")

        areaInput.text = sb.toString()
    }

    fun onPrev() {
        if (challengeIndex == 0)
            return

        challengeIndex--
        selectChallenge(challenges[challengeIndex])
    }

    fun onNext() {
        if (challengeIndex == challenges.size - 1)
            return

        challengeIndex++
        selectChallenge(challenges[challengeIndex])
    }

    fun onSubmit() {
        val text = areaInput.text

        Server.requestSubmit(2)

        // TODO: use Service
        Thread(CompileAndExecuteTask(text, areaOutput, selectedChallenge)).start()
    }

    /**
     * Constructs a new JavaSourceFromString.
     * @param name the name of the compilation unit represented by this file object
     * @param code the source code for the compilation unit represented by this file object
     */
    private class JavaSourceFromString constructor(name: String, val code: String) :
            SimpleJavaFileObject(URI.create("string:///" + name.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
                    JavaFileObject.Kind.SOURCE) {

        override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence {
            return code
        }
    }

    private class CompileAndExecuteTask(
            val code: String,
            val output: TextArea,
            val challenge: Challenge) : Task<Void>() {
        var challengeResult: String = ""

        override fun call(): Void? {

            // adapted from https://stackoverflow.com/questions/21544446/how-do-you-dynamically-compile-and-load-external-java-classes

            val diagnostics = DiagnosticCollector<JavaFileObject>()
            val compiler = ToolProvider.getSystemJavaCompiler()
            val fileManager = compiler.getStandardFileManager(diagnostics, null, null)

            val task = compiler.getTask(null, fileManager, diagnostics, emptyList(), null, listOf(JavaSourceFromString("Solution", code)))

            if (task.call()) {
                val classLoader = URLClassLoader(arrayOf(File("./").toURI().toURL()))
                val loadedClass = classLoader.loadClass("Solution")

                val obj = loadedClass.getDeclaredConstructor().newInstance()

                val m = obj.javaClass.declaredMethods.find { it.name == "challenge" }!!

                var totalTests = 0
                var passedTests = 0
                challenge.params.forEach {
                    val result = m.invoke(obj, *it.inputs.toTypedArray())

                    if (result == it.output) {
                        challengeResult += "\u2713 ${it.inputs}: OK!\n"
                        passedTests++
                    } else {
                        challengeResult += "\u2717 ${it.inputs}: Expected: ${it.output}. Got: $result\n"
                    }
                    totalTests++
                }

                // Add test summary at top of output.
                challengeResult = String.format("Total: %s, Passed: %s, Failing: %s\n%s",
                        totalTests,
                        passedTests,
                        totalTests - passedTests,
                        challengeResult)
            } else {
                for ((errorNo, diagnostic) in diagnostics.diagnostics.withIndex()) {
                    challengeResult += String.format("[$errorNo] Error on line %d in %s - %s\n",
                            diagnostic.lineNumber,
                            diagnostic.source.toUri(),
                            diagnostic.getMessage(Locale.UK))
                }
            }
            fileManager.close()

            return null
        }

        override fun failed() {
            output.text = exception.toString()
        }

        override fun succeeded() {
            output.text = challengeResult
        }
    }
}