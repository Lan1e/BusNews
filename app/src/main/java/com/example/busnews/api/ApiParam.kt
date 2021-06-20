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
        private const val COMMA = ','
    }

    override fun toString(): String {
        return "${key.apiFormat()}=${value.apiFormat()}"
    }

    private fun String.apiFormat() =
        replaceSymbols(listOf(SPACE, SINGLE_QUOTATION_MARK, SLASH, COMMA))

    private fun Char.toHex() =
        "%${toInt().toHexString()}"

    private fun String.replaceSymbols(symbols: List<Char>) =
        let {
            var result = this
            symbols.forEach {
                result = result.replace(Regex(it.toString()), it.toHex())
            }
            return@let result
        }

}