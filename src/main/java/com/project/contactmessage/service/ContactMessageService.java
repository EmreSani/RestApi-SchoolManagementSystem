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

import java.time.LocalDateTime;


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
    public Page<ContactMessageResponse> searchByDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int page, int size, String prop, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        return contactMessageRepository.findByDateTimeBetween(startDateTime, endDateTime, pageable).map(createContactMessage::contactMessageToResponse);
    }
    // Bu method zaten tarih ve zamanı aynı alanı kullandığı için `searchByTimeBetween` yerine sadece `searchByDateBetween` yeterli olacaktır.


    //findContactById methodu oluşturup bu methodu silme işlemleri için kullanabiliriz
    public ContactMessage findContactMessage(Long id){

        return contactMessageRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Contact Message not found with id: "+ id));

    }

    // Not: *********************************** deleteByIdParam ***************************************
    public void deleteById(Long id) {
        ContactMessage contactMessage = findContactMessage(id); //böylece silmeye çalıştığımız contact message objesinin null olup olmaması kontrolleri sağlanmış oldu.
        contactMessageRepository.deleteById(id);
    }

    // Not: ***************************************** deleteById ***************************************
    public ResponseMessage<ContactMessageResponse> deleteByIdPath(Long id) {
        ContactMessage contactMessage = findContactMessage(id); //böylece silmeye çalıştığımız contact message objesinin null olup olmaması kontrolleri sağlanmış oldu.
        contactMessageRepository.deleteById(id);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact message deleted succesfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }


    // Not: *********************************** getByIdWithParam ***************************************
    public ResponseMessage<ContactMessageResponse> ContactMessageToResponse(Long id){

        ContactMessageResponse contactMessageResponse = createContactMessage.contactMessageToResponse(findContactMessage(id));

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact message found successfully")
                .httpStatus(HttpStatus.OK)
                .object(contactMessageResponse).build();

    }

    // Not: ************************************ getByIdWithPath ***************************************
    //99. satırdaki methodu 2 kere kullandım. Controllerdaki 2 ayrı istek tek bir methoda yönlendi çünkü zaten işlevleri aynı.
    //Birisi id bilgisini requestparam ile requestten alıyor diğeri ise urldeki variable ile (path variable)

}