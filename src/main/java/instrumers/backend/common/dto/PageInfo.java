package instrumers.backend.common.dto;

import org.springframework.data.domain.Page;

public record PageInfo(
	int page,
	int size,
	long totalElements,
	int totalPages,
	boolean hasNext,
	boolean hasPrevious
) {
	/**
	 * Spring Data의 Page 객체로부터 PageInfo를 생성합니다.
	 *
	 * @param page Spring Data Page 객체
	 * @return PageInfo 객체
	 */
	public static <T> PageInfo from(Page<T> page) {
		return new PageInfo(
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages(),
			page.hasNext(),
			page.hasPrevious()
		);
	}
}

