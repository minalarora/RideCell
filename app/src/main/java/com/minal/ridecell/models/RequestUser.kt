package com.minal.ridecell.models

import java.lang.Exception
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

data class RequestUser(val email: String? = null, val password: String? = null,
                       val display_name: String? = null, val created_at: String? = null){

}