package com.project.contactmessage.service;

import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.mapper.ContactMessageMapper;
import com.project.contactmessage.repository.ContactMessageRepository;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
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

    // Not: ************************************* searchByEmailByPage ***************************************

    // Not: *************************************** searchBySubjectByPage ***************************************

    // Not: searchByDateBetween ***************************************

    // Not: searchByTimeBetween ***************************************

    // Not: *********************************** deleteByIdParam ***************************************

    // Not: ***************************************** deleteById ***************************************

    // Not: *********************************** getByIdWithParam ***************************************

    // Not: ************************************ getByIdWithPath ***************************************


}