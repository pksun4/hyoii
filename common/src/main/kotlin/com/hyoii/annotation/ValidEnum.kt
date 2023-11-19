package com.hyoii.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ValidEnumValidator::class])
annotation class ValidEnum (

    val message: String = "invalid enum value",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val enumClass: KClass<out Enum<*>>

)

class ValidEnumValidator: ConstraintValidator<ValidEnum, Any> {
    private lateinit var enumValues: Array<out Enum<*>>

    override fun initialize(annotion: ValidEnum) {
        enumValues = annotion.enumClass.java.enumConstants
    }

    // 체크하는 부분
    // value : 사용자로부터 받은 값
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true
        }

        // any : 뒤에 조건이 하나라도 만족하면 TRUE
        return enumValues.any { it.name == value.toString() }
    }
}
