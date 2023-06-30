package com.mgt.earthquake.exception

import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.hateoas.Link
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.serde.annotation.SerdeImport
import jakarta.inject.Singleton
import java.io.PrintWriter
import java.io.StringWriter

@Produces
@Singleton
@Replaces(RuntimeException::class)
@SerdeImport(JsonError::class)
class GlobalExceptionHandler : ExceptionHandler<RuntimeException, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>, e: RuntimeException): HttpResponse<*> {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        val error = JsonError(e.message ?: "No error message supplied")
                        .link(Link.SELF, Link.of(request.uri))

        val responseError =
            ResponseError(500, error, e.stackTrace[0].toString())

        return HttpResponse
            .ok<Any>()
            .body(responseError)
    }
}