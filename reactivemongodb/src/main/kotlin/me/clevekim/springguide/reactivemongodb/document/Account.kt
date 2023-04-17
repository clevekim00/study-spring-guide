package me.clevekim.springguide.reactivemongodb.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Account(
    var owner: String? = null,
    var value: Double? = null
) {

    @Id
    var id: String? = null
}