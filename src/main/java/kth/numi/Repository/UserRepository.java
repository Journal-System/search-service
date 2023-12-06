package kth.numi.Repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import kth.numi.model.User;
import kth.numi.roles.UserRole;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public List<User> findPatientByName(String name) {
        return stream("firstname", name)
                .filter(user -> user.getUserRole() == UserRole.PATIENT)
                .collect(Collectors.toList());
    }

    public List<User> findPatientsByPatientIds(List<Integer> patientIds) {
        return
                stream("id IN (?1)", patientIds)
                .collect(Collectors.toList());
    }

    public Integer findDoctorId(String name) {
        return stream("firstname", name)
                .filter(user -> user.getUserRole() == UserRole.DOCTOR)
                .findFirst()
                .map(User::getId)
                .orElse(null);
    }
}
