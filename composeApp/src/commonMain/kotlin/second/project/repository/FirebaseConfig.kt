package second.project.repository

object FirebaseConfig {
    const val databaseUrl = "https://projeto-second-default-rtdb.firebaseio.com"
    private const val placeholderPrefix = "COLE_AQUI"

    // Copie a Web API Key em Firebase Console > Configuracoes do projeto > Geral.
    // Essa chave identifica o projeto; a seguranca real fica nas regras do Firebase.
    const val webApiKey = "AIzaSyDxlLff63b5uy4Bg6vfbIeo3dDr-Gdb0Js"
    const val storageBucket = "projeto-second.appspot.com"

    fun isAuthConfigured(): Boolean {
        return webApiKey.isNotBlank() && !webApiKey.startsWith(placeholderPrefix)
    }

    fun isStorageConfigured(): Boolean {
        return storageBucket.isNotBlank() && !storageBucket.startsWith(placeholderPrefix)
    }
}
