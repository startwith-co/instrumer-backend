package instrumers.backend.solution.repository.impl;

import instrumers.backend.solution.domain.SolutionImageEntity;
import instrumers.backend.solution.repository.custom.SolutionImageRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SolutionImageRepositoryImpl implements SolutionImageRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void bulkSave(List<SolutionImageEntity> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.batchUpdate(
                """
                       INSERT INTO SOLUTION_IMAGE
                       (solution_image_url, image_type, solution_seq, created_at, updated_at)
                       VALUES (?, ?, ?, ?, ?)
                       """,
                images,
                1000,
                (ps, image) -> {
                    ps.setString(1, image.getImageUrl());
                    ps.setString(2, image.getImageType());
                    ps.setLong(3, image.getSolutionEntity().getSolutionSeq());
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                    ps.setTimestamp(5, Timestamp.valueOf(now));
                }
        );
    }
}
