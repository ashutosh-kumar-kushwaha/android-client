package com.mifos.mifosxdroid.online.search

import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mifos.mifosxdroid.R
import com.mifos.objects.SearchedEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(searchUiState: SearchUiState, onSearchClick: (String, Boolean) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.search),
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Row {
                            Text(
                                text = "All",
                                fontSize = 16.sp
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it)
        ) {
            var searchText by remember { mutableStateOf("") }

            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.search_hint),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            var exactMatchChecked by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    onSearchClick(searchText, exactMatchChecked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.search),
                    fontSize = 16.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = exactMatchChecked,
                    onCheckedChange = {
                        exactMatchChecked = it
                    }
                )
                Text(
                    text = stringResource(id = R.string.exact_match),
                    fontSize = 16.sp
                )
            }
            LazyColumn {
                if (searchUiState is SearchUiState.ShowSearchedResources) {
                    items(searchUiState.searchedEntities.size) { position ->
                        ClientItem(searchUiState.searchedEntities[position])
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchScreenPreview() {
//    SearchScreen()
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED)
@Composable
fun SearchScreenPreviewNight() {
//    SearchScreen()
}

@Composable
fun ClientItem(searchedEntity: SearchedEntity) {
    val color = ColorGenerator.MATERIAL.getColor(searchedEntity.entityType)
    val drawable =
        TextDrawable.builder().round().build(searchedEntity.entityType ?: "", color) as Drawable
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp),
            contentDescription = null,
            bitmap = drawable.toBitmap().asImageBitmap(),
        )
        Text(
            text = searchedEntity.entityName ?: "",
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun Preview() {
//    ClientItem(searchUiState.searchedEntities[it])
}