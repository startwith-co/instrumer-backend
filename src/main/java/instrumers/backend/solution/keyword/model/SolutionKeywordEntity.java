package instrumers.backend.solution.keyword.model;

import instrumers.backend.base.BaseTimeEntity;
import instrumers.backend.solution.base.model.SolutionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SOLUTION_KEYWORD")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class SolutionKeywordEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solution_keyword_seq")
    private Long solutionKeywordSeq;

    @Column(name = "keyword", nullable = false, unique = false)
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_seq", nullable = false)
    private SolutionEntity solutionEntity;
}
