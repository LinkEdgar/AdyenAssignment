package com.adyen.android.assignment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adyen.android.assignment.R
import com.adyen.android.assignment.util.Resource
import com.adyen.android.assignment.viewmodels.PODViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.toRoute
import com.adyen.android.assignment.ui.planets.PODImageModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun ApodContainer() {

    Column {
        Image(painter = painterResource(R.drawable.ic_stars), contentDescription = "")

        Text(text = "Test")

        Text(text = "Test Description")
    }

}

@Composable
fun PODListScreen(
    PODViewModel: PODViewModel = koinViewModel(),
    onPODClicked: (podTitle: String) -> Unit
) {

    val uiState = PODViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        PODViewModel.loadPlanets()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        when(uiState.value) {
            is Resource.Error -> {
                Text("error")
            }
            Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp)
                )
            }
            is Resource.Success -> {
                val podList: List<PODImageModel> = (uiState.value as? Resource.Success<List<PODImageModel>>)?.data ?: emptyList()
                PODListSuccessScreen(pods = podList, onPODClicked = onPODClicked)

            }
            Resource.Uninitiated -> {

            }
        }
    }
}

@Composable
fun PODListSuccessScreen(
    pods: List<PODImageModel>,
    onPODClicked: (podTitle: String) -> Unit
) {
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
                    onPODClicked
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
    onPlanetClicked: (podTitle: String) -> Unit
) {

    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .clickable { onPlanetClicked(title) },
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

@Composable
fun PlanetDetailsScreen(
    podTitle: String
) {
    
    Text(text = podTitle, color = Color.White)
    
}

@Preview
@Composable
fun APodContainerPreivew() {
    ApodContainer()
}

@Composable
fun PlanetsApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination =  PODS) {
        composable<PODS> {
            PODListScreen(
                onPODClicked = { podTitle ->  navController.navigate(route = PODDetails(title = podTitle)) }
            )
        }
        composable<PODDetails> { navBackStackEntry ->
            val pod: PODDetails = navBackStackEntry.toRoute()
            PlanetDetailsScreen(
                podTitle = pod.title
            )
        }
    }
}


@kotlinx.serialization.Serializable
object PODS

@kotlinx.serialization.Serializable
data class PODDetails(
    val title: String
)
