package com.project.service.business;

import com.project.entity.concretes.business.Meet;
import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.MeetMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.MeetRequest;
import com.project.payload.response.business.MeetResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.MeetRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.user.UserService;
import com.project.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetService {

    private final MeetRepository meetRepository;
    private final MeetMapper meetMapper;
    private final MethodHelper methodHelper;
    private final DateTimeValidator dateTimeValidator;
    private final UserService userService;


    public List<MeetResponse> getAll() {

        return meetRepository.findAll()
                .stream()
                .map(meetMapper::mapMeetToMeetResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<MeetResponse> getMeetById(Long meetId) {

        return ResponseMessage.<MeetResponse>builder()
                .message(SuccessMessages.MEET_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(meetMapper.mapMeetToMeetResponse(isMeetExistById(meetId)))
                .build();

    }

    private Meet isMeetExistById(Long meetId){
        return  meetRepository.findById(meetId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.MEET_NOT_FOUND_MESSAGE, meetId)));
    }

    public ResponseMessage<MeetResponse> saveMeet(HttpServletRequest httpServletRequest,
                                                  MeetRequest meetRequest){
        String username = (String) httpServletRequest.getAttribute("username");
        User advisoryTeacher = methodHelper.isUserExistByUsername(username);
        // istegi ileten Teacher Advisor mi kontrolu :
        methodHelper.checkAdvisor(advisoryTeacher);

        // !!! Yeni Meet saatlerınde cakısma var mı kontrolu
        dateTimeValidator.checkTimeWithException(meetRequest.getStartTime(),
                meetRequest.getStopTime());
        // !!! Teacher in eski meeetleri ile cakisma var mi
        checkMeetConflict(advisoryTeacher.getId(),
                meetRequest.getDate(),
                meetRequest.getStartTime(),
                meetRequest.getStopTime());

        // !!! Student ıcın meet saatlerınde cakısma var mı kontrolu
        for (Long studentId : meetRequest.getStudentIds()){
            User student = methodHelper.isUserExist(studentId);
            // Student mi kontrolu :
            methodHelper.checkRole(student, RoleType.STUDENT);

            // ogrencinin daha onceki meetleri ile cakisma var mi kontrolu :
            checkMeetConflict(studentId,
                    meetRequest.getDate(),
                    meetRequest.getStartTime(),
                    meetRequest.getStopTime());
        }
        // !!! Meet e katılacak ogrencıler getırılıyor
        List<User> students = userService.getStudentById(meetRequest.getStudentIds());
        // !!! DTO --> POJO
        Meet meet = meetMapper.mapMeetRequestToMeet(meetRequest);
        meet.setStudentList(students);
        meet.setAdvisoryTeacher(advisoryTeacher);
        Meet savedMeet = meetRepository.save(meet);
        // !!! ogrencilerin mail adreslerine mail gonderiliyor
        //sendMailToService(savedMeet, "created a new meeting", students);

        return ResponseMessage.<MeetResponse>builder()
                .message(SuccessMessages.MEET_SAVE)
                .object(meetMapper.mapMeetToMeetResponse(savedMeet))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // bu metod hem techer hemde student icin calisacak
    private void checkMeetConflict(Long userId, LocalDate date, LocalTime startTime, LocalTime stopTime){
        List<Meet>meets;

        // !!! Student veya Teacher a aıt olan mevcut Meet ler getırılıyor
        if(Boolean.TRUE.equals(methodHelper.isUserExist(userId).getIsAdvisor())) {
            meets = meetRepository.getByAdvisoryTeacher_IdEquals(userId); // Derived
        } else meets = meetRepository.findByStudentList_IdEquals(userId); // Derived

        // !!! cakısma kontrolu
        for (Meet meet :meets){
            LocalTime existingStartTime = meet.getStartTime();
            LocalTime existingStopTime = meet.getStopTime();

            if(meet.getDate().equals(date) &&
                    (
                            (startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime)) ||
                                    (stopTime.isAfter(existingStartTime) && stopTime.isBefore(existingStopTime)) ||
                                    (startTime.isBefore(existingStartTime) && stopTime.isAfter(existingStopTime)) ||
                                    (startTime.equals(existingStartTime) || stopTime.equals(existingStopTime))
                    )
            ){
                throw new ConflictException(ErrorMessages.MEET_HOURS_CONFLICT);
            }
        }
    }
}
