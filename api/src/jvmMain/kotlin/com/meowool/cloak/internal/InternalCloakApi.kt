package com.meowool.cloak.internal


/**
 * Used to mark APIs that are not available externally.
 *
 * @author 凛 (RinOrz)
 */
@RequiresOptIn(
  level = RequiresOptIn.Level.ERROR,
  message = "This is an internal 'com.meowool.cloak' API that should not be used from outside."
)
annotation class InternalCloakApi
