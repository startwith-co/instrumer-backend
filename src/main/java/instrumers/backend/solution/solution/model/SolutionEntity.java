package instrumers.backend.solution.solution.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import instrumers.backend.base.BaseTimeEntity;
import instrumers.backend.solution.solution.util.SolutionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SOLUTION")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
@SQLDelete(sql = "UPDATE SOLUTION SET deleted = true WHERE solution_seq = ?")
@Where(clause = "deleted = false")
public class SolutionEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solution_seq")
    private Long solutionSeq;

    @Column(name = "name", nullable = false, unique = false)
    private String name;

    @Column(name = "explanation", nullable = false, unique = false)
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, unique = false)
    private SolutionCategory category;

    @Builder.Default
    @Column(name = "deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean deleted = false;

    @Version
    @Builder.Default
    @Column(name = "version", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer version = 0;
}
