package com.oyo.backend.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;


@Data // @Getter, @Setter, equals(), hashCode(), and toString() methods &  A constructor that initializes all final and non-null fields.
@NoArgsConstructor
public class RoomResponse {

    private Long id;

    private String roomType;

    private BigDecimal roomPrice;

    private boolean isBooked;

    private String photo;

    private List<BookingResponse> bookings;

    public RoomResponse( Long id, String roomType, BigDecimal roomPrice ) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse( Long id, String roomType, BigDecimal roomPrice,
                         boolean isBooked, byte[] photoBytes, List<BookingResponse> bookings ) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = (photoBytes != null ) ? Base64.encodeBase64String( photoBytes) : null ;
        //this.bookings = bookings;

        /* ----------< NOTES : why we have get the byte[], when we retrieve images form DB >-----------
            The image will be stored in binary representation, when we retrieve that image from the data base,
            We get them as binary array, because at the end, the RGB light in the mobile screen or monitor will display
            this image, we just have to store what is the proportion of that particular pixels in-terms of RGB,
            RGB only requires 0 to 255 values, which can be created using bytes. Then we convert that byte[] into
            base64 encoded string, because most of the data transformation supports text format and when we send the
            data as byte[], it might get corrupted in HTTP protocol. The important thing to note here is, after converting
            the byte[] into base64, the size would significantly increase by approximately 33%
         */
    }
}

// if we use @Data, we dont have to explicity specify @Getter, @Setter and few methods over the class :
