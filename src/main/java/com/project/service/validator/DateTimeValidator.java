package com.project.service.validator;

import com.project.entity.concretes.business.LessonProgram;
import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DateTimeValidator {

    public boolean checkTime(LocalTime start, LocalTime stop){
        return start.isAfter(stop) || start.equals(stop) ;
    }

    public void checkTimeWithException(LocalTime start, LocalTime stop){
        if(checkTime(start,stop)){
            throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }
    }

    private void checkDuplicateLessonPrograms(Set<LessonProgram> lessonPrograms){
        //!!! bu metodda request ile gelen lesson programlarda cakisma kontrolu var mi kontrolu yapiliyor :
        Set<String> uniqueLessonProgramDays = new HashSet<>();
        Set<LocalTime> existingLessonProgramStartTimes = new HashSet<>();
        Set<LocalTime> existingLessonProgramStopTimes = new HashSet<>();

        for (LessonProgram lessonProgram : lessonPrograms) {
            String lessonProgramDay = lessonProgram.getDay().name() ;

            // !!! Karsilastirilan LessonProgramlar Ayni Gunde ise
            if (uniqueLessonProgramDays.contains(lessonProgramDay)) {
                // !!! Baslama saatine gore kontrol
                for (LocalTime startTime : existingLessonProgramStartTimes) {
                    // !!! Baslama saatleri esit ise :
                    if (lessonProgram.getStartTime().equals(startTime)) {
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                    // !!! mevcut ders programının başlangıç saati ile diğer bir ders programının başlangıç ve bitiş
                     //  saatleri arasında çakışma olduğunda hata fırlatır.
                    if (lessonProgram.getStartTime().isBefore(startTime) && lessonProgram.getStopTime().isAfter(startTime)) {
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                }
                // !!! Bitis saatine gore kontrol
                for (LocalTime stopTime : existingLessonProgramStopTimes) {
                    if (lessonProgram.getStartTime().isBefore(stopTime) && lessonProgram.getStopTime().isAfter(stopTime)) {
                        throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
                    }
                }


            }

            //!!! yukardaki Baslangic ve Biris saatine gore kontrollerden gecen LessonProgram, metodun en basinda olusturulan
             // unique degiskenlere ataniyor
            uniqueLessonProgramDays.add(lessonProgramDay);
            existingLessonProgramStartTimes.add(lessonProgram.getStartTime());
            existingLessonProgramStopTimes.add(lessonProgram.getStopTime());
        }
    }

    // !!!  mevcut ders programları ile talep edilen ders programları arasında karşılaştırma
    private void checkDuplicateLessonPrograms (Set<LessonProgram> existLessonProgram,
                                               Set<LessonProgram> lessonProgramRequest){
        for (LessonProgram requestLessonProgram : lessonProgramRequest) {
            String requestLessonProgramDay = requestLessonProgram.getDay().name();
            LocalTime requestStart = requestLessonProgram.getStartTime();
            LocalTime requestStop = requestLessonProgram.getStopTime();

            // Check for any match where the LessonProgram's start or stop time is within existing LessonPrograms
            if (existLessonProgram.stream()
                    .anyMatch(lessonProgram ->
                            lessonProgram.getDay().name().equals(requestLessonProgramDay)
                                    && (lessonProgram.getStartTime().equals(requestStart) // lp1(sali 09:00) / lp2(sali 09:00)
                                    || (lessonProgram.getStartTime().isBefore(requestStart) && lessonProgram.getStopTime().isAfter(requestStart)) // lp1( Sali 09:00 - 11:00) / lp2 ( Sali 10:00- 12:00)
                                    || (lessonProgram.getStartTime().isBefore(requestStop) && lessonProgram.getStopTime().isAfter(requestStop))
                                    || (lessonProgram.getStartTime().isAfter(requestStart) && lessonProgram.getStopTime().isBefore(requestStop))))) {
                throw new BadRequestException(ErrorMessages.LESSON_PROGRAM_ALREADY_EXIST);
            }
        }
    }

    //!!! Yukardaki 2 metodu kullanan Ana metod
    public void checkLessonPrograms(Set<LessonProgram> existLessonProgram,
                                    Set<LessonProgram> lessonProgramRequest){

        // !!! mevcut ders programi bos ise ve requestten gelen ders programi doluysa
        if(existLessonProgram.isEmpty() && lessonProgramRequest.size()>1){
            // !!! talep edilen icinde cakisma var mi
            checkDuplicateLessonPrograms(lessonProgramRequest);
        } else {
            // !!! talep edilen icinde cakisma var mi
            checkDuplicateLessonPrograms(lessonProgramRequest);
            // !!! talep edilen ile mevcutta cakisma var mi
            checkDuplicateLessonPrograms(existLessonProgram,lessonProgramRequest);
        }
    }
}
