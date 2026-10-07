package com.example.rafeeq.Service;

import com.example.rafeeq.Api.ApiException;
import com.example.rafeeq.DTO.PlanReviewStatusDTO;
import com.example.rafeeq.Model.ExercisePlan;
//import com.example.capston3.Model.User;
import com.example.rafeeq.Model.User;
import com.example.rafeeq.Model.VitalSign;
import com.example.rafeeq.Repository.ExercisePlanRepository;
//import com.example.capston3.Repository.UserRepository;
import com.example.rafeeq.Repository.UserRepository;
import com.example.rafeeq.Repository.VitalSignRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;



import java.util.List;

@Service
@RequiredArgsConstructor
public class ExercisePlanService {

    private final ExercisePlanRepository exercisePlanRepository;
      private final UserRepository userRepository;
    private final VitalSignRepository vitalSignRepository;
    private final JavaMailSender mailSender;
    public List<ExercisePlan> getAllExercisePlans() {
        return exercisePlanRepository.findAll();
    }


    public void addExercisePlan(Integer userId, ExercisePlan exercisePlan) {

      User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        ExercisePlan oldPlan = exercisePlanRepository.findExercisePlanByUserId(userId);

        if (oldPlan != null) {
            throw new ApiException("User already has an exercise plan");
        }

        exercisePlan.setUser(user);

        exercisePlanRepository.save(exercisePlan);
    }


    public void updateExercisePlan(Integer id, ExercisePlan exercisePlan) {

        ExercisePlan oldPlan = exercisePlanRepository.findExercisePlanById(id);

        if (oldPlan == null) {
            throw new ApiException("Exercise plan not found");
        }

        oldPlan.setGoal(exercisePlan.getGoal());
        oldPlan.setExercises(exercisePlan.getExercises());
        oldPlan.setSummary(exercisePlan.getSummary());

        exercisePlanRepository.save(oldPlan);
    }


    public void deleteExercisePlan(Integer id) {

        ExercisePlan exercisePlan = exercisePlanRepository.findExercisePlanById(id);

        if (exercisePlan == null) {
            throw new ApiException("Exercise plan not found");
        }

        exercisePlanRepository.delete(exercisePlan);
    }


    public ExercisePlan getExercisePlanByUserId(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        ExercisePlan exercisePlan = exercisePlanRepository.findExercisePlanByUserId(userId);

        if (exercisePlan == null) {
            throw new ApiException("Exercise plan not found");
        }

        return exercisePlan;
    }
    public PlanReviewStatusDTO getExercisePlanReviewStatus(Integer userId){

        User user=userRepository.findUserById(userId);

        if(user==null){
            throw new ApiException("User not found");
        }

        ExercisePlan exercisePlan=exercisePlanRepository.findExercisePlanByUserId(userId);

        if(exercisePlan==null){
            throw new ApiException("Exercise plan not found");
        }

        List<VitalSign> vitalSigns=
                vitalSignRepository.findByUserIdOrderByMeasuredAtDesc(userId);

        for(VitalSign vitalSign:vitalSigns){

            if(vitalSign.getMeasuredAt().isAfter(exercisePlan.getUpdatedAt())
                    && ("HIGH".equals(vitalSign.getFlag())
                    || "CRITICAL".equals(vitalSign.getFlag()))){

                String reason=
                        "تم تسجيل قراءة "
                                + vitalSign.getType()
                                + " بحالة "
                                + vitalSign.getFlag()
                                + " بعد آخر تحديث لخطة التمارين";

                try {

                    MimeMessage message=mailSender.createMimeMessage();

                    MimeMessageHelper helper=
                            new MimeMessageHelper(message,true,"UTF-8");

                    helper.setTo(user.getEmail());

                    helper.setSubject(
                            "رفيق | تنبيه لمراجعة خطة التمارين"
                    );

                    String body=
                            "مرحباً " + user.getFullName() + "،<br><br>"
                                    + "تم تسجيل قراءة صحية جديدة تشير إلى أن خطة التمارين الحالية تحتاج إلى مراجعة.<br><br>"
                                    + "<strong>سبب التنبيه:</strong><br>"
                                    + reason
                                    + "<br><br>"
                                    + "يفضل مراجعة خطة التمارين قبل الاستمرار عليها والتأكد من ملاءمتها لحالتك الصحية الحالية.<br><br>"
                                    + "<small>هذا التنبيه للتوعية العامة ولا يعتبر بديلاً عن استشارة الطبيب.</small>";

                    helper.setText(body,true);

                    mailSender.send(message);

                } catch(Exception e){
                    throw new ApiException("Failed to send exercise plan review email");
                }

                return new PlanReviewStatusDTO(
                        true,
                        reason
                );
            }
        }

        return new PlanReviewStatusDTO(
                false,
                "خطة التمارين محدثة ولا تحتاج إلى مراجعة"
        );
    }
}