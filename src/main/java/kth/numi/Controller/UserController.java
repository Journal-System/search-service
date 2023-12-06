package kth.numi.Controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import kth.numi.Repository.ConditionRepository;
import kth.numi.Repository.EncounterRepository;
import kth.numi.Repository.UserRepository;
import kth.numi.model.Condition;
import kth.numi.model.Encounter;
import kth.numi.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private UserRepository userRepository;
    private ConditionRepository conditionRepository;
    private EncounterRepository encounterRepository;

    @Inject
    private UserController(UserRepository userRepository, ConditionRepository conditionRepository,
                           EncounterRepository encounterRepository) {
        this.userRepository = userRepository;
        this.conditionRepository = conditionRepository;
        this.encounterRepository = encounterRepository;
    }

    @GET
    @Path("/patients/name/{name}")
    public List<User> getByName(@PathParam("name") String name) {
        return userRepository.findPatientByName(name);
    }

    @GET
    @Path("/patients/condition/{condition}")
    public List<User> getByCondition(@PathParam("condition") String condition) {
        List<Integer> patientIds = conditionRepository.findPatientIdsByCondition(condition);
        if (!patientIds.isEmpty()) {
            return userRepository.findPatientsByPatientIds(patientIds);
        }
        return null;
    }

    @GET
    @Path("/doctor/patients/{name}")
    public List<User> getPatientsByDoctorName(@PathParam("name") String name) {
        Integer doctorId = userRepository.findDoctorId(name);
        if (doctorId != null) {
            List<Integer> patientsIds = encounterRepository.findPatientsIdsByDoctorId(doctorId);
            if (patientsIds != null) {
                return userRepository.findPatientsByPatientIds(patientsIds);
            }
        }

        return null;
    }

    @GET
    @Path("/doctor/encounters/{name}")
    public List<Encounter> getEncountersByDoctorName(@PathParam("name") String name) {
        Integer doctorId = userRepository.findDoctorId(name);
        if (doctorId != null) {
            return encounterRepository.findEncountersTodayByDoctorId(doctorId);
        }

        return null;
    }
}
