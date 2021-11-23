package sw.etri.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {
    private String startDate;
    private String endDate;
    private List<String> satellite;
}
