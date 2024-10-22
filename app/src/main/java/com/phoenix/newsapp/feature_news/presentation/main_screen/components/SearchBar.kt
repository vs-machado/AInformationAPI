package com.phoenix.newsapp.feature_news.presentation.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(
    onSearchTextChanged: (String) -> Unit,
    horizontalPadding: Dp,
    verticalPadding: Dp,
    backgroundColor: Color,
    cursorBrushColor: SolidColor,
    handleColor: Color
){
    var searchText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontalPadding, verticalPadding)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(1.dp, Color(0xFF121B22), RoundedCornerShape(24.dp))
            .height(48.dp)
    ) {
        val customTextSelectionColors = TextSelectionColors(
            handleColor = handleColor,
            backgroundColor = backgroundColor
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            BasicTextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    onSearchTextChanged(newText)
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                ),
                cursorBrush = cursorBrushColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 44.dp, end = 8.dp)
                    .align(Alignment.CenterStart)// Adjust to avoid overlapping with icon
            )
        }

        if(searchText.isEmpty()){
            Text(
                text = "Search...",
                color = Color(0xFF70777B), // Hint color
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 44.dp, top = 14.dp)
                    .height(48.dp) // Ensure hint text aligns with the field height
            )
        }
        if (searchText.isNotEmpty()) {
            IconButton(
                onClick = {
                    searchText = ""
                    onSearchTextChanged("")
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = Color(0xFF70777B)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color(0xFF70777B),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(22.dp) // Adjust size if needed
        )
    }
}