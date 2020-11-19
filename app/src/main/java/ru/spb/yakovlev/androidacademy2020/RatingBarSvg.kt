package ru.spb.yakovlev.androidacademy2020

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Shader
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.graphics.drawable.DrawableWrapper
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop


/**
 * Custom RatingBar with vector drawable support.
 * Additional attributes:
 *      innerPadding    the space between drawables.
 *      drawableHeight  the height of the drawable, if not specified, then is taken from drawable settings.
 *      drawableWidth   the width of the drawable, if not specified, then is taken from drawable settings.
 */
class RatingBarSvg @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.ratingBarStyle
) : AppCompatRatingBar(context, attrs, defStyleAttr) {

    private var mSampleTile: Bitmap? = null
    var halfOfInnerPadding: Int
    var drawableHeight: Int
    var drawableWidth: Int

    private val drawableShape: Shape
        get() {
            val roundedCorners = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
            return RoundRectShape(roundedCorners, null, null)
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBarSvg)
        halfOfInnerPadding =
            (typedArray.getDimension(R.styleable.RatingBarSvg_innerPadding, 0f) / 2).toInt()
        drawableHeight =
            typedArray.getDimension(R.styleable.RatingBarSvg_drawableHeight, 0f).toInt()
        drawableWidth = typedArray.getDimension(R.styleable.RatingBarSvg_drawableWidth, 0f).toInt()

        val drawable = tileify(progressDrawable, false) as LayerDrawable
        progressDrawable = drawable
        typedArray.recycle()
    }

    /**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     */
    @SuppressLint("RestrictedApi")
    private fun tileify(drawable: Drawable, clip: Boolean): Drawable {
        if (drawable is DrawableWrapper) {
            var inner: Drawable? = drawable.wrappedDrawable
            if (inner != null) {
                inner = tileify(inner, clip)
                drawable.wrappedDrawable = inner
            }
        } else if (drawable is LayerDrawable) {
            val numberOfLayers = drawable.numberOfLayers
            val outDrawables = arrayOfNulls<Drawable>(numberOfLayers)

            for (i in 0 until numberOfLayers) {
                val id = drawable.getId(i)
                outDrawables[i] = tileify(
                    drawable.getDrawable(i),
                    id == android.R.id.progress || id == android.R.id.secondaryProgress
                )
            }

            val newBg = LayerDrawable(outDrawables)

            for (i in 0 until numberOfLayers) {
                newBg.setId(i, drawable.getId(i))
            }
            return newBg

        } else if (drawable is BitmapDrawable) {
            val tileBitmap = drawable.bitmap
            if (mSampleTile == null) {
                mSampleTile = tileBitmap
            }

            val shapeDrawable = ShapeDrawable(drawableShape)
            val bitmapShader = BitmapShader(
                tileBitmap,
                Shader.TileMode.REPEAT, Shader.TileMode.CLAMP
            )
            shapeDrawable.paint.shader = bitmapShader
            shapeDrawable.paint.colorFilter = drawable.paint.colorFilter
            return if (clip)
                ClipDrawable(
                    shapeDrawable, Gravity.START,
                    ClipDrawable.HORIZONTAL
                )
            else
                shapeDrawable
        } else {
            return tileify(getBitmapDrawableFromVectorDrawable(drawable), clip)
        }

        return drawable
    }

    private fun getBitmapDrawableFromVectorDrawable(drawable: Drawable): BitmapDrawable {
        val drHeight = if (drawableHeight > 0) drawableHeight else drawable.intrinsicHeight
        val drWidth = if (drawableWidth > 0) drawableWidth else drawable.intrinsicWidth

        val bitmap = Bitmap.createBitmap(
            drWidth + halfOfInnerPadding * 2, //dp between svg images
            drHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(halfOfInnerPadding, 0, drWidth + halfOfInnerPadding, drHeight)
        drawable.draw(canvas)
        return BitmapDrawable(resources, bitmap)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var mHeight = measuredHeight

        val heightMS = if (heightMeasureSpec != MeasureSpec.EXACTLY) {
            mHeight = drawableHeight
            MeasureSpec.EXACTLY
        } else heightMeasureSpec

        super.onMeasure(widthMeasureSpec, heightMS)
        if (mSampleTile != null) {
            val width = mSampleTile!!.width * numStars
            setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(mHeight, heightMeasureSpec, 0)
            )
        }

        val lp = layoutParams as ViewGroup.MarginLayoutParams
        lp.setMargins(
            marginStart - halfOfInnerPadding,
            marginTop,
            marginEnd,
            marginBottom
        )
    }
}