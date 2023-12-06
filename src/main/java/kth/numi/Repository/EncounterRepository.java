package kth.numi.Repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import kth.numi.model.Encounter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EncounterRepository implements PanacheRepository<Encounter> {

    public List<Integer> findPatientsIdsByDoctorId(Integer doctorId) {
        PanacheQuery<Encounter> query = find("doctorId", doctorId);
        List<Encounter> encounters = query.list();
        return encounters.stream()
                .map(Encounter::getPatientId)
                .collect(Collectors.toList());
    }

    public List<Encounter> findEncountersTodayByDoctorId(Integer doctorId) {
        LocalDate today = LocalDate.now();
        LocalDate localDateTime = LocalDateTime.now().toLocalDate();
        System.out.println("HEREEEEE: " + localDateTime.equals(today));

        return find("doctorId", doctorId)
                .stream()
                .filter(encounter -> encounter.getTimestamp().toLocalDate().equals(today))
                .collect(Collectors.toList());
    }
}
