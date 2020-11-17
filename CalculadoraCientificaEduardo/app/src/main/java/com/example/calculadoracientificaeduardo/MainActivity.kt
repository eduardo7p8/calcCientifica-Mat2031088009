package com.example.calculadoracientificaeduardo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.udojava.evalex.Expression
import java.lang.ArithmeticException



class MainActivity : AppCompatActivity() {

    private lateinit var calcOutput : TextView
    private lateinit var calcHist : TextView
    private var buildString = ArrayList<String>()
    private var juntaTodo = ArrayList<String>()
    private var lastNumeric = false
    private var lastEqual = false
    private var lastPercent = false
    private var errorState = false
    private var onlyDec = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calcOutput = findViewById(R.id.calcOutput)
        calcHist = findViewById(R.id.calcHist)
    }

    private fun buildTextOutput():String{
        var txtOut = ""
        for (item in buildString){
            txtOut += item
        }
        return txtOut
    }

    private fun Historico():String{
        var txtOut = ""
        for (item in juntaTodo){
            txtOut += item
        }
        return txtOut
    }

    fun onDigit(view: View){
        System.out.println(calcOutput.text);
        if (errorState || lastEqual){
            this.buildString = ArrayList()
            calcOutput.text = (view as Button).text
            errorState = false
            lastEqual = false
        }else{
            calcOutput.append((view as Button).text)
        }
        lastNumeric = true
    }

    fun onOperator(view:View){
        val txt:String?

        if (lastNumeric && !errorState){
            buildString.add(calcOutput.text.toString())
            buildString.add((view as Button).text.toString())
            txt = buildTextOutput()

            this.calcOutput.text = ""
           // calcHist.text = txt
            lastNumeric = false
            onlyDec = false
        }
    }

    fun onEqual(view:View){
        var txt:String
        var teste:String
        if (lastNumeric && !errorState && buildString.isNotEmpty()){
            txt = buildTextOutput()
            txt += calcOutput.text.toString()
            try{
                var sobra =  calcHist.text.toString()
                if(sobra != ""){

                }else{

                }
                teste = eval(txt)
                calcOutput.text = teste
                teste = txt.plus("=").plus(teste).plus(", ")
                juntaTodo.add(teste.toString())
                // calcHist.text = teste
                calcHist.text = juntaTodo.toString()




                onlyDec = true
            }catch (ex: ArithmeticException){
                calcOutput.text = "Error"
                errorState = true
                lastNumeric = false
            }
            lastEqual = true
            this.buildString = ArrayList()
        }
    }

    private fun eval(txt:String):String{

        val expression = Expression(txt)
        expression.setPrecision(12)
        return expression.eval().toString()
    }






    fun doNothing(view:View){
        buildString.add(calcOutput.text.toString())
        this.calcOutput.text = "Error"
    }

    fun pi(view:View){
        var txt:String
        this.calcOutput.text = "3.14"

        buildString.add(calcOutput.text.toString())
        txt = buildTextOutput()

        this.calcOutput.text = ""
        calcHist.text = txt
        lastNumeric = false
        onlyDec = false
    }



    fun fatorial(view:View){
        var txt:String
        if (lastNumeric && !errorState){
            buildString.add(calcOutput.text.toString())
            txt = buildTextOutput()
            if(txt != ""){
                val num = txt.toIntOrNull()
                var i = 1
                var factorial: Long = 1
                while (i <= num!!) {
                    factorial *= i.toLong()
                    i++
                }
                this.calcOutput.text = factorial.toString()
            }else {
                this.calcOutput.text = "Error"
            }
            lastNumeric = false
            onlyDec = false
       }else{
            this.calcOutput.text = "Error"
        }


    }

    private fun stringToDbl(input:TextView):Double{
        return input.text.toString().toDouble()
    }

    fun onClear(view:View){
        this.calcOutput.text = ""
        this.calcHist.text = ""
        this.juntaTodo = ArrayList()
        lastNumeric = false
        errorState = false
        onlyDec = false
        this.buildString = ArrayList()
    }

    fun onSign(view:View){
        if (calcOutput.text.isBlank()){

        }else if (calcOutput.text.isNotBlank()){
            calcOutput.text = (stringToDbl(calcOutput) * -1).toString()
        }

    }

    fun onPercent(view:View){
        if (calcOutput.text.isBlank() && buildString.isEmpty() || calcOutput.text.toString().toDoubleOrNull() == null){
            // don't do anything
        }else if (buildString.isEmpty() && calcOutput.text.isNotBlank()){
            calcOutput.text = (stringToDbl(calcOutput) / 100).toString()
            lastPercent = true
        }else if (buildString.isNotEmpty() && calcOutput.text.isNotBlank()){
            calcOutput.text = evalPercent(calcOutput.text.toString()).toString()
        }
    }

    private fun evalPercent(userInput:String):Double{
        return eval(buildTextOutput() + "0").toDouble() * (userInput.toDouble() / 100)
    }


}