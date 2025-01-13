package com.payby.pos.ecr.utils

val String.isValid: Boolean
    get() = this.isNotEmpty() && this.isNotBlank()

val String.isEmpty: Boolean
    get() = this.isEmpty() || this.isBlank()