package com.project.contactmessage.controller;

import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.service.ContactMessageService;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contactMessages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping("/save") //http://localhost:8080/contactMessages/save + JSON + POST
    public ResponseMessage<ContactMessageResponse> saveContact(@Valid @RequestBody ContactMessageRequest contactMessageRequest) {

        return contactMessageService.save(contactMessageRequest);

    }

    // Not: ******************************************** getAllByPage ***************************************
    @GetMapping("/page")
    public Page<ContactMessageResponse> getAllByPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam("direction") Sort.Direction direction
    ) {
        return contactMessageService.getAllByPage(page, size, prop, direction);
    }

    // Not: ************************************* searchByEmailByPage ***************************************
    @GetMapping("/searchByEmail")
    public Page<ContactMessageResponse> searchByEmail(
            @RequestParam("email") String email,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam("direction") Sort.Direction direction
    ) {
        return contactMessageService.searchByEmail(email, page, size, prop, direction);
    }

    // Not: *************************************** searchBySubjectByPage ***************************************
    @GetMapping("/searchBySubject")
    public Page<ContactMessageResponse> searchBySubject(
            @RequestParam("subject") String subject,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam("direction") Sort.Direction direction
    ) {
        return contactMessageService.searchBySubject(subject, page, size, prop, direction);
    }
    // Not: searchByDateBetween ***************************************
    @GetMapping("/searchByDate")
    public Page<ContactMessageResponse> searchByDateBetween(
            @RequestParam("startDateTime") LocalDateTime startDateTime,
            @RequestParam("endDateTime") LocalDateTime endDateTime,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam("direction") Sort.Direction direction
    ) {
        return contactMessageService.searchByDateBetween(startDateTime, endDateTime, page, size, prop, direction);
    }


    // Not: *********************************** deleteByIdParam ***************************************
    @DeleteMapping("/query")
    public ResponseEntity<Map<String, String>> deleteContactMessageByIdParam(@RequestParam("id") Long id) {

        contactMessageService.deleteById(id);

        Map<String, String> map = new HashMap<>();
        map.put("message", "contactmessage is deleted successfully");
        map.put("status", "true");
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    // Not: ***************************************** deleteById ***************************************
    @DeleteMapping("/{id}")
    public ResponseMessage<ContactMessageResponse> deleteContactMessageById(@PathVariable("id") Long id) {
        return contactMessageService.deleteByIdPath(id);

    }

    // Not: *********************************** getByIdWithParam ***************************************
    @GetMapping("/query")
    public ResponseMessage<ContactMessageResponse> getContactMessageWithParam(@RequestParam("id") Long id) {

        return contactMessageService.ContactMessageToResponse(id);

    }

    // Not: ************************************ getByIdWithPath ***************************************
    @GetMapping("/{id}")
    public ResponseMessage<ContactMessageResponse> getContactMessageWithPathVariable(@PathVariable("id") Long id) {

        return contactMessageService.ContactMessageToResponse(id);

    }

}
