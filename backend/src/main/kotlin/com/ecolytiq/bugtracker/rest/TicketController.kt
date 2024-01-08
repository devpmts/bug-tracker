package com.ecolytiq.bugtracker.rest

import com.ecolytiq.bugtracker.domain.SortCriterion
import com.ecolytiq.bugtracker.domain.TicketService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin
class TicketController @Autowired constructor(
    private val ticketService: TicketService,
    private val ticketDtoConverter: TicketDtoConverter,
) {


    private val logger = LoggerFactory.getLogger(TicketController::class.java)

    @GetMapping(path = ["/api/v1/tickets"], produces = [APPLICATION_JSON_VALUE])
    fun getAllTickets(@RequestParam(required = false) sort: Optional<SortCriterion>): ResponseEntity<List<TicketDto>> {
        logger.info("Received request [sort=$sort]")

        val foundTickets =
            if (sort.isPresent) ticketService.getTicketsSorted(sort.get()) else ticketService.getAllTickets()

        val response = foundTickets.map { ticketDtoConverter.convertToTicketDto(it) }

        return ResponseEntity.ok(response)
    }
}
