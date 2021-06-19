package com.example.busnews.api

import okhttp3.internal.toHexString

data class ApiParam(
    val key: String,
    val value: String
) {
    companion object {
        private const val SPACE = ' '
        private const val SINGLE_QUOTATION_MARK = '\''
        private const val SLASH = '/'
    }

    override fun toString(): String {
        return "${key.apiFormat()}=${value.apiFormat()}"
    }

    private fun String.apiFormat() =
        this.let {
            it.replace(Regex(SPACE.toString()), SPACE.toHex()).let {

                it.replace(Regex(SLASH.toString()), SLASH.toHex())

            }
        }

    private fun Char.toHex() =
        "%${this.toInt().toHexString()}"
}