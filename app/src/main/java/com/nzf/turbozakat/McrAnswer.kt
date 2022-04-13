package com.nzf.turbozakat

class McrAnswer(answer: String, nextQuestion: Question?, calculation: (Any?) -> Unit = {}): Answer(answer, nextQuestion, calculation)