package com.mvp.mvptemplate.utils.storage


class Folder(val folderName: String, val preview: String){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Folder

        if (folderName != other.folderName) return false

        return true
    }

    override fun hashCode(): Int {
        return folderName.hashCode()
    }
}
