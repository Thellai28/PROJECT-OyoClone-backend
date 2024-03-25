package com.oyo.backend.service;

import com.oyo.backend.exception.ResourceNotFoundException;
import com.oyo.backend.model.BookedRoom;
import com.oyo.backend.response.BookingResponse;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


public interface IBookingService {
    void cancelBooking( Long bookingId );

    List<BookedRoom> getAllBookings();

    BookedRoom findByBookingConfirmationCode( String confirmationCode );

    String saveBookings( Long roomId, BookedRoom bookingRequest );
}
