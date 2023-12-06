package kth.numi.Repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import kth.numi.model.Condition;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ConditionRepository implements PanacheRepository<Condition> {

    public List<Integer> findPatientIdsByCondition(String condition) {
        PanacheQuery<Condition> query = find("`condition`", condition);
        List<Condition> conditions = query.list();
        return conditions.stream()
                .map(Condition::getPatientId)
                .collect(Collectors.toList());
    }
}
