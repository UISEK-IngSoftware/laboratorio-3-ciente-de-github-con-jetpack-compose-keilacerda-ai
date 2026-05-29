package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.ui.components.RepoItem
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme
import ec.edu.uisek.githubclient.viewmodels.RepoListViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ec.edu.uisek.githubclient.models.Repository


@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    viewModel: RepoListViewModel = viewModel(),
    onNavigateToForm: () -> Unit = {},
    onEditRepo: (Repository) -> Unit = {}
) {
    val repos by viewModel.repos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()
    var repoToDelete by remember { mutableStateOf<Repository?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToForm,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMsg?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
        if (!isLoading && errorMsg == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(repos.size) { index ->
                    RepoItem(repos[index],
                        onEdit = { onEditRepo(it)},
                        onDelete = { repoToDelete = it})
                }
            }
        }

        repoToDelete?.let { repos ->
            AlertDialog(
                {
                    repoToDelete = null
                },
                title = {
                    Text("¿Seguro que deseas eliminar ${repos.name}?")
                },
                text = {
                    Text("Esta acción no se puede deshacer")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteRepo(
                                repos.owner.name,
                                repos.name
                            )
                            repoToDelete = null
                        }
                    ) { Text("Eliminar") }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            repoToDelete = null
                        }
                    ) { Text("Cancelar") }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RepoListPreview(){
    GithubClientTheme {
        RepoList()
    }
}