package me.clevekim.springguide.reactivemongodb.repository

import me.clevekim.springguide.reactivemongodb.document.Account
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface AccountCrudRepository: ReactiveCrudRepository<Account, String> {

    fun findAllByValue(value: Double): Flux<Account>
    fun findFirstByOwner(owner: Mono<String>): Mono<Account>
}