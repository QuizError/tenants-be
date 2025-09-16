package to.co.divinesolutions.tenors.utils;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class PageRequestDto {
    private int pageNumber = 0;
    private int pageSize = 10;
    private String sortBy = "id";
    private String sortDirection = "ASC";
    
    public Pageable toPageable() {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        return PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
    }
}