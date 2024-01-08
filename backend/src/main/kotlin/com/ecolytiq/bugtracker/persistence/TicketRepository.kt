package com.ecolytiq.bugtracker.persistence

import com.ecolytiq.bugtracker.domain.Ticket
import org.springframework.data.jpa.repository.JpaRepository

interface TicketRepository : JpaRepository<Ticket, String> {
    fun findAllByOrderByIdAsc(): List<Ticket>
    fun findAllByOrderByTitleAsc(): List<Ticket>
    fun findAllByOrderByStatusAsc(): List<Ticket>
    fun findAllByOrderByCreatedAtAsc(): List<Ticket>
}
