package com.oyo.backend.repository;

import com.oyo.backend.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
    void deleteById( Long bookingId );
    BookedRoom findByBookingConfirmationCode(String confirmationCode);


    List<BookedRoom> findByRoomId( Long roomId);
}
