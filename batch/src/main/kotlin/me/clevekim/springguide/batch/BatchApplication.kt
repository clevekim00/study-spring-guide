package me.clevekim.springguide.batch

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import kotlin.system.exitProcess


@SpringBootApplication
class BatchApplication

fun main(args: Array<String>) {
	exitProcess(SpringApplication.exit(SpringApplication.run(BatchApplication::class.java, *args)))
}