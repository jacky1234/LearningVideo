package com.cxp.learningvideo.utils

import com.chenlittleping.videoeditor.decoder.MMExtractor
import java.lang.RuntimeException

object MediaUtil {
    fun getWidthAndHeight(path: String): IntArray {
        return MMExtractor(path).getWithAndHeight()
            ?: throw RuntimeException("${path}:getWithAndHeight null!")
    }
}