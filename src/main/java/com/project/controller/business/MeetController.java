package com.project.controller.business;

import com.project.payload.request.business.MeetRequest;
import com.project.payload.response.business.MeetResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetController {

    private final MeetService meetService;

    // Not : ( ODEVV ) save *******************************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/save") // http://localhost:8080/meet/save
    public ResponseMessage<MeetResponse>saveMeet(HttpServletRequest httpServletRequest,
                                                 @RequestBody @Valid MeetRequest meetRequest){
        return meetService.saveMeet(httpServletRequest,meetRequest);
    }

    // Not: ( ODEVV ) update ******************************************
    @PreAuthorize("hasAnyAuthority('TEACHER','ADMIN' )")
    @PutMapping("/update/{meetId}")  // http://localhost:8080/meet/update/1
    public ResponseMessage<MeetResponse>updateMeet(@RequestBody @Valid MeetRequest meetRequest,
                                                   @PathVariable Long meetId,
                                                   HttpServletRequest httpServletRequest){
        return meetService.updateMeet(meetRequest,meetId, httpServletRequest);
    }

    @PreAuthorize("hasAnyAuthority( 'ADMIN')")
    @GetMapping("/getAll") // http://localhost:8080/meet/getAll
    public List<MeetResponse> getAll() {
        return meetService.getAll();
    }

    @PreAuthorize("hasAnyAuthority( 'ADMIN')")
    @GetMapping("/getMeetById/{meetId}")  // http://localhost:8080/meet/getMeetById/1
    public ResponseMessage<MeetResponse> getMeetById(@PathVariable Long meetId){
        return meetService.getMeetById(meetId);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER','ADMIN')")
    @DeleteMapping("/delete/{meetId}") // http://localhost:8080/meet/2
    public ResponseMessage delete(@PathVariable Long meetId, HttpServletRequest httpServletRequest){
        return meetService.delete(meetId, httpServletRequest);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllMeetByAdvisorTeacherAsList") // http://localhost:8080/meet/getAllMeetByAdvisorTeacherAsList
    public ResponseEntity<List<MeetResponse>> getAllByTeacher(HttpServletRequest httpServletRequest){

        return meetService.getAllByTeacher(httpServletRequest);
    }

    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllMeetByStudent") // http://localhost:8080/meet/getAllMeetByStudent
    public ResponseEntity<List<MeetResponse>> getAllByStudent(HttpServletRequest httpServletRequest){

        return meetService.getAllByStudent(httpServletRequest);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/getAllMeetByPage") // http://localhost:8080/meet/getAllMeetByPage
    public Page<MeetResponse> getAllMeetByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ){
        return meetService.getAllMeetByPage(page,size);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllMeetByAdvisorTeacherAsPage") // http://localhost:8080/meet/getAllMeetByAdvisorTeacherAsPage
    public ResponseEntity<Page<MeetResponse>> getAllMeetByTeacher(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ){
        return meetService.getAllMeetByTeacher(httpServletRequest,page,size);
    }






}
