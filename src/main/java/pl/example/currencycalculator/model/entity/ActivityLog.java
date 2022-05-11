package pl.example.currencycalculator.model.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityLog {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String apiAddress;
    private String httpMethod;
    private String endpoint;
    private Timestamp timestamp;
    private String statusCode;
}
