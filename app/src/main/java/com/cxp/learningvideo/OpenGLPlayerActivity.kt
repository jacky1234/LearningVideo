package com.cxp.learningvideo

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Surface
import com.chenlittleping.videoeditor.decoder.MMExtractor
import com.cxp.learningvideo.media.decoder.AudioDecoder
import com.cxp.learningvideo.media.decoder.VideoDecoder
import com.cxp.learningvideo.opengl.SimpleRender
import com.cxp.learningvideo.opengl.drawer.IDrawer
import com.cxp.learningvideo.opengl.drawer.VideoDrawer
import kotlinx.android.synthetic.main.activity_opengl_player.*
import java.util.concurrent.Executors


/**
 * 使用OpenGL渲染的播放器
 *
 * @author Chen Xiaoping (562818444@qq.com)
 * @since LearningVideo
 * @version LearningVideo
 * @Datetime 2019-10-26 21:07
 *
 */
class OpenGLPlayerActivity : AppCompatActivity() {
    companion object {
        const val TAG = "OpenGLPlayerActivity"
    }

    val path = Environment.getExternalStorageDirectory().absolutePath + "/test/mvtest.mp4"
    lateinit var drawer: IDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_player)
        initRender()
    }

    private fun initRender() {
        drawer = VideoDrawer()
        val mmExtractor = MMExtractor(path)
        val withAndHeight = mmExtractor.getWithAndHeight()
        if (withAndHeight == null) {
            Log.e(TAG, "getWithAndHeight null! return")
            return
        }
        drawer.setVideoSize(withAndHeight[0], withAndHeight[1])
        drawer.getSurfaceTexture {
            initPlayer(Surface(it))
        }
        gl_surface.setEGLContextClientVersion(2)
        val render = SimpleRender()
        render.addDrawer(drawer)
        gl_surface.setRenderer(render)
    }

    private fun initPlayer(sf: Surface) {
        val threadPool = Executors.newFixedThreadPool(10)

        val videoDecoder = VideoDecoder(path, null, sf)
        threadPool.execute(videoDecoder)

        val audioDecoder = AudioDecoder(path)
        threadPool.execute(audioDecoder)

        videoDecoder.goOn()
        audioDecoder.goOn()
    }
}