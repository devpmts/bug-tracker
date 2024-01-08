package com.ecolytiq.bugtracker.domain

import com.ecolytiq.bugtracker.domain.Status.CODE_REVIEW
import com.ecolytiq.bugtracker.domain.Status.OPEN
import com.ecolytiq.bugtracker.persistence.TicketRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.OffsetDateTime

@SpringBootTest
class TicketServiceTest @Autowired constructor(
    private val ticketService: TicketService,
    private val ticketRepository: TicketRepository,
) {

    private val ticket1 = Ticket(
        id = "BUG-123",
        title = "Save Button not responding",
        status = CODE_REVIEW,
        createdAt = OffsetDateTime.parse("2023-02-15T10:25:43.785+01:00"),
    )

    private val ticket2 = Ticket(
        id = "BUG-456",
        title = "Incorrect password reset link",
        status = OPEN,
        createdAt = OffsetDateTime.parse("2023-02-27T15:36:26.517+01:00"),
    )

    private val ticket3 = Ticket(
        id = "BUG-789",
        title = "Inconsistent sorting of search results",
        status = OPEN,
        createdAt = OffsetDateTime.parse("2022-02-28T12:43:07.458+01:00")
    )

    private val givenTickets = listOf(
        ticket2,
        ticket1,
        ticket3
    )

    @BeforeEach
    fun cleanUp() {
        ticketRepository.deleteAll()
    }

    @Test
    fun getAllTickets_noTicketsArePresent_returnsEmptyList() {
        val actualTickets = ticketService.getAllTickets()

        assertThat(actualTickets).hasSize(0)
    }

    @ParameterizedTest
    @EnumSource(SortCriterion::class)
    fun getTicketsSorted_checkSortCriteria_returnsSortedList(criterion: SortCriterion) {
        ticketRepository.saveAll(givenTickets)
        val actualTickets = ticketService.getTicketsSorted(criterion)
        when (criterion) {
            SortCriterion.ID -> assertThat(actualTickets).first().isEqualTo(ticket1)
            SortCriterion.TITLE -> assertThat(actualTickets).first().isEqualTo(ticket3)
            SortCriterion.STATUS -> assertThat(listOf(ticket1, ticket2)).contains(actualTickets.first())
            SortCriterion.CREATED_AT -> assertThat(actualTickets).first().isEqualTo(ticket3)
        }
    }

    @Test
    fun getAllTickets_ticketsArePresent_returnsAllTickets() {
        ticketRepository.saveAll(givenTickets)

        val actualTickets = ticketService.getAllTickets()

        assertThat(actualTickets).hasSize(3)
        assertThat(actualTickets).usingRecursiveComparison().isEqualTo(givenTickets)
    }
}
