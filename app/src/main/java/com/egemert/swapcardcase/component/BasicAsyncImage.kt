package com.egemert.swapcardcase.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.egemert.swapcardcase.R

@Composable
fun BasicAsyncImage(
    modifier: Modifier = Modifier,
    image: Any,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality
) {
    val context = LocalContext.current
    val placeholder = R.drawable.ic_launcher_background

    val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            super.onError(request, result)
        }

        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            super.onSuccess(request, result)
        }
    }
    val imageRequest = ImageRequest.Builder(context)
        .data(image)
        .listener(listener)
        .memoryCacheKey(image.toString())
        .diskCacheKey(image.toString())
        .placeholder(placeholder)
        .error(placeholder)
        .fallback(placeholder)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    AsyncImage(
        model = imageRequest,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        alpha = alpha
    )
}