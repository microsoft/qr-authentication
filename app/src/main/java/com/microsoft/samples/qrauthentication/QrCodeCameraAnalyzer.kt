/*
 *
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 *
 */

package com.microsoft.samples.qrauthentication

import android.graphics.ImageFormat
import android.os.Build
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import java.nio.ByteBuffer

/**
 * Class to analyze images from camera and process Qr code information
 */
class QrCodeCameraAnalyzer(
    private val onQrCodesDetected: (qrText: String) -> Unit
) : ImageAnalysis.Analyzer {
    private val yuvFormats: List<Int> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        listOf(ImageFormat.YUV_420_888, ImageFormat.YUV_422_888, ImageFormat.YUV_444_888)
    } else {
        listOf(ImageFormat.YUV_420_888)
    }

    private val reader = QRCodeReader()

    override fun analyze(image: ImageProxy) {
        // ImageReader uses YUV format by default.
        if (image.format !in yuvFormats) {
            Log.e(TAG, "Expected YUV format, received: ${image.format}")
            image.close()
            return
        }

        val source = PlanarYUVLuminanceSource(
            image.planes[0].buffer.toByteArray(),
            image.width,
            image.height,
            0,
            0,
            image.width,
            image.height,
            false
        )

        try {
            val result = reader.decode(BinaryBitmap(HybridBinarizer(source)))
            onQrCodesDetected(result.text)
        } catch (e: NotFoundException) {
            e.printStackTrace()
            Log.d(TAG, "No Qr code found")
        } catch (e: FormatException) {
            e.printStackTrace()
            Log.d(TAG, "Qr is incorrect format")
        } catch (e: ChecksumException) {
            e.printStackTrace()
            Log.d(TAG, "Qr code doesn't have a correct checksum")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "Unknown error")
        } finally {
            image.close()
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }

    companion object {
        private const val TAG = "QrCodeCameraAnalyzer"
    }
}
