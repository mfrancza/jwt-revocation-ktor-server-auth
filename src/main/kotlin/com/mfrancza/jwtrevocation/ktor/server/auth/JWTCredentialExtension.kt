package com.mfrancza.jwtrevocation.ktor.server.auth

import com.auth0.jwt.interfaces.Payload
import com.mfrancza.jwtrevocation.rules.ClaimsSource
import com.mfrancza.jwtrevocation.rules.RuleSet
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal

fun JWTCredential.toPrincipal() : JWTPrincipal {
    return JWTPrincipal(this.payload)
}

fun RuleSet.isMet(credential: JWTCredential) : Boolean {
    return this.isMet(JWTCredentialClaimSource(credential))
}

fun JWTCredential.notRevoked(ruleSet: RuleSet) : JWTCredential? {
    return if (!ruleSet.isMet(this)) {
        this
    } else {
        null
    }
}

/**
 * Adapter to provide a ClaimsSource implementation for JWTCredential
 */
private class JWTCredentialClaimSource(val credential: JWTCredential) : ClaimsSource {
    override fun audValue(): String? = credential.audience.firstOrNull()

    override fun expValue(): Long? = credential.expiresAt?.toInstant()?.epochSecond

    override fun iatValue(): Long? = credential.issuedAt?.toInstant()?.epochSecond

    override fun issValue(): String? = credential.issuer

    override fun jtiValue(): String? = credential.jwtId

    override fun nbfValue(): Long? = credential.notBefore?.toInstant()?.epochSecond

    override fun subValue(): String? = credential.subject

}