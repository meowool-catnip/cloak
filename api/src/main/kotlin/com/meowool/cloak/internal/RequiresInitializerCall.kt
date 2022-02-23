package com.meowool.cloak.internal

/**
 * Represents that a function can only be called through an initialization expression.
 *
 * @author 凛 (RinOrz)
 */
@InternalCloakApi
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.SOURCE)
annotation class RequiresInitializerCall