package com.oyo.backend.service;

import com.oyo.backend.model.Room;
import com.oyo.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService{
    private final RoomRepository roomRepository;
    @Override
    public Room addNewRoom( MultipartFile file, String roomType, BigDecimal roomPrice ) throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType( roomType );
        room.setRoomPrice( roomPrice );

        if( !file.isEmpty() ){
            byte[] photoByte = file.getBytes(); // Throws IO expception
            Blob photoBlob = new SerialBlob( photoByte ); // Throws SQLException
            room.setPhoto( photoBlob );
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }
}
