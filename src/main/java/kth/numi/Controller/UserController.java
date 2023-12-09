package kth.numi.Controller;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import kth.numi.model.Condition;
import kth.numi.model.User;
import kth.numi.roles.Role;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/v1")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @GET
    @Path("/patients/name/{name}")
    public Uni<List<User>> getByName(String name) {
        return User.find("firstname", name).list();
    }

    @GET
    @Path("/patients/condition/{condition}")
    public Uni<List<?>> getByCondition(String condition) {
        // Find patients_id based on the given condition
        Uni<List<Condition>> patientsIds = Condition.find("condition", condition).list();

        return patientsIds
                .onItem().ifNotNull().transformToUni(patients -> {
                    if (!patients.isEmpty()) {
                        // Extract patients_ids as a list of Integers
                        List<Integer> patientIdList = patients.stream()
                                .map(Condition::getPatientId)
                                .collect(Collectors.toList());

                        // Find users with matching users_ids and user_role
                        return User.find("id IN ?1 and role = ?2", patientIdList, Role.PATIENT)
                                .list();
                    } else {
                        // Return an empty list if no patients found
                        return Uni.createFrom().item(Collections.emptyList());
                    }
                })
                .onItem().ifNull().continueWith(Collections.emptyList());
    }

//    @GET
//    @Path("/patients/condition/{condition}")
//    public List<User> getByCondition(@PathParam("condition") String condition) {
//        List<Integer> patientIds = conditionRepository.findPatientIdsByCondition(condition);
//        if (!patientIds.isEmpty()) {
//            return userRepository.findPatientsByPatientIds(patientIds);
//        }
//        return null;
//    }

//    @GET
//    @Path("/doctor/patients/{name}")
//    public List<User> getPatientsByDoctorName(@PathParam("name") String name) {
//        Integer doctorId = userRepository.findDoctorId(name);
//        if (doctorId != null) {
//            List<Integer> patientsIds = encounterRepository.findPatientsIdsByDoctorId(doctorId);
//            if (patientsIds != null) {
//                return userRepository.findPatientsByPatientIds(patientsIds);
//            }
//        }
//
//        return null;
//    }

//    @GET
//    @Path("/doctor/encounters/{name}")
//    public List<Encounter> getEncountersByDoctorName(@PathParam("name") String name) {
//        Integer doctorId = userRepository.findDoctorId(name);
//        if (doctorId != null) {
//            return encounterRepository.findEncountersTodayByDoctorId(doctorId);
//        }
//
//        return null;
//    }
}
