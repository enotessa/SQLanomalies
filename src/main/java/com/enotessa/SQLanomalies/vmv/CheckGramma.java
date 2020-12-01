/*
package com.enotessa.SQLanomalies.mutz;

import org.python.util.PythonInterpreter;
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;

import java.util.*;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

*/
/**
 * Модель: Вывод структуры строки
 *//*

public class CheckGramma {
    PythonInterpreter interpreter = new PythonInterpreter();
    ArrayList<ArrayList<String>> tokenizedSequences = new ArrayList<>();
    ArrayList<String> tokenizedSequence = new ArrayList<>();

    public void train(ArrayList<String> sequences) {
        interpreter.exec("tokenized_sequences = []");
        for (String sequence : sequences){
            interpreter.set("sequence", sequence);
            interpreter.exec("tokenizedSequence = tokenize(sequence)\n" +
                    "tokenized_sequences.append(tokenized_sequence)");
            tokenizedSequences =interpreter.get("result", String.class);


            // Set a variable with the content you want to work with
            interpreter.set("code", content);

            // Simple use Pygments as you would in Python
            interpreter.exec("from pygments import highlight\n"
                    + "from pygments.lexers import PythonLexer\n"
                    + "from pygments.formatters import HtmlFormatter\n"
                    + "\nresult = highlight(code, PythonLexer(), HtmlFormatter())");

            return interpreter.get("result", String.class);

            tokenizedSequence = tokenize(sequence);
            tokenizedSequences.add(tokenizedSequence);

            mergeSequence(tokenizedSequence, tokenizedSequences);
            */
/*
            tokenized_sequence = tokenize(sequence)
        tokenized_sequences.append(tokenized_sequence)
        model.merge_sequence(tokenized_sequence, tokenized_sequences)
             *//*

        }
    }

    public boolean validate(String sequence) {
        return true;
    }
}

    public static void main(String[] args) throws PyException {
        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("import sys");
        interp.exec("print sys");
        interp.set("a", new PyInteger(42));
        interp.exec("print a");
        interp.exec("x = 2+2");
        PyObject x = interp.get("x");
        System.out.println("x: " + x);
    }
*/
