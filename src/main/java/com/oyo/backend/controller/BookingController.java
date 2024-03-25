package com.oyo.backend.controller;

import com.oyo.backend.exception.InvalidBookRequestException;
import com.oyo.backend.exception.ResourceNotFoundException;
import com.oyo.backend.model.BookedRoom;
import com.oyo.backend.model.Room;
import com.oyo.backend.response.BookingResponse;
import com.oyo.backend.response.RoomResponse;
import com.oyo.backend.service.IBookingService;
import com.oyo.backend.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings" )
public class BookingController {

    private final IBookingService bookingService;
    private final IRoomService roomService;
    @GetMapping("/all-bookings" )
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedRoom> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();

        for( BookedRoom booking : bookings ){
            BookingResponse bookingResponse  =  getBookingResponse( booking );
            bookingResponses.add( bookingResponse );
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode ){
        try{ // because the confirmation code may or may not present in Data Base :
            BookedRoom booking = bookingService.findByBookingConfirmationCode( confirmationCode );
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex ){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBookings( @PathVariable Long roomId, @RequestBody BookedRoom bookingRequest ){
        try{
            String confirmationCode = bookingService.saveBookings(roomId, bookingRequest );
            return ResponseEntity
                    .ok( "Room booked successfully ! Your confirmation code is " + confirmationCode );
        }catch(InvalidBookRequestException ex ){
            return ResponseEntity.badRequest().body(ex.getMessage() );
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete" )
    public void cancelBooking(@PathVariable Long bookingId ){
        bookingService.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedRoom booking ){
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse roomResponse = new RoomResponse( theRoom.getId(),
                theRoom.getRoomType(),theRoom.getRoomPrice());

        return new BookingResponse(booking.getBookingId(), booking.getCheckedInDate(),
                booking.getCheckedOutDate(), booking.getGuestFullName(), booking.getGuestEmail(),
                booking.getNumOfAdults(), booking.getNumOfChildren(), booking.getTotalNumOfGuest(),
                booking.getBookingConfirmationCode(), roomResponse );
    }


}
