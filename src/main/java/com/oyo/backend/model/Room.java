package com.oyo.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id; // using Wrapper class

    private String roomType;
    private BigDecimal roomPrice; // BigDecimal from java.math for precise monetary calculation :

    @Lob // We usually save images as large objects 'LOB' and  we use Blob as variable type "Binary large object "
    private Blob photo;

    private boolean isBooked;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    /*
        Without the room, the bookedRooms doesn't make any sense, here the Rooms is the parent and BookedRooms is the child
        If the parent doesn't exist, children doesn't make any sense alone, so the children also should be remove,
        To achieve this, we have to use cascadeType.ALL

        BookedRoom is the manySide and Room is the one's cardinality, we put, one side in manyside table, here we take
        the Room object and refence it in the bookingRooms table, here the booking room table is the owning table,
        and room is the inverse table. mappebBy = "room", room is the joining attribut from booked room table. This also
        represents that this object will be mapped by the BookedRooms table.

    */

    private List<BookedRoom> bookings;
    // The current room may be present in many bookings, here we assume, in one booking we can book only one room.

    public Room() {
        this.bookings = new ArrayList<>();
    }

    public void addBooking( BookedRoom booking ){
        if( bookings == null ){
            bookings = new ArrayList<>();
        }
        bookings.add( booking );
        booking.setRoom( this );
        String confirmationCode = RandomStringUtils.randomNumeric(10); // Generation random confirmation code :
        booking.setBookingConfirmationCode( confirmationCode );
    }
}
