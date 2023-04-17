package me.clevekim.springguide.reactivemongodb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactivemongodbApplication

fun main(args: Array<String>) {
    runApplication<ReactivemongodbApplication>(*args)
}
