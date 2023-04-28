package me.clevekim.springguide.gsintegration

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ImportResource

@SpringBootApplication
@ImportResource("/integration/integration.xml")
class GsIntegrationApplication

fun main(args: Array<String>) {
	val ctx = SpringApplication(GsIntegrationApplication::class.java).run(*args)
	println("Hit Enter to terminate")
	System.`in`.read()
	ctx.close()
}
