package com.everyonewaiter.presentation.owner.api

import com.everyonewaiter.application.store.dto.Apply
import com.everyonewaiter.application.store.dto.Registration
import com.everyonewaiter.application.store.service.StoreRegistrationService
import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.global.annotation.AuthenticationAccount
import com.everyonewaiter.presentation.owner.spec.StoreRegistrationControllerSpecification
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/v1/stores/registrations")
class StoreRegistrationRestController(
    private val registrationService: StoreRegistrationService,
) : StoreRegistrationControllerSpecification {
    @GetMapping("/{registrationId}")
    override fun getRegistration(
        @PathVariable registrationId: Long,
        @AuthenticationAccount account: Account,
    ): ResponseEntity<Registration.Response> {
        val response = registrationService.getRegistration(registrationId, account.id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    override fun getRegistrations(
        @RequestParam(value = "page", defaultValue = "1") page: Long,
        @RequestParam(value = "size", defaultValue = "20") size: Long,
        @AuthenticationAccount account: Account,
    ): ResponseEntity<Registration.PageResponse> {
        val request = Registration.PageRequest(page, size)
        val responses = registrationService.getRegistrations(account.id, request)
        return ResponseEntity.ok(responses)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun apply(
        @ModelAttribute @Valid request: Apply.Request,
        @AuthenticationAccount account: Account,
    ): ResponseEntity<Unit> {
        val registrationId = registrationService.apply(account.id, request)
        return ResponseEntity.created(URI.create(registrationId.toString())).build()
    }
}
