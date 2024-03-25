package com.oyo.backend.service;

import com.oyo.backend.exception.InvalidBookRequestException;
import com.oyo.backend.model.BookedRoom;
import com.oyo.backend.model.Room;
import com.oyo.backend.repository.BookingRepository;
import com.oyo.backend.response.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService{
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    public List<BookedRoom> getAllBookingByRoomId( Long roomId ) {
        return bookingRepository.findByRoomId(roomId) ;
    }

    @Override
    public void cancelBooking( Long bookingId ) {
        bookingRepository.deleteById(bookingId);

    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public BookedRoom findByBookingConfirmationCode( String confirmationCode ) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode);
    }

    @Override
    public String saveBookings( Long roomId, BookedRoom bookingRequest ) {
        if( bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookRequestException("Check in date must be before check out date");
        }
        Room theRoom = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = theRoom.getBookings();
        boolean roomIsAvailable = roomIsAvailable( bookingRequest, existingBookings );

        if( roomIsAvailable ){
            theRoom.addBooking(bookingRequest);
            bookingRepository.save( bookingRequest );
        }else{
            throw new InvalidBookRequestException("Sorry!, This room is not available for the selected dates");
        }
        return bookingRequest.getBookingConfirmationCode();
    }


    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
