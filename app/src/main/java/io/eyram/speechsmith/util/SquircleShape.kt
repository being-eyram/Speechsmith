package io.eyram.speechsmith.util

import android.graphics.Matrix
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

val SquircleShape: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val baseWidth = 152f
        val baseHeight = 208f

        val path = Path()

        path.moveTo(0f, 62.7925f)
        path.cubicTo(0f, 33.1918f, 0f, 18.3915f, 8.6868f, 9.1957f)
        path.cubicTo(17.3736f, 0f, 31.3547f, 0f, 59.3171f, 0f)
        path.lineTo(92.6829f, 0f)
        path.cubicTo(120.6453f, 0f, 134.6264f, 0f, 143.3132f, 9.1957f)
        path.cubicTo(152f, 18.3915f, 152f, 33.1918f, 152f, 62.7925f)
        path.lineTo(152f, 145.2076f)
        path.cubicTo(152f, 174.8082f, 152f, 189.6085f, 143.3132f, 198.8042f)
        path.cubicTo(134.6264f, 208f, 120.6453f, 208f, 92.6829f, 208f)
        path.lineTo(59.3171f, 208f)
        path.cubicTo(31.3547f, 208f, 17.3736f, 208f, 8.6868f, 198.8042f)
        path.cubicTo(0f, 189.6085f, 0f, 174.8082f, 0f, 145.2076f)
        path.lineTo(0f, 62.7925f)
        path.close()

        return Outline.Generic(
            path
                .asAndroidPath()
                .apply {
                    transform(Matrix().apply {
                        setScale(size.width / baseWidth, size.height / baseHeight)
                    })
                }
                .asComposePath()
        )
    }
}