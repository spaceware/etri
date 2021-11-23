package sw.etri.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModalDto {
    private String layer;
    private String sat;
    private String date;
}
