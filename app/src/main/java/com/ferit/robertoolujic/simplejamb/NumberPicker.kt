package com.ferit.robertoolujic.simplejamb

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max


@Composable
fun NumberPicker(
    modifier : Modifier,
    onValueChange: (Int) -> Unit
) {
    val items = (1..6).toList()
    val startPage = 300_000
    val pagerState = rememberPagerState(
        initialPage = startPage,
        pageCount = { 600_000 }
    )
    val selectedNumber by remember {
        derivedStateOf {
            items[pagerState.currentPage % items.size]
        }
    }
    val configuration = LocalConfiguration.current
    LaunchedEffect(selectedNumber) {
        onValueChange(selectedNumber)
    }
    BoxWithConstraints(modifier){
        when(configuration.orientation){
            Configuration.ORIENTATION_LANDSCAPE -> {
                PickerPager(items, pagerState, maxWidth/1.5f)
            }
            else -> {
                PickerPager(items,pagerState,maxHeight/1.5f)
            }
        }
    }
}

@Composable
fun PickerPager(
    items: List<Int>,
    pagerState: PagerState,
    maxSize: Dp
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxHeight()
                    .width(maxSize),
                pageSize = PageSize.Fixed(maxSize / 2),
                contentPadding = PaddingValues(horizontal = maxSize / 4),
                beyondViewportPageCount = 1
            ) { page ->
                val num = items[page % 6]
                Image(
                    painterResource(id = diceDrawables[num]),
                    contentDescription = "$num",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(maxSize/2)
                        .border(
                            width = 4.dp,
                            color = if (items[pagerState.currentPage % 6] == num)
                                MaterialTheme.colorScheme.primary
                            else Color.Transparent,
                            shape = RoundedCornerShape(5.dp)
                        )
                )
            }
        }
        else ->{
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
                    .height(maxSize),
                pageSize = PageSize.Fixed(maxSize / 2),
                contentPadding = PaddingValues(vertical = maxSize / 4),
                beyondViewportPageCount = 1
            ) { page ->
                val num = items[page % 6]
                Image(
                    painterResource(id = diceDrawables[num]),
                    contentDescription = "$num",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(maxSize/2)
                        .border(
                            width = 4.dp,
                            color = if (items[pagerState.currentPage % 6] == num)
                                MaterialTheme.colorScheme.primary
                            else Color.Transparent,
                            shape = RoundedCornerShape(5.dp)
                        )
                )
            }
        }
        }

}