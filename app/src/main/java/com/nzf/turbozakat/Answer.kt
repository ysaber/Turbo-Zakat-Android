package com.nzf.turbozakat

import java.io.Serializable

open class Answer(val answer: String?, val nextQuestion: Question?, val calculation: (Any?) -> Unit = {}) : Serializable