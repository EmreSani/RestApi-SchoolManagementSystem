package com.project.controller.user;

import com.project.payload.request.user.StudentRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/save") // http://localhost:8080/student/save + JSON + POST
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<StudentResponse>> saveStudent(
            @RequestBody @Valid StudentRequest studentRequest){
        return ResponseEntity.ok(studentService.saveStudent(studentRequest));
    }

    // Not: updateStudentForStudents() ***********************************************
    // !!! ogrencinin kendisini update etme islemi
    @PutMapping("/updateStudentSelf")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseMessage<StudentResponse> updateStudentForStudents(@RequestBody @Valid StudentRequest studentRequest, HttpServletRequest httpServletRequest){
        return studentService.updateStudentForStudents(studentRequest, httpServletRequest);
    }

    // Not: updateStudent() **********************************************************
    @PutMapping("/updateStudent")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public ResponseMessage<StudentResponse> updateStudent(@RequestBody @Valid StudentRequest studentRequest, Long studentId){
        return studentService.updateStudent(studentRequest, studentId);
    }


    // Not: ChangeActıveStatusOfStudent() ********************************************
    @PatchMapping("/changeActiveStatusOfStudent/{studentId}") // http://localhost:8080/teacher/saveAdvisorTeacher/1
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<StudentResponse> ChangeActiveStatusOfStudent (@PathVariable Long studentId){
        return studentService.changeActiveStatusOfStudent(studentId);
    }

    // TODO : LESSON PROGRAM

}
