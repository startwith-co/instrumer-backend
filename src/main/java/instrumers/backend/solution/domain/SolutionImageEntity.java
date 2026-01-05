package instrumers.backend.solution.domain;

import instrumers.backend.base.BaseTimeEntity;
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

    @Column(name = "solution_image_url", nullable = false, unique = false, length = 1024)
    private String imageUrl;

    @Column(name = "image_type", nullable = false, unique = false)
    private String imageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_seq", nullable = false)
    private SolutionEntity solutionEntity;
}
