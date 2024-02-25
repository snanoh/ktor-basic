package com.example.route

import com.example.models.Customer
import com.example.models.customerStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customerRouting() {


    route("/customer") {
        get {
            if (customerStorage.isNotEmpty()) call.respond(customerStorage)
            else call.respondText("No Customer Found.", status = HttpStatusCode.OK)
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                text = "Missing Id",
                status = HttpStatusCode.BadRequest
            )
            val customer = customerStorage.find { it.id == id } ?: return@get call.respondText(
                text = "No Customer with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respondText(
                text = "Customer stored correctly",
                status = HttpStatusCode.Created
            )
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (customerStorage.removeIf { it.id == id })
                call.respondText(
                    text = "Customer remove correctly",
                    status = HttpStatusCode.Accepted
                )
            else call.respondText(text = "Not found.", status = HttpStatusCode.NotFound)
        }
    }
}
