package ec.edu.uisek.githubclient

// Objeto separado para contener la configuración del token GitHub.
// Se evita el nombre `BuildConfig` porque Gradle genera una clase
// con ese mismo nombre y provocaría redeclaración.
object GitHubConfig {
    const val GITHUB_TOKEN: String = ""
}
