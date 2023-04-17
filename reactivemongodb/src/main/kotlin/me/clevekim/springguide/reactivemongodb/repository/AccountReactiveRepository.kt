package me.clevekim.springguide.reactivemongodb.repository

import me.clevekim.springguide.reactivemongodb.document.Account
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface AccountReactiveRepository: ReactiveMongoRepository<Account, String> {

    fun findAllByValue(value: Double): Flux<Account>
    fun findFirstByOwner(owner: Mono<String>): Mono<Account>
}