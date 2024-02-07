package com.oyo.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.websocket.server.WsFrameServer;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long bookingId;

    @Column( name = "check_In" )
    private LocalDate checkedInDate;

    @Column( name = "check_Out")
    private LocalDate checkedOutDate;

    @Column( name = "guest_FullName" )
    private String guestFullName;

    @Column( name = "guest_Email" )
    private String guestEmail;

    @Column( name = "adults")
    private int NumOfAdults; // default value is '0';

    @Column( name = "children")
    private int NumOfChildren;

    @Column( name = "total_guest")
    private int totalNumOfGuest;

    @Column( name = "confirmation_code")
    private String bookingConfirmationCode; // Default value is null;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "room_id")
    // in booking table, the primary key of the Room table will be used as foreign key
    private Room room;













    public void calculateTotalNumberOfGuest(){ // why we are using public here, why not private?
        this.totalNumOfGuest = this.NumOfAdults + this.NumOfChildren;
        // we will use this method when the user sets the value of number of adults and number of children
    }

    public void setNumOfAdults( int numOfAdults ) {
        NumOfAdults = numOfAdults;
        calculateTotalNumberOfGuest();
    }

    public void setNumOfChildren( int numOfChildren ) {
        NumOfChildren = numOfChildren;
        calculateTotalNumberOfGuest();
    }

    public void setBookingConfirmationCode( String bookingConfirmationCode ) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
