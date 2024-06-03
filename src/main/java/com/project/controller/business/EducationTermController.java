package com.project.controller.business;

import com.project.payload.request.business.EducationTermRequest;
import com.project.payload.response.business.EducationTermResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.EducationTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/educationTerms")
@RequiredArgsConstructor
public class EducationTermController {

    private final EducationTermService educationTermService;

    // Not: ODEVV save() *******************************************************************************
    @PostMapping("/save")// http://localhost:8080/educationTerms/save + JSON + POST
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<EducationTermResponse>saveEducationTerm(@RequestBody @Valid
                                                                   EducationTermRequest educationTermRequest){
        return educationTermService.saveEducationTerm(educationTermRequest);
    }

    // Not: getById() ****************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    @GetMapping("/{id}") // http://localhost:8080/educationTerms/1 + GET
    public EducationTermResponse getEducationTermById(@PathVariable Long id){
        return educationTermService.getEducationTermById(id);
    }
    // Not: getAll ************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    @GetMapping("/getAll") // hhtp://localhost:8080/educationTerms/getAll  + GET
    public List<EducationTermResponse> getAllEducationTerms(){
        return educationTermService.getAllEducationTerms();
    }

    //Not: getAllWithPage *****************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    @GetMapping("/getAllEducationTermsByPage") // hhtp://localhost:8080/educationTerms/getAllEducationTermsByPage
    public Page<EducationTermResponse> getAllEducationTermsByPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "startDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return educationTermService.getAllEducationTermsByPage(page,size,sort,type);
    }

    // Not:ODEVVV deleteById ********************************
    @PostMapping("/delete/{educationTermId}")// http://localhost:8080/educationTerms/delete + JSON + POST
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<String> deleteEducationTermById(@PathVariable Long educationTermId){

       return educationTermService.deleteEducationTermById(educationTermId);

    }

    //Not: ODEVVV UpdateById *********************************
    @PostMapping("/update/{educationTermId}")// http://localhost:8080/educationTerms/update + JSON + POST
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<EducationTermResponse> updateById(@RequestBody @Valid EducationTermRequest educationTermRequest ,@PathVariable Long educationTermId){

        return educationTermService.updateEducationTermById(educationTermId, educationTermRequest);

    }

}
