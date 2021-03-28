package ru.roggi.lab2.model

class MethodCannotBeAppliedException: Exception {
    constructor(message: String?) : super(message)
    constructor() : super()
}