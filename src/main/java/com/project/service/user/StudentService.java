package com.project.service.user;

import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.StudentRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.StudentResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;

    public ResponseMessage<StudentResponse> saveStudent(StudentRequest studentRequest) {

        User advisoryTeacher = methodHelper.isUserExist(studentRequest.getAdvisorTeacherId());
        methodHelper.checkAdvisor(advisoryTeacher);
        uniquePropertyValidator.checkDuplicate(studentRequest.getUsername(),
                studentRequest.getSsn(), studentRequest.getPhoneNumber(), studentRequest.getEmail());

        User student = userMapper.mapStudentRequestToUser(studentRequest);
        student.setAdvisorTeacherId(advisoryTeacher.getId());
        student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        student.setUserRole(userRoleService.getUserRole(RoleType.STUDENT));
        student.setActive(true);
        student.setIsAdvisor(Boolean.FALSE);

        student.setStudentNumber(getLastNumber());

        return ResponseMessage.<StudentResponse>builder()
                .object(userMapper.mapUserToStudentResponse(userRepository.save(student)))
                .message(SuccessMessages.STUDENT_SAVE)
                .build();
    }

    public ResponseMessage<StudentResponse> updateStudentForStudents(StudentRequest studentRequest, HttpServletRequest httpServletRequest) {

        String userName = (String) httpServletRequest.getAttribute("username");
        methodHelper.isUserExistByUsername(userName);

        User student = userRepository.findByUsername(userName);

       if (!studentRequest.getUsername().equalsIgnoreCase(userName))
       {
           throw new RuntimeException();
       }

        // !!! built_in kontrolu built in öğrenci olur mu ki ?
        methodHelper.checkBuiltIn(student);

        // !!! unique kontrolu
        uniquePropertyValidator.checkUniqueProperties(student, studentRequest);
        //DTO TO POJO
        User updatedStudent = userMapper.mapStudentRequestToUser(studentRequest);
        updatedStudent.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
  //      updatedStudent.setPassword(passwordEncoder.encode(studentRequest.getPassword()));

        userRepository.save(updatedStudent);

        String message = SuccessMessages.STUDENT_UPDATE;

        return ResponseMessage.<StudentResponse>builder().message(message).httpStatus(HttpStatus.OK).object(userMapper.mapUserToStudentResponse(updatedStudent)).build();

    }

    public ResponseMessage<StudentResponse> updateStudent(StudentRequest studentRequest, Long studentId) {

        User student = methodHelper.isUserExist(studentId);

        // !!! bulit_in kontrolu
        methodHelper.checkBuiltIn(student);
        //!!! update isleminde gelen request de unique olmasi gereken eski datalar hic degismedi ise
        // dublicate kontrolu yapmaya gerek yok :
        uniquePropertyValidator.checkUniqueProperties(student, studentRequest);

        //DTO TO POJO
        User updatedStudent = userMapper.mapStudentRequestToUser(studentRequest);

        updatedStudent.setPassword(passwordEncoder.encode(studentRequest.getPassword()));

        userRepository.save(updatedStudent);

        String message = SuccessMessages.USER_UPDATE;

        return ResponseMessage.<StudentResponse>builder().message(message).httpStatus(HttpStatus.OK).object(userMapper.mapUserToStudentResponse(updatedStudent)).build();

    }

    public ResponseMessage<StudentResponse> changeActiveStatusOfStudent(Long studentId) {
       User student = methodHelper.isUserExist(studentId);

        student.setActive(!student.isActive());

        return ResponseMessage.<StudentResponse>builder().message("students activity updated successfully")
               .httpStatus(HttpStatus.OK)
               .object(userMapper.mapUserToStudentResponse(student)).build();
    }

    private int getLastNumber(){
        //DB de hıc ogrencı yoksa  ogrencı numarası olarak 1000 gonderıyoruz
        if(!userRepository.findStudent(RoleType.STUDENT)){
            return 1000;
        }

        return userRepository.getMaxStudentNumber() + 1 ;
    }



}
