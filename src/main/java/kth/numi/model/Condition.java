package kth.numi.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`condition`")
@Entity
@Cacheable
public class Condition extends PanacheEntityBase {

    @Id
    @Column(name = "`condition_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int conditionId;

    @Column(name = "`condition`", nullable = false)
    private String condition;

    @Column(name = "description")
    private String description;

    @Column(name = "patient_Id")
    private Integer patientId;
}
