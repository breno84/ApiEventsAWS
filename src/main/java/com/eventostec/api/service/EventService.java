package com.eventostec.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(EventRequestDTO data){
        String imgUrl = null;

        if(data.image() != null){
            imgUrl = this.uploadImg(data.image());
        }
        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

        eventRepository.save(newEvent);
        return newEvent;
    }

    private String uploadImg(MultipartFile multipartFile){
        String fileName= UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        try {
            File file = this.converMultipartToFile(multipartFile);
            s3Client.putObject(bucketName,fileName, file);
            file.delete();
            System.out.println("subiu arquivo");
            return s3Client.getUrl(bucketName,fileName).toString();
        } catch (Exception e) {
            System.out.println("erro ao subir arquivo");
            e.printStackTrace();
            return null;
        }

    }
    private File converMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public List<EventResponseDTO> getEvents(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Event> eventPage = this.eventRepository.findAll(pageable);
        return eventPage.map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                "",
                "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl())
        ).toList();
    }

}
