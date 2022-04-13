package com.nzf.turbozakat

import java.io.Serializable

open class Question(val question: String,
                    val progress: Int,
                    val isFinalQuestion: Boolean = false,
                    val blurb: String = "",
                    val finalCalculation: () -> Double) : Serializable