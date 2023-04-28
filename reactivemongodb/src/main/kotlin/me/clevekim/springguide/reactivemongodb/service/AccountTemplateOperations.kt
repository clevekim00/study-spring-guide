package me.clevekim.springguide.reactivemongodb.service

import com.mongodb.client.result.DeleteResult
import me.clevekim.springguide.reactivemongodb.document.Account
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class AccountTemplateOperations(
    @Autowired val template: ReactiveMongoTemplate
) {
    fun findById(id: String?): Mono<Account?>? {
        return template.findById(id!!, Account::class.java)
    }

    fun findAll(): Flux<Account?>? {
        return template.findAll(Account::class.java)
    }

    fun save(account: Mono<Account>?): Mono<Account>? {
        return template.save(account!!)
    }

    fun delete(account: Account) : Long {
        val result: DeleteResult? = template.remove(account).block()
        return result!!.deletedCount
    }
}