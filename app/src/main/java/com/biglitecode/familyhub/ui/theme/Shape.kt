package com.biglitecode.familyhub.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shared shape scale for the FamilyHub warm "family chore tracker" theme.
 *
 *  - small  → chips, small badges, input fields
 *  - medium → info / task cards
 *  - large  → primary buttons, header pills, hero cards
 */
val FamilyHubShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp)
)