package com.phoenix.newsapp.feature_news.presentation.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenix.newsapp.feature_news.domain.model.RssItem
import com.phoenix.newsapp.feature_news.presentation.main_screen.components.FeedItem

/**k
 * The main screen shows a search bar to allow user to query specific news.
 * A news feed is automatically shown if the fetch was successful,
 * if there is an error during the fetch, it displays an error and
 * show a "Try again" button.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    onNavigationToNewsDetail: (RssItem) -> Unit
) {
    val feedState by viewModel.filteredFeed.collectAsState()
    var searchText by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchText,
                        onQueryChange = {
                            searchText = it
                            viewModel.updateSearchQuery(it)
                        },
                        onSearch = { 
                            expanded = false 
                            viewModel.updateSearchQuery(searchText)
                       },
                        expanded = false,
                        onExpandedChange = { expanded = false},
                        placeholder = { Text("Search news") },
                        leadingIcon = {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            }
                        },
                        trailingIcon = {
                            if(searchText.isNotEmpty()){
                                IconButton(onClick = {
                                    searchText = ""
                                    viewModel.updateSearchQuery("")
                                }){
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search"
                                    )
                                }
                            }
                        }
                    )
                },
                expanded = false,
                onExpandedChange = { expanded = false },
                content = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                    FeedContent(
                        feed = state.items,
                        onNavigationToNewsDetail = onNavigationToNewsDetail
                    )
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
                            onClick = { viewModel.fetchRssFeed(isScrolling = false) }
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
fun FeedContent(
    feed: List<RssItem>,
    viewModel: MainScreenViewModel = hiltViewModel(),
    onNavigationToNewsDetail: (RssItem) -> Unit
) {
    val lazyListState = rememberLazyListState()

    // When user reaches the last lazycolumn item the app fetches more news to the feed.
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo }
            .collect { layoutInfo ->
                if (lazyListState.isScrollInProgress) {
                    if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
                        val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
                        val lastIndex = lazyListState.layoutInfo.totalItemsCount - 1

                        if (lastVisibleItem.index == lastIndex) {
                            viewModel.fetchRssFeed(isScrolling = true)
                        }
                    }


                }
            }
    }

    LazyColumn(state = lazyListState) {
        items(feed) { item ->
            FeedItem(
                item = item,
                onItemClick = { onNavigationToNewsDetail(item) }
            )
        }
    }
}

// Displays a loading indicator when fetching the news
@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}