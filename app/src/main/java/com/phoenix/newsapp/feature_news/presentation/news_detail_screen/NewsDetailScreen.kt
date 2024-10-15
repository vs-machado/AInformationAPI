package com.phoenix.newsapp.feature_news.presentation.news_detail_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.phoenix.newsapp.feature_news.domain.model.RssItem
import com.phoenix.newsapp.feature_news.domain.util.DateUtil
import com.phoenix.newsapp.feature_news.domain.util.Parser

@Composable
fun NewsDetailScreen(
     title: String,
     description: String,
     pubDate: String,
     imageUrl: String?
) {
    Scaffold { innerPadding ->
        Log.d("debug", Parser().cleanDescription(description))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ){
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = Parser().cleanDescription(description), fontSize = 20.sp)
        }
    }
}

@Preview
@Composable
fun NewsDetailPreview(){
    NewsDetailScreen("Jets acquire wide receiver Davante Adams from Raiders, AP sources say",
        "https://dims.apnews.com/dims4/default/61fb49c/2147483647/strip/true/crop/3225x1814+0+168/resize/1440x810!/quality/90/?url=https%3A%2F%2Fassets.apnews.com%2Fec%2Ff2%2F5f8b2297d2e178b647380878a6ad%2F48aa866cc91d447b92f351f784636216",
        "Las Vegas Raiders wide receiver Davante Adams is tackled by Carolina Panthers cornerback Jaycee Horn during the first half of an NFL football game, Sunday, Sept. 22, 2024, in Las Vegas. (AP Photo/John Locher) The New York Jets have agreed to terms to acquire disgruntled wide receiver Davante Adams from the Las Vegas Raiders, reuniting him with quarterback Aaron Rodgers, two people with knowledge of the deal told The Associated Press. The Jets are sending a conditional third-round pick in next year’s draft that could become a second-rounder, but it is pending a physical, one of the people told the AP on condition of anonymity Tuesday because the teams didn’t announce the deal. NFL Network was the first to report the trade. The 31-year-old Adams immediately boosts a Jets offense that has been inconsistent through the first part of the season. The three-time All-Pro joins Garrett Wilson to give Rodgers two No. 1-caliber wide receivers to throw to, complementing fellow receivers Mike Williams, Allen Lazard and Xavier Gipson, tight end Tyler Conklin and running backs Breece Hall and Braelon Allen. Adams, who missed the Raiders’ last three games with a hamstring injury, told the team he wanted out of Las Vegas — and the team was willing to accommodate his request. And now he’s back with Rodgers, the quarterback with whom he enjoyed eight seasons of success catching passes from in Green Bay.",
        "Tue, 15 Oct 2024 22:53:01 +0800")
}
