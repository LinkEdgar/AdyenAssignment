package com.adyen.android.assignment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.adyen.android.assignment.R

@Composable
fun ApodContainer() {

    Column {
        Image(painter = painterResource(R.drawable.ic_stars), contentDescription = "")

        Text(text = "Test")

        Text(text = "Test Description")
    }

}

@Preview
@Composable
fun APodContainerPreivew() {
    ApodContainer()
}