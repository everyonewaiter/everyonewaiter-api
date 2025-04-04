package com.everyonewaiter.presentation.owner.api

import com.everyonewaiter.application.store.dto.Apply
import com.everyonewaiter.application.store.service.StoreRegistrationService
import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.global.annotation.AuthenticationAccount
import com.everyonewaiter.presentation.owner.spec.StoreRegistrationControllerSpecification
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/v1/stores/registrations")
class StoreRegistrationRestController(
    private val registrationService: StoreRegistrationService,
) : StoreRegistrationControllerSpecification {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun apply(
        @ModelAttribute @Valid request: Apply.Request,
        @AuthenticationAccount account: Account,
    ): ResponseEntity<Unit> {
        val registrationId = registrationService.apply(account.id, request)
        return ResponseEntity.created(URI.create(registrationId.toString())).build()
    }
}
