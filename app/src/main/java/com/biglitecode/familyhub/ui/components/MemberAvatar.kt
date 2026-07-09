package com.biglitecode.familyhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

/**
 * Circular avatar with initial letter and optional active/muted ring.
 */
@Composable
fun MemberAvatar(
    name: String,
    avatarColorHex: String?,
    isActive: Boolean = true,
    size: Dp = 52.dp,
    modifier: Modifier = Modifier
) {
    val fill = parseAvatarColor(avatarColorHex)
    val ring = if (isActive) ForestGreen else TextMutedBrown.copy(alpha = 0.45f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(width = 2.5.dp, color = ring, shape = CircleShape)
            .background(fill.copy(alpha = if (isActive) 1f else 0.45f), CircleShape)
    ) {
        Text(
            text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.38f).sp
        )
    }
}

fun parseAvatarColor(hex: String?): Color {
    if (hex.isNullOrBlank()) return ForestGreen
    return try {
        val cleaned = hex.removePrefix("#")
        val colorLong = cleaned.toLong(16)
        if (cleaned.length == 6) {
            Color(0xFF000000 or colorLong)
        } else {
            Color(colorLong)
        }
    } catch (_: Exception) {
        ForestGreen
    }
}
