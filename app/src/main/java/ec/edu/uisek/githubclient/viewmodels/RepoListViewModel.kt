package ec.edu.uisek.githubclient.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoListViewModel: ViewModel () {
    private val _repos = MutableStateFlow<List<Repository>>(emptyList())
    val repos: StateFlow<List<Repository>> = _repos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    fun fetchRepos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                _repos.value = RetrofitClient.apiService.getRepositories()
            } catch (e: Exception) {
                _errorMsg.value = "Error al cargar repositorios: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}