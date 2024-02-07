package com.oyo.backend.response;

import com.oyo.backend.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long id;
    private LocalDate checkedInDate;
    private LocalDate checkedOutDate;
    private String guestFullName;
    private String guestEmail;
    private int NumOfAdults; // default value is '0';
    private int NumOfChildren;
    private int totalNumOfGuest;
    private String bookingConfirmationCode; // Default value is null;

    // in booking table, the primary key of the Room table will be used as foreign key
    private RoomResponse room;

    public BookingResponse( Long id, LocalDate checkedInDate, LocalDate checkedOutDate,
                            String bookingConfirmationCode ) {
        this.id = id;
        this.checkedInDate = checkedInDate;
        this.checkedOutDate = checkedOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
