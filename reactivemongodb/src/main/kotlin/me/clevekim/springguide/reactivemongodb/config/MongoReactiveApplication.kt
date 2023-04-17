package me.clevekim.springguide.reactivemongodb.config
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@EnableReactiveMongoRepositories
class MongoReactiveApplication: AbstractReactiveMongoConfiguration() {

    @Value("\${spring.data.mongodb.database}")
    val database: String? = null

    @Bean
    fun mongoClient(): MongoClient {
        return MongoClients.create()
    }
    override fun getDatabaseName(): String {
        return database!!
    }

}