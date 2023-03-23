package me.clevekim.springguide.batch

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class PersonMapper : BeanWrapperFieldSetMapper<Person>() {
    init {
        setTargetType(Person::class.java)
    }

    override fun mapFieldSet(fs: FieldSet): Person {
        return super.mapFieldSet(fs)
    }
}