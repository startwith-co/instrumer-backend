package instrumers.backend.solution.review.model;

import instrumers.backend.base.BaseTimeEntity;
import instrumers.backend.solution.base.model.SolutionEntity;
import instrumers.backend.user.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "SOLUTION_REVIEW")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
@SQLDelete(sql = "UPDATE SOLUTION_REVIEW SET deleted = true WHERE solution_review_seq = ?")
@Where(clause = "deleted = false")
public class SolutionReviewEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solution_review_seq")
    private Long solutionReviewSeq;

    @Column(name = "context", nullable = false, unique = false)
    private String context;

    @Column(name = "rate", nullable = false, unique = false)
    private Double rate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_seq", nullable = false)
    private SolutionEntity solutionEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private UserEntity userEntity;

    @Builder.Default
    @Column(name = "deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean deleted = false;

    @Version
    @Builder.Default
    @Column(name = "version", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer version = 0;
}
