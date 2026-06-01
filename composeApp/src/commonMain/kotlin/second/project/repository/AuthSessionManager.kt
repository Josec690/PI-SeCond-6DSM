package second.project.repository

import second.project.model.AuthSession

object AuthSessionManager {
    var currentSession: AuthSession? = null
        private set

    fun setSession(session: AuthSession) {
        currentSession = session
    }

    fun clear() {
        currentSession = null
    }

    fun requireIdToken(): String {
        return currentSession?.idToken
            ?: throw IllegalStateException("Sessao expirada. Faca login novamente.")
    }
}
