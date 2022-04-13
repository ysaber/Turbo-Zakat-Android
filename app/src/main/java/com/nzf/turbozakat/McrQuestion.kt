package com.nzf.turbozakat

class McrQuestion(question: String, progress: Int, val answers: ArrayList<Answer>?, blurb: String = "", isFinalQuestion: Boolean = false, calculation: () -> Double = { 0.0 })
    : Question(question, progress, isFinalQuestion, blurb, calculation)