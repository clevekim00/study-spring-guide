package me.clevekim.springguide.batchprocessing

data class Person(
        var firstName: String,
        var lastName: String
    ) {
    constructor() : this("", "")
}