package com.seemebetter.admin.domain.model

data class GlobalSettings(
  val handle: String,
  val profileName: String,
  val publicMessage: String,
  val cooldownHours: Long
)
