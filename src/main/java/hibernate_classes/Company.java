package hibernate_classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company", schema = "public")
public class Company {
    @Id
    private Long id;
    private String name;
    @Column(name = "founding_date")
    private Date foundingDate;

}
