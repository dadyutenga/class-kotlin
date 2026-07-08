package com.biglitecode.familyhub.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.biglitecode.familyhub.ui.components.FamilyLogoBadge
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.GoldYellow
import com.biglitecode.familyhub.ui.theme.TextBrown
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0.6f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
        )
        delay(900)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo badge inside a gold ring — circular avatar chip style.
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .background(ForestGreen.copy(alpha = 0.08f))
                    .border(width = 2.dp, color = GoldYellow, shape = CircleShape)
                    .scale(scale.value),
                contentAlignment = Alignment.Center
            ) {
                FamilyLogoBadge(
                    badgeSize = 120.dp,
                    logoSize = 88.dp,
                    cornerRadius = 28.dp,
                    surfaceColor = Color.Transparent
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "FamilyHub",
                color = TextBrown,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Stay connected, stay organized",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Splash")
@Composable
private fun SplashScreenPreview() {
    FamilyHubTheme {
        SplashScreen(onTimeout = {})
    }
}