package com.cxp.learningvideo

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Surface
import com.chenlittleping.videoeditor.decoder.MMExtractor
import com.cxp.learningvideo.media.IDecoder
import com.cxp.learningvideo.media.decoder.AudioDecoder
import com.cxp.learningvideo.media.decoder.VideoDecoder
import com.cxp.learningvideo.opengl.SimpleRender
import com.cxp.learningvideo.opengl.drawer.IDrawer
import com.cxp.learningvideo.opengl.drawer.VideoDrawer
import com.cxp.learningvideo.utils.MediaUtil
import kotlinx.android.synthetic.main.activity_opengl_player.*
import java.util.concurrent.ExecutorService
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
    var videoDecoder: IDecoder?  = null
    var audioDecoder: IDecoder?  = null
    var executorService: ExecutorService?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_player)
        initRender()
    }

    private fun initRender() {
        drawer = VideoDrawer()
        val widthAndHeight = MediaUtil.getWidthAndHeight(path)
        drawer.setVideoSize(widthAndHeight[0], widthAndHeight[1])
        drawer.getSurfaceTexture {
            initPlayer(Surface(it))
        }
        gl_surface.setEGLContextClientVersion(2)
        val render = SimpleRender()
        render.addDrawer(drawer)
        gl_surface.setRenderer(render)
    }

    private fun initPlayer(sf: Surface) {
        executorService = Executors.newFixedThreadPool(10)
        videoDecoder = VideoDecoder(path, null, sf)
        audioDecoder = AudioDecoder(path)
        executorService?.execute(videoDecoder)
        executorService?.execute(audioDecoder)

        videoDecoder?.goOn()
        audioDecoder?.goOn()
    }

    override fun onDestroy() {
        super.onDestroy()

        videoDecoder?.stop()
        audioDecoder?.stop()
        try {
            executorService?.shutdown()
        } catch (e: Exception) {
        }
    }
}