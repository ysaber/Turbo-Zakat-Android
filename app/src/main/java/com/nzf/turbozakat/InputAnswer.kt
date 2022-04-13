package com.nzf.turbozakat

class InputAnswer(nextQuestion: Question?,
                  calculation: (Any?) -> Unit)
    : Answer("", nextQuestion, calculation)