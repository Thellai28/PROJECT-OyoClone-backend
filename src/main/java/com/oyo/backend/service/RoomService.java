package com.oyo.backend.service;

import com.oyo.backend.exception.InternalServerException;
import com.oyo.backend.exception.ResourceNotFoundException;
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
import java.util.Optional;


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






    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }





    @Override
    public byte[] getRoomPhotoByRoomId( Long roomId ) throws SQLException {
        Optional<Room> theRoom = roomRepository.findById( roomId );
        if( theRoom.isEmpty() ){
            throw new ResourceNotFoundException("Sorry, room not found"); // It's a runtimeException :
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if( photoBlob != null ){
            return photoBlob.getBytes(1, (int)photoBlob.length());
        }
        return new byte[0];
    }





    @Override
    public void deleteRoom( Long roomId ) {
        Optional<Room> selectedRoom = roomRepository.findById( roomId );

        if( selectedRoom.isPresent() ){
            roomRepository.deleteById( roomId );
        }
    }

    @Override
    public Room updateRoom( Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes ) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found") );
        if( roomType != null ) room.setRoomType(roomType);
        if( roomPrice != null )room.setRoomPrice(roomPrice);
        if( photoBytes != null && photoBytes.length > 0){
            try{
                room.setPhoto( new SerialBlob(photoBytes));
            }catch (SQLException e ){
                throw new InternalServerException("Error updating room");
            }
        }
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById( Long roomId ) {
        //return roomRepository.findById(roomId);
        return Optional.of( roomRepository.findById(roomId).get());
    }


}
