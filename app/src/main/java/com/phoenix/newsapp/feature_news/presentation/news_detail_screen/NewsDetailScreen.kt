package com.phoenix.newsapp.feature_news.presentation.news_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.phoenix.newsapp.feature_news.domain.util.DateUtil
import com.phoenix.newsapp.feature_news.domain.util.Parser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
     onNavigateUp: () -> Unit,
     title: String,
     description: String,
     pubDate: String,
     imageUrl: String?
) {
    Scaffold (
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = "AI generated summary") },
                    navigationIcon = {
                        IconButton(onClick = { onNavigateUp() }){
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back to the news feed")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
                HorizontalDivider(color = Color(0xFF323335))
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
        ){
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = DateUtil().formatPubDate(pubDate),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = imageUrl,
                contentDescription = "News image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = Parser().cleanDescription(description), fontSize = 20.sp)
        }
    }
}

//@Preview
//@Composable
//fun NewsDetailPreview(){
//    NewsDetailScreen("Jets acquire wide receiver Davante Adams from Raiders, AP sources say",
//        "https://dims.apnews.com/dims4/default/61fb49c/2147483647/strip/true/crop/3225x1814+0+168/resize/1440x810!/quality/90/?url=https%3A%2F%2Fassets.apnews.com%2Fec%2Ff2%2F5f8b2297d2e178b647380878a6ad%2F48aa866cc91d447b92f351f784636216",
//        "Las Vegas Raiders wide receiver Davante Adams is tackled by Carolina Panthers cornerback Jaycee Horn during the first half of an NFL football game, Sunday, Sept. 22, 2024, in Las Vegas. (AP Photo/John Locher) The New York Jets have agreed to terms to acquire disgruntled wide receiver Davante Adams from the Las Vegas Raiders, reuniting him with quarterback Aaron Rodgers, two people with knowledge of the deal told The Associated Press. The Jets are sending a conditional third-round pick in next year’s draft that could become a second-rounder, but it is pending a physical, one of the people told the AP on condition of anonymity Tuesday because the teams didn’t announce the deal. NFL Network was the first to report the trade. The 31-year-old Adams immediately boosts a Jets offense that has been inconsistent through the first part of the season. The three-time All-Pro joins Garrett Wilson to give Rodgers two No. 1-caliber wide receivers to throw to, complementing fellow receivers Mike Williams, Allen Lazard and Xavier Gipson, tight end Tyler Conklin and running backs Breece Hall and Braelon Allen. Adams, who missed the Raiders’ last three games with a hamstring injury, told the team he wanted out of Las Vegas — and the team was willing to accommodate his request. And now he’s back with Rodgers, the quarterback with whom he enjoyed eight seasons of success catching passes from in Green Bay.",
//        "Tue, 15 Oct 2024 22:53:01 +0800")
//}
