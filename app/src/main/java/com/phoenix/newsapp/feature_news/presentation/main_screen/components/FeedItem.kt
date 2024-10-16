package com.phoenix.newsapp.feature_news.presentation.main_screen.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.phoenix.newsapp.R
import com.phoenix.newsapp.feature_news.domain.model.RssItem
import com.phoenix.newsapp.feature_news.domain.util.DateUtil
import com.phoenix.newsapp.feature_news.presentation.main_screen.AISummaryState
import com.phoenix.newsapp.feature_news.presentation.main_screen.MainScreenViewModel
import java.util.Locale

@Composable
fun FeedItem(
    item: RssItem,
    onItemClick: () -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel(),
    onNavigateToAISummaryScreen: (String) -> Unit
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val aiSummaryState = viewModel.aiSummaryState.collectAsStateWithLifecycle(initialValue = AISummaryState.Initial)
    val currentSummarizedItemId = viewModel.currentSummarizedItemId.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier.clickable { onItemClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "News image",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = item.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth(),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = DateUtil().getTimeAgo(item.pubDate),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 18.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(
                    onClick = { openAlertDialog.value = true },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_gemini),
                        contentDescription = "Generate AI summary",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate AI summary")
                }
            }
        }
        HorizontalDivider(color = Color(0xFF2C2C2C).copy(alpha = 0.7f))
    }

    when {
        openAlertDialog.value -> {
            AISummaryDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    viewModel.generateAISummary(item.description, Locale.getDefault().language.toString(), item.id)
                },
                dialogTitle = "Generate AI summary",
                dialogText = "When you click the confirm button, AI will be used to create a summary of the news item." +
                        " Always read the full story before spreading information.",
                currentItemId = item.id,
                currentSummarizedItemId = currentSummarizedItemId.value!!,
                icon = ImageVector.vectorResource(id = R.drawable.ic_gemini),
                aiSummaryState = aiSummaryState.value,
                onAISummaryGenerated = { summary ->
                    openAlertDialog.value = false
                    onNavigateToAISummaryScreen(summary)
                    viewModel.resetAISummaryState()
                }
            )
        }
    }
}

@Composable
fun AISummaryDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    currentItemId: String,
    currentSummarizedItemId: String,
    icon: ImageVector,
    aiSummaryState: AISummaryState,
    onAISummaryGenerated: (String) -> Unit
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            when {
                aiSummaryState is AISummaryState.Initial -> Text(text = dialogText)
                aiSummaryState is AISummaryState.Loading -> {
                    Column {
                        Text(text = dialogText)
                        Spacer(modifier = Modifier.height(8.dp))
                        CircularProgressIndicator()
                    }
                }
                aiSummaryState is AISummaryState.Success && currentItemId == currentSummarizedItemId -> {
                    onAISummaryGenerated(aiSummaryState.summary)
                    Text("Summary generated successfully!")
                }
                aiSummaryState is AISummaryState.Error -> "Error: ${aiSummaryState.message}"}
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

//@Preview
//@Composable
//fun PreviewFeedItem() {
//    FeedItem(
//        RssItem("Title", "http://google.com/", "Hello world!", "10/10/2024")
//    )
//}