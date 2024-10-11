package com.phoenix.newsapp.feature_news.presentation.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.feature_news.domain.model.RssItem
import com.phoenix.newsapp.feature_news.presentation.main_screen.components.FeedItem

/**
 * The main screen shows a search bar to allow user to query specific news.
 * A news feed is automatically shown if the fetch was successful,
 * if there is an error during the fetch, it displays an error and
 * show a "Try again" button.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedState.collectAsState()
    var searchText by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.fetchRssFeed()
    }

    Scaffold(
        topBar = {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchText,
                        onQueryChange = {
                            searchText = it
                        },
                        onSearch = { 
                            expanded = false 
                            //Query news by text
                       },
                        expanded = false,
                        onExpandedChange = { expanded = false},
                        placeholder = { Text("Search news") },
                        leadingIcon = {
                            IconButton(onClick = { /* Query news by text*/ }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            }
                        }
                    )
                },
                expanded = false,
                onExpandedChange = { expanded = false },
                content = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when(val state = feedState) {
                is FeedState.Initial -> {}
                is FeedState.Loading -> LoadingIndicator()
                is FeedState.Success -> {
                    FeedContent(feed = state.items)
                }
                is FeedState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Connection error occurred.",
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = { viewModel.fetchRssFeed() }
                        ) {
                            Text("Try Again")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun FeedContent(feed: List<RssItem>) {
    LazyColumn {
        items(feed) { item ->
            FeedItem(item)
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}