package hibernate_classes;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trip")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trip_id")
    private int tripId;
    private String airplane;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    private int companyId;
    @Column(name = "town_from")

    private String townFrom;
    @Column(name = "town_to")

    private String townTo;
    @Column(name = "timo_out")

    private Timestamp timeOut;
    @Column(name = "time_in")

    private Timestamp timeIn;


    @ManyToMany(mappedBy = "trip")
    private Set<Passenger> passengers;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "trip_id", nullable = false)
    private Company company;

}
