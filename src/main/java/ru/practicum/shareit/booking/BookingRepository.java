package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByBookerIdOrderByStartDesc(long bookerId);

    @Query("SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = :ownerId ORDER BY b.start DESC")
    Collection<Booking> findByOwnerIdOrderByStartDesc(@Param("ownerId") long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start > :now " +
            "AND b.status IN (:validStatuses) ORDER BY b.start ASC")
    Booking findNextBookingForItem(
            @Param("itemId") Long itemId,
            @Param("now") LocalDateTime now,
            @Param("validStatuses") Collection<BookingStatus> validStatuses
    );

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.end < :now " +
            "ORDER BY b.end DESC")
    Booking findLastBookingForItem(
            @Param("itemId") Long itemId,
            @Param("now") LocalDateTime now
    );

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.item.id = :itemId AND b.booker.id = :bookerId AND b.status IN (:statuses)")
    boolean existsByItemIdAndBookerIdAndStatusIn(
            @Param("itemId") Long itemId,
            @Param("bookerId") Long bookerId,
            @Param("statuses") Collection<BookingStatus> statuses
    );
}