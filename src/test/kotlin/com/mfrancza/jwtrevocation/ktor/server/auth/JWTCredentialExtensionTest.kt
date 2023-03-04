package com.mfrancza.jwtrevocation.ktor.server.auth

import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.Payload
import com.mfrancza.jwtrevocation.rules.Rule
import com.mfrancza.jwtrevocation.rules.RuleSet
import com.mfrancza.jwtrevocation.rules.conditions.StringEquals
import io.ktor.http.Parameters
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.response.ApplicationResponse
import io.ktor.util.Attributes
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class JWTCredentialExtensionTest {

    private val referenceEpochSeconds : Long = 1673123605
    @Test
    fun credentialsThatAreRevokedReturnNull() {
        val jwtCredential = JWTCredential(object : Payload {
            override fun getIssuer() = "bad-iss.mfrancza.com"

            override fun getSubject() = "mfrancza"

            override fun getAudience() = listOf("aud.mfrancza.com")

            override fun getExpiresAt() = Date()

            override fun getNotBefore() = Date()

            override fun getIssuedAt() = Date()

            override fun getId() = "mfrancza-id"

            override fun getClaim(name: String?): Claim {
                TODO("Not used")
            }

            override fun getClaims(): MutableMap<String, Claim> {
                TODO("Not used")
            }
        })


        val ruleSet = RuleSet(
            rules = listOf(
                Rule(
                    ruleExpires = referenceEpochSeconds + 60,
                    iss = listOf(StringEquals("bad-iss.mfrancza.com"))
                )
            ),
            timestamp = referenceEpochSeconds
        )

        assertNull(jwtCredential.notRevoked(ruleSet), "Credentials that are revoked return null")
    }

    @Test
    fun credentialsThatAreNotRevokedAreReturned() {
        val jwtCredential = JWTCredential(object : Payload {
            override fun getIssuer() = "good-iss.mfrancza.com"

            override fun getSubject() = "mfrancza"

            override fun getAudience() = listOf("aud.mfrancza.com")

            override fun getExpiresAt() = Date()

            override fun getNotBefore() = Date()

            override fun getIssuedAt() = Date()

            override fun getId() = "mfrancza-id"

            override fun getClaim(name: String?): Claim {
                TODO("Not used")
            }

            override fun getClaims(): MutableMap<String, Claim> {
                TODO("Not used")
            }
        })


        val ruleSet = RuleSet(
            rules = listOf(
                Rule(
                    ruleExpires = referenceEpochSeconds + 60,
                    iss = listOf(StringEquals("bad-iss.mfrancza.com"))
                )
            ),
            timestamp = referenceEpochSeconds
        )

        assertEquals(jwtCredential, jwtCredential.notRevoked(ruleSet), "Credentials that do not meet the revocation rules are ")
    }

    @Test
    fun toPrincipalReturnsMatchingPrincipal() {
        val jwtCredential = JWTCredential(object : Payload {
            override fun getIssuer() = "bad-iss.mfrancza.com"

            override fun getSubject() = "mfrancza"

            override fun getAudience() = listOf("aud.mfrancza.com")

            override fun getExpiresAt() = Date()

            override fun getNotBefore() = Date()

            override fun getIssuedAt() = Date()

            override fun getId() = "mfrancza-id"

            override fun getClaim(name: String?): Claim {
                TODO("Not used")
            }

            override fun getClaims(): MutableMap<String, Claim> {
                TODO("Not used")
            }
        })

        assertEquals(jwtCredential.payload, jwtCredential.toPrincipal().payload, "The created principal should have the same payload")
    }
}