# **Spring Data Reactive Repositories with MongoDB**

[Spring Data Reactive Repositories with MongoDB | Baeldung](https://www.baeldung.com/spring-data-mongodb-reactive)

# 개요

이 튜토리얼에서는 MongoDB와 함께 Spring Data Reactive Repositories를 통해 Reactive Programming을 사용하여 데이터베이스 작업을 구성하고 구현하는 방법을 살펴보겠다.

*ReactiveCrudRepository*, *ReactiveMongoRepository* 및 *ReactiveMongoTemplate*의 기본 사용법을 살펴보겠다.

# 환경

Reactive MongoDB를 사용하려면 종속성을 추가해야 헌다. 테스트를 위해 포함된 MongoDB도 추가.

build.gradle.kts

```jsx
implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
```

pom.xml

```xml
<dependencies>
    // ...
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
    </dependency>
    <dependency>
        <groupId>de.flapdoodle.embed</groupId>
        <artifactId>de.flapdoodle.embed.mongo</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Configuration

Reactive를 활성화하려면 일부 인프라 설정과 함께 @*EnableReactiveMongoRepositories*를 사용해야 한다.

```kotlin
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@EnableReactiveMongoRepositories
class MongoReactiveApplication: AbstractReactiveMongoConfiguration() {

    @Bean
    fun mongoClient(): MongoClient {
        return MongoClients.create()
    }
    override fun getDatabaseName(): String {
        return "reactive"
    }

}
```

## Create a Document

Account 클래스를 만들고 @Document로 주석을 달아 데이터베이스 작업에 사용하겠다.

```kotlin
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Account {

    @Id
    private val id: String? = null
    private val owner: String? = null
    private val value: Double? = null
}
```

## ****Using Reactive Repositories****

반응형 모델을 사용하면 결과와 매개변수를 반응형 방식으로 처리한다는 점을 제외하고는  이미 정의된 CRUD 방법과 다른 공통 사항에 대한 지원을 통해 Repository Programming Model과 동일 하다.

### *****ReactiveCrudRepository*****

블럭킹 CrudRepository와 같은 방식으로 이 저장소를 사용할 수 있다.

```kotlin
import me.clevekim.springguide.reactivemongodb.document.Account
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface AccountCrudRepository: ReactiveCrudRepository<Account, String> {

    fun findAllByValue(value: String): Flux<Account>
    fun findFirstByOwner(owner: Mono<String>): Mono<Account>
}
```

findFirstByOwner() 메서드에서 볼 수 있듯이 일반(String), 래핑된(Optional, Stream) 또는 반응형(Mono, Flux)과 같은 다양한 유형의 인수를 전달할 수 있다.

### *****ReactiveMongoRepository*****

*ReactiveCrudRepository*에서 상속하고 몇 가지 새로운 쿼리 메서드를 추가하는 *ReactiveMongoRepository* 인터페이스도 있다.

```kotlin
import me.clevekim.springguide.reactivemongodb.document.Account
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface AccountReactiveRepository: ReactiveMongoRepository<Account, String> {

    fun findAllByValue(value: String): Flux<Account>
    fun findFirstByOwner(owner: Mono<String>): Mono<Account>
}
```

## *****ReactiveMongoTemplate*****

리포지토리 접근 방식 외에도 ReactiveMongoTemplate이 있다. 먼저 ReactiveMongoTemplate을 빈으로 등록해야 한다.

```kotlin
import com.mongodb.reactivestreams.client.MongoClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@Configuration
class ReactiveMongoConfig(
    @Autowired val mongoClient: MongoClient
) {

    @Bean
    fun reactiveMongoTemplate(): ReactiveMongoTemplate {
        return ReactiveMongoTemplate(mongoClient, "test")
    }
}
```

그런 다음 이 빈을 서비스에 주입하여 데이터베이스 작업을 수행할 수 있다.

```kotlin
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
}
```

ReactiveMongoTemplate에는 보유하고 있는 도메인과 관련이 없는 여러 메소드도 있다.

API Doc을 참고
https://docs.spring.io/spring-data/mongodb/docs/current/api/org/springframework/data/mongodb/core/ReactiveMongoTemplate.html
