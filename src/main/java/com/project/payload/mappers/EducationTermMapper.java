package com.project.payload.mappers;

import com.project.entity.concretes.business.EducationTerm;
import com.project.payload.request.business.EducationTermRequest;
import org.springframework.stereotype.Component;

@Component
public class EducationTermMapper {

    public EducationTerm mapEducationTermRequestToEducationTerm (EducationTermRequest educationTermRequest){

       return EducationTerm.builder().term(educationTermRequest.getTerm())
                .endDate(educationTermRequest.getEndDate())
                .startDate(educationTermRequest.getStartDate())
                .lastRegistrationDate(educationTermRequest.getLastRegistrationDate()).build();

    }

}
