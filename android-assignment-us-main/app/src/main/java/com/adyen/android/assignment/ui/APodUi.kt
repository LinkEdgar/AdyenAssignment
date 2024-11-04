package com.adyen.android.assignment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.adyen.android.assignment.R
import com.adyen.android.assignment.ui.planets.PODImageModel
import com.adyen.android.assignment.viewmodels.FilterType
import com.adyen.android.assignment.viewmodels.PODViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Composable
fun PODListScreen(
    podViewModel: PODViewModel,
    onPODClicked: (id: String) -> Unit,
    onSortClicked: () -> Unit
) {

    val uiState = podViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        podViewModel.loadPlanets()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (uiState.value.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
            )
        } else if (uiState.value.errorMessage != null) {
            Text("Error --> ${uiState.value.errorMessage}")

        } else {
            PODListSuccessScreen(
                pods = uiState.value.pods,
                onPODClicked = onPODClicked,
                onSortClicked = { onSortClicked() })
        }
    }
}

@Composable
fun PODListSuccessScreen(
    pods: List<PODImageModel>,
    onPODClicked: (id: String) -> Unit,
    onSortClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Spacer(Modifier.height(32.dp))

            Text(
                "Our Universe",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )

            Spacer(Modifier.height(32.dp))

            LazyColumn {
                items(pods) { pod ->
                    PODListContainer(
                        pod.title,
                        pod.getFormattedDate(),
                        pod.imageUrl,
                        pod.id,
                        onPODClicked
                    )
                }
            }

        }
        FloatingActionButton(
            onClick = { onSortClicked() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            containerColor = Color.Blue
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_reorder),
                    contentDescription = "Reorder Icon"
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    stringResource(R.string.reorder_list),
                    color = Color.White,
                )
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PODListContainer(
    title: String,
    date: String,
    url: String,
    id: String,
    onPODClicked: (id: String) -> Unit,
) {

    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .clickable { onPODClicked(id) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlideImage(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentDescription = "",
            model = url,
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(16.dp))

        Column {
            Text(text = title, color = Color.White)
            Text(text = date, color = Color.White)
        }
    }

}

//todo spacing for texts

//todo loader for glide image

//todo back functionality

//todo back arrow should be white
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlanetDetailsScreen(
    id: String,
    vm: PODViewModel
) {

    val pod = vm.detailPodState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        vm.loadPod(id)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        pod.value?.let { loadedPod ->

            GlideImage(
                model = loadedPod.imageUrlHQ ?: loadedPod.imageUrl,
                contentDescription = "Pod image",
                modifier = Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back arrow",
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = stringResource(R.string.our_universe),
                        fontSize = 24.sp,
                        color = Color.White
                    )

                }

                Spacer(modifier = Modifier.height(48.dp))

                Text(text = loadedPod.title, fontSize = 32.sp, color = Color.White)

                Spacer(modifier = Modifier.height(38.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(text = loadedPod.getFormattedDate(), fontSize = 16.sp, color = Color.White)


                    //todo color should be white
                    Image(
                        painter = painterResource(
                            id = if (loadedPod.isFavorite) {
                                R.drawable.ic_favorite_filled
                            } else {
                                R.drawable.ic_favorite_border
                            }
                        ),
                        contentDescription = "back arrow",
                        modifier = Modifier.clickable {
                            if (loadedPod.isFavorite) {
                                vm.removePodFromFavorites(loadedPod)
                            } else {
                                vm.addPODToFavorite(loadedPod)
                            }
                        }
                    )

                }

                Spacer(modifier = Modifier.height(14.dp))


                Text(text = loadedPod.explanation, fontSize = 16.sp, color = Color.White)


            }
        } ?: run {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
            )
        }
    }

}

//todo determine whether we should dismiss dialog after action
@Composable
fun PODSortDialogScreen(
    onSortTypeSelected: (type: FilterType) -> Unit
) {

    var sortType by rememberSaveable { mutableStateOf(FilterType.TITLE) }

    Column(
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(16.dp),

        ) {
        Text("Reorder list", color = Color.White, fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.reorder_by_title), color = Color.White)

            RadioButton(
                selected = sortType == FilterType.TITLE,
                onClick = { sortType = FilterType.TITLE })

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.reorder_by_date), color = Color.White)

            RadioButton(
                selected = sortType == FilterType.DATE,
                onClick = { sortType = FilterType.DATE })

        }

        Button(
            onClick = { onSortTypeSelected(sortType) }, modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(start = 16.dp, end = 16.dp)
        ) {

            Text(stringResource(R.string.apply), color = Color.White)

        }

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = stringResource(R.string.reset), modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    sortType = FilterType.TITLE
                    onSortTypeSelected(sortType)
                }, textAlign = TextAlign.Center
        )


    }

}

@Composable
fun PlanetsApp() {
    val navController = rememberNavController()
    val podListViewModel: PODViewModel = koinViewModel()
    NavHost(navController = navController, startDestination = PODS) {
        composable<PODS> {
            PODListScreen(
                podListViewModel,
                onPODClicked = { id -> navController.navigate(route = PODDetails(id = id)) },
                onSortClicked = {
                    navController.navigate(PODSortSettings)
                }
            )
        }
        composable<PODDetails> { navBackStackEntry ->
            val pod: PODDetails = navBackStackEntry.toRoute()
            PlanetDetailsScreen(
                id = pod.id,
                vm = podListViewModel
            )
        }
        dialog<PODSortSettings> {
            PODSortDialogScreen(
                onSortTypeSelected = { sortType ->
                    podListViewModel.setFilterType(sortType)
                })
        }
    }
}


@Serializable
object PODS

@Serializable
data class PODDetails(
    val id: String
)

@Serializable
object PODSortSettings
