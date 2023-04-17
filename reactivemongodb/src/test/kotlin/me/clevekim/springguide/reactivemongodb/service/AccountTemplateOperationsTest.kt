package me.clevekim.springguide.reactivemongodb.service

import me.clevekim.springguide.reactivemongodb.document.Account
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.lang.Double
import kotlin.io.path.createTempDirectory

@Suppress("NAME_SHADOWING")
@SpringBootTest
class AccountTemplateOperationsTest(
    @Autowired val templateOperations: AccountTemplateOperations
) {

    @Test
    fun test() {
        val account = Account()
        account.owner = "Bill1"
        account.value = 12.31
        val saved = templateOperations.save(Mono.just(account))?.block()
        Assertions.assertNotNull(saved)

        val all:Flux<Account?>? = templateOperations.findAll()
        StepVerifier
            .create(all!!)
            .assertNext{ a ->
                Assertions.assertEquals(account.owner, a?.owner ?: "")
                Assertions.assertEquals(account.value, a?.value ?: 12)
                Assertions.assertNotNull(a?.id ?: "")
            }
            .expectComplete()
            .verify()

        val deletedCnt = templateOperations.delete(saved!!)
        Assertions.assertEquals(1L, deletedCnt)
    }

    @Test
    fun testFlux() {
        val account = Account()
        account.owner = "Bill1"
        account.value = 12.31

        val flux:Flux<Account> = Flux.just(account)
        StepVerifier.create(flux)
            .assertNext{x ->
                Assertions.assertEquals(account.owner, x.owner)

            }
            .expectComplete()
            .verify()
    }
}