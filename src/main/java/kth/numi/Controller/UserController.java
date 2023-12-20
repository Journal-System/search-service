package kth.numi.Controller;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import kth.numi.model.Condition;
import kth.numi.model.Encounter;
import kth.numi.model.User;
import kth.numi.roles.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/v1")
public class UserController {
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

    @GET
    @Path("/doctor/patients/{name}")
    public Uni<List<?>> getPatientsByDoctorName(String name) {
        Uni<User> doctor = User.find("firstname = ?1 and role = ?2", name, Role.DOCTOR)
                .firstResult();

        Uni<Integer> doctorId = doctor
                .onItem().ifNotNull().transform(User::getId)
                .onItem().ifNull().continueWith(-1);

        return doctorId
                .onItem().ifNotNull().transformToUni(id -> {
                    if (id != null) {
                        Uni<List<Encounter>> patientId = Encounter
                                .find("select patientId from Encounter where doctorId = ?1", id)
                                .list();
                        return patientId
                                .onItem().ifNotNull().transformToUni(pId -> {
                                    List<Encounter> patientID = new ArrayList<>(pId);

                                    if (patientID != null) {
                                        return User.find("id IN ?1", patientID).list();
                                    }
                                    return Uni.createFrom().item(Collections.emptyList());
                                })
                                .onItem().ifNull().continueWith(Collections.emptyList());

                    }
                    return Uni.createFrom().item(Collections.emptyList());
                })
                .onItem().ifNull().continueWith(Collections.emptyList());
    }

    @GET
    @Path("/doctor/encounters/{name}")
    public Uni<List<?>> getEncounterByDoctorName(String name) {
        Uni<User> doctor = User.find("firstname = ?1 and role = ?2", name, Role.DOCTOR)
                .firstResult();

        Uni<Integer> doctorId = doctor
                .onItem().ifNotNull().transform(User::getId)
                .onItem().ifNull().continueWith(-1);

        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atTime(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX);

        return doctorId
                .onItem().ifNotNull().transformToUni(id -> {
                    if (id != null) {
                        return Encounter.find("doctorId = ?1 and timestamp >= ?2 and timestamp <= ?3", id, startOfDay, endOfDay)
                                .list();
                    }
                    return Uni.createFrom().item(Collections.emptyList());
                })
                .onItem().ifNull().continueWith(Collections.emptyList());
    }
}