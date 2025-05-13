package com.payby.pos.ecr.utils

object Logger {

    fun error(message: String) {
        val red = "\u001B[31m"
        val reset = "\u001B[0m"
        println("$red$message$reset")
    }

}

