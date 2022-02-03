package org.geeksforgeeks.textureviewexample

import android.content.res.AssetFileDescriptor
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.view.TextureView

class MainActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var assetFileDescriptor: AssetFileDescriptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mTextureView = findViewById<TextureView>(R.id.texture_view_1)
        mTextureView.surfaceTextureListener = this@MainActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        try {
            assetFileDescriptor = assets.openFd("sample_video.mp4")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val surface = Surface(surfaceTexture)
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
            )
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setSurface(surface)
            mediaPlayer?.isLooping = true
            mediaPlayer?.setOnPreparedListener { player -> player?.start() }
            mediaPlayer?.setOnErrorListener { _, _, _ -> false }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {}

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
        return false
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}
}