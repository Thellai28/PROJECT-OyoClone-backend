package com.oyo.backend.controller;

import com.oyo.backend.model.Room;
import com.oyo.backend.response.RoomResponse;
import com.oyo.backend.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@RequiredArgsConstructor // Creates constructor injection by default, the commented out part
@RequestMapping("/rooms")
@RestController
public class RoomController {
    private final IRoomService roomService;

//    public RoomController( IRoomService roomService ) {
//        this.roomService = roomService;
//    }

    @PostMapping( "/add/new-room" )
    public ResponseEntity<RoomResponse> addNewRoom( // This method will return an HTTP response.
            @RequestParam( "photo" ) MultipartFile photo,
            @RequestParam("roomType" ) String roomType,
            @RequestParam( "roomPrice" ) BigDecimal roomPrice ) throws SQLException, IOException {
        // Exceptions are due to parsing handling MultipartFile, check RoomService for better clarity :
        Room savedRoom = roomService.addNewRoom( photo, roomType, roomPrice );
        RoomResponse response =  new RoomResponse(savedRoom.getId(),
                savedRoom.getRoomType(), savedRoom.getRoomPrice());
        return ResponseEntity.ok( response );
        /*
            In Spring Boot applications, ResponseEntity is a class provided by the Spring Framework that represents
            the entire HTTP response. It allows you to control the HTTP response status code, headers, and body
            returned to the client.
        */
    }
}
