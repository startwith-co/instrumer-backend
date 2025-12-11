package instrumers.backend.solution.image.model;

import instrumers.backend.base.BaseTimeEntity;
import instrumers.backend.solution.image.util.SolutionImageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SOLUTION_IMAGE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class SolutionImageEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solution_image_seq")
    private Long solutionImageSeq;

    @Column(name = "solution_image_url", nullable = false, unique = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "solution_image_type", nullable = false, unique = false)
    private SolutionImageType solutionImageType;
}
