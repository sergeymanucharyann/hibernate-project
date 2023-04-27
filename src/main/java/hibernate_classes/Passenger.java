package hibernate_classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "passenger")
public class Passenger {
    @Id
    private Long id;
    private String name;
    private String phone;
    private String country;
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id",referencedColumnName = "id",nullable = false)
    private Address address;
    @ManyToMany
    @JoinTable(
            name = "pass_in_trip",
            joinColumns = {@JoinColumn(name = "psg_id")},
            inverseJoinColumns = {@JoinColumn(name = "trip_id")}
    )
    private List<Trip> trip = new ArrayList<Trip>();
}
