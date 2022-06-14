package com.harshita.pdfprintwithwifiprinter

import android.content.Context
import java.io.File

object Comman {

    fun getAppPath(context: Context): String? {
        //File dir = new File(android.os.Environment.getExternalStorageDirectory()
        val dir = File(
            context.filesDir
                .toString() + File.separator
                    + context.resources.getString(R.string.app_name)
                    + File.separator
        )
        if (!dir.exists()) dir.mkdir()
        return dir.path + File.separator
    }
}