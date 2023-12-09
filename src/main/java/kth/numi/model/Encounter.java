package kth.numi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Encounter")
@Entity
public class Encounter extends PanacheEntityBase {

    @Id
    @Column(name = "encounter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer encounterId;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "Timestamp", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "`patient_id`")
    private Integer patientId;

    @Column(name = "`doctorId`")
    private Integer doctorId;

    @Column(name = "`observation_id`")
    private Integer observationId;
}
