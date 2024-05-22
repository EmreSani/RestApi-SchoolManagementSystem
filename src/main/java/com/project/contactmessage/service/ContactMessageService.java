package com.project.contactmessage.service;

import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.mapper.ContactMessageMapper;
import com.project.contactmessage.repository.ContactMessageRepository;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper createContactMessage;

    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {

        ContactMessage contactMessage = createContactMessage.requestToContactMessage(contactMessageRequest);
        ContactMessage savedContactMessage = contactMessageRepository.save(contactMessage);
        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Created Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(createContactMessage.contactMessageToResponse(savedContactMessage))
                .build();
    }

    // Not: ******************************************** getAllByPage ***************************************
    public Page<ContactMessageResponse> getAllByPage(int page, int size, String prop, Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

       return contactMessageRepository.findAll(pageable).map(createContactMessage::contactMessageToResponse);

    }

    // Not: ************************************* searchByEmailByPage ***************************************
    public Page<ContactMessageResponse> searchByEmail(String email, int page, int size, String prop, Sort.Direction direction) {
    Pageable pageable = PageRequest.of(page,size, Sort.by(direction, prop));

    return contactMessageRepository.findByEmailEquals(email, pageable).map(createContactMessage::contactMessageToResponse);
    }

    // Not: *************************************** searchBySubjectByPage ***************************************
    public Page<ContactMessageResponse> searchBySubject(String subject, int page, int size, String prop, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        return contactMessageRepository.findBySubjectEquals(subject, pageable).map(createContactMessage::contactMessageToResponse);
    }



    // Not: searchByDateBetween ***************************************

    // Not: searchByTimeBetween ***************************************

    //findContactById methodu oluşturup bu methodu silme işlemleri için kullanabiliriz
    public ContactMessage findContactMessage(Long id){

        return contactMessageRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Contact Message not found with id: "+ id));

    }

    public ResponseMessage<ContactMessageResponse> ContactMessageToResponse(Long id){

     ContactMessageResponse contactMessageResponse = createContactMessage.contactMessageToResponse(findContactMessage(id));

     return ResponseMessage.<ContactMessageResponse>builder()
             .message("Contact message found successfully")
             .httpStatus(HttpStatus.OK)
             .object(contactMessageResponse).build();

    }

    // Not: *********************************** deleteByIdParam ***************************************
    public void deleteById(Long id) {
        ContactMessage contactMessage = findContactMessage(id);
        contactMessageRepository.deleteById(id); //böylece silmeye çalıştığımız contact message objesinin null olup olmaması kontrolleri sağlanmış oldu.
    }

    public ResponseMessage<ContactMessageResponse> deleteByIdPath(Long id) {
        ContactMessage contactMessage = findContactMessage(id);
        contactMessageRepository.deleteById(id);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact message deleted succesfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Not: ***************************************** deleteById ***************************************


    // Not: *********************************** getByIdWithParam ***************************************

    // Not: ************************************ getByIdWithPath ***************************************


}