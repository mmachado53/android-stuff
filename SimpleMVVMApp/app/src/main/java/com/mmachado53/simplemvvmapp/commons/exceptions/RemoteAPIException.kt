package com.mmachado53.simplemvvmapp.commons.exceptions
open class RemoteAPIException(message: String? = null, throwable: Throwable? = null) : Exception(message, throwable) {
    object ConnectionException : RemoteAPIException()
    object UnauthorizedException : RemoteAPIException()
    object ParsingException : RemoteAPIException()
    class LocalizedAPIErrorException(message: String) : RemoteAPIException(message)
    class InternalServerErrorException(val responseCode: Int) : RemoteAPIException()
}
