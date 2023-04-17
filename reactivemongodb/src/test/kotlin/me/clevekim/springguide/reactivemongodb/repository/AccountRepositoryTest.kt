package me.clevekim.springguide.reactivemongodb.repository

import me.clevekim.springguide.reactivemongodb.document.Account
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

//@SpringBootTest
class AccountRepositoryTest(
    @Autowired val repository: AccountCrudRepository
) {

    @Test
    fun givenValue_whenFindAllByValue_thenFindAccount() {
        val account:Account = Account()
        account.owner = "Bill"
        account.value = 12.3
        repository.save(account).block()
        val accountFlux: Flux<Account> = repository.findAllByValue(12.3)
        StepVerifier
            .create(accountFlux)
            .assertNext { account ->
                assertEquals("Bill", account.owner)
                assertEquals(java.lang.Double.valueOf(12.3), account.value)
                assertNotNull(account.id)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun givenOwner_whenFindFirstByOwner_thenFindAccount() {

        val account:Account = Account()
        account.owner = "Bill"
        account.value = 12.3
        repository.save(account).block()
        val accountMono: Mono<Account> = repository
            .findFirstByOwner(Mono.just("Bill"))
        StepVerifier
            .create(accountMono)
            .assertNext { account ->
                assertEquals("Bill", account.owner)
                assertEquals(java.lang.Double.valueOf(12.3), account.value)
                assertNotNull(account.id)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun givenAccount_whenSave_thenSaveAccount() {
        val account:Account = Account()
        account.owner = "Bill"
        account.value = 12.3
        val accountMono: Mono<Account> = repository.save(account)
        StepVerifier
            .create(accountMono)
            .assertNext { account -> assertNotNull(account.id) }
            .expectComplete()
            .verify()
    }
}