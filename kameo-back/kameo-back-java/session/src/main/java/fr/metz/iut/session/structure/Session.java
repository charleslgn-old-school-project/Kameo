package fr.metz.iut.session.structure;

import java.util.*;

import fr.metz.iut.dataacces.PersistentStorage;
import fr.metz.iut.film.structure.Film;
import fr.metz.iut.session.exception.SitNotAvailableException;
import fr.metz.iut.session.structure.type.TicketStatus;

/**
 * A session represent when and where a {@link Film} is displayed.
 */
public class Session implements PersistentStorage {

  private final String id;
  private final Date sessionDate;
  private final Room room;
  private final Film film;
  private final Set<Ticket> tickets = new HashSet<>();

  public Session(final String id, final Date sessionDate, final Room room, final Film film) {
    this.id = id;
    this.sessionDate = sessionDate;
    this.room = room;
    this.film = film;
  }

  @Override
  public String getId() {
    return id;
  }

  public Date getSessionDate() {
    return sessionDate;
  }

  public Room getRoom() {
    return room;
  }

  public Film getFilm() {
    return film;
  }

  public Set<Ticket> getTickets() {
    return Collections.unmodifiableSet(tickets);
  }

  public void addTicket(final Ticket ticket) {
    removeTicket(ticket);
    tickets.add(ticket);
  }

  public void removeTicket(final Ticket ticket) {
    tickets.removeIf(t -> t.getId().equals(ticket.getId()));
  }

  public void reservedPlace(final Ticket ticket) throws SitNotAvailableException {
    if (!isSitPossible(ticket.getRow(), ticket.getColumn())) {
      throw new SitNotAvailableException();
    }
    addTicket(ticket);
  }

  private boolean isSitPossible(final int row, final int column) {
    return room.sitExists(row, column)
           && sitNotTaken(row, column);
  }

  private boolean sitNotTaken(final int row, final int column) {
    return tickets.stream()
                  .filter(ticket -> TicketStatus.sitReserved(ticket.getStatus()))
                  .anyMatch(ticket -> ticket.getRow() == row && ticket.getColumn() == column);
  }
}
