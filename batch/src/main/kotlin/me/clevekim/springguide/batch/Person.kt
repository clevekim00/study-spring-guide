package me.clevekim.springguide.batch

data class Person(
        var firstName: String,
        var lastName: String
    ) {
    constructor() : this("", "")
}