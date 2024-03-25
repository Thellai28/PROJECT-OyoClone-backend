package com.oyo.backend.controller;

import com.oyo.backend.exception.PhotoRetrievalException;
import com.oyo.backend.exception.ResourceNotFoundException;
import com.oyo.backend.model.BookedRoom;
import com.oyo.backend.model.Room;
import com.oyo.backend.response.BookingResponse;
import com.oyo.backend.response.RoomResponse;
import com.oyo.backend.service.BookingService;
import com.oyo.backend.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor // Creates constructor injection by default, the commented out part
@RequestMapping("/rooms")
@RestController
public class RoomController {
    private final IRoomService roomService;
    private final BookingService bookingService;



    @PostMapping( "/add/new-room" )
    public ResponseEntity<RoomResponse> addNewRoom( // This method will return an HTTP response.
            @RequestParam( "photo" ) MultipartFile photo,
            @RequestParam("roomType" ) String roomType,
            @RequestParam( "roomPrice" ) BigDecimal roomPrice ) throws SQLException, IOException {

        // Exceptions are due to parsing handling MultipartFile, check RoomService for better clarity :
        Room savedRoom = roomService.addNewRoom( photo, roomType, roomPrice );
        RoomResponse response =  new RoomResponse(savedRoom.getId(),
                savedRoom.getRoomType(), savedRoom.getRoomPrice());


        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(savedRoom.getId())
                            .toUri();


        return ResponseEntity.created(location).body(response);
        /*
            In Spring Boot applications, ResponseEntity is a class provided by the Spring Framework that represents
            the entire HTTP response. It allows you to control the HTTP response status code, headers, and body
            returned to the client.
        */
    }








    @GetMapping("/room/types")
    public List<String> getRoomTypes(){
        return roomService.getAllRoomTypes();
    }


    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();

        for( Room room : rooms ){
            byte[] photoBytes = roomService.getRoomPhotoByRoomId( room.getId() );

            if( photoBytes != null && photoBytes.length > 0 ){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add( roomResponse );
            }
        }
        return ResponseEntity.ok( roomResponses );
    }





    private RoomResponse getRoomResponse( Room room ) {
        List<BookedRoom> bookings = getAllBookingsByRoomId( room.getId() ); // This returns null as of now.

//        List<BookingResponse> bookingInfo = bookings
//                .stream()
//                .map( booking -> new BookingResponse(booking.getBookingId(),
//                        booking.getCheckedInDate(), booking.getCheckedOutDate(),
//                        booking.getBookingConfirmationCode()))
//                .toList();

        byte[] photoByte = null;
        Blob photoBlob = room.getPhoto();
        if( photoBlob != null ){
            try{
                photoByte = photoBlob.getBytes(1, (int)photoBlob.length() );
            }catch( SQLException e ){
                throw new PhotoRetrievalException( "Error retrieving photo" );
            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(),
                room.isBooked(),photoByte, null);
    }






    private List<BookedRoom> getAllBookingsByRoomId( Long roomId ) {
        return bookingService.getAllBookingByRoomId( roomId );
    }





@DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom( @PathVariable Long roomId ){
        // If you need to use different variable name other than roomId, then you have to use this syntax
        // deleteRoom( @PathVariable("roomId") Long id ) -> syntax to use different variable name.
        roomService.deleteRoom( roomId );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


@PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom( @PathVariable Long roomId,
                                                    @RequestParam( required = false ) String roomType,
                                                    @RequestParam( required = false )BigDecimal roomPrice,
                                                    @RequestParam( required = false )MultipartFile photo )
                                                    throws IOException, SQLException {
        byte[] photoBytes = ( photo != null && !photo.isEmpty() ) ?
                photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);

        Blob photoBlob = ( photoBytes != null && photoBytes.length > 0) ?
                new SerialBlob(photoBytes) : null;

        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        theRoom.setPhoto( photoBlob );

        RoomResponse roomResponse = getRoomResponse( theRoom );
        return ResponseEntity.ok(roomResponse);
    }



    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById( @PathVariable Long roomId ){
        Optional<Room> theRoom = roomService.getRoomById( roomId );
        return theRoom.map( (room) ->{
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(()-> new ResourceNotFoundException("Room not found"));
    }
}
