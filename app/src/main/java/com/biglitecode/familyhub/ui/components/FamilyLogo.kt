package com.biglitecode.familyhub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.biglitecode.familyhub.R

/**
 * Shared FamilyHub brand logo used across splash, login, and other screens.
 */
@Composable
fun FamilyLogo(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    contentDescription: String? = "FamilyHub Logo"
) {
    Image(
        painter = painterResource(id = R.drawable.ic_family),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(size)
    )
}

/**
 * Logo inside a soft frosted surface — matches splash / login header style.
 */
@Composable
fun FamilyLogoBadge(
    modifier: Modifier = Modifier,
    badgeSize: Dp = 110.dp,
    logoSize: Dp = 72.dp,
    surfaceColor: Color = Color.White.copy(alpha = 0.18f),
    cornerRadius: Dp = 28.dp
) {
    Surface(
        shape = RoundedCornerShape(cornerRadius),
        color = surfaceColor,
        modifier = modifier.size(badgeSize)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            FamilyLogo(size = logoSize)
        }
    }
}
