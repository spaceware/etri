package sw.etri.controller;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ApiController {
    @PostMapping("/test")
    public ResponseEntity test(){
        List<SatelliteInfo> infos = new ArrayList<>();
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20190408T021609_TCI_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20190408T021609_B01_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20190408T021609_B02_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20190408T021609_B03_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20190408T021609_B04_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20190408T021610_B02_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,10))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("LAN")
                .layer("T525DH_20190408T021610_TCI_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,10))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("KON")
                .layer("K3A_20190305043408_27296_002235335_L1R_B")
                .date(LocalDateTime.of(2019,03,05,04,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("KON")
                .layer("K3A_20190305043408_27296_002235335_L1R_G")
                .date(LocalDateTime.of(2019,03,05,04,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("KON")
                .layer("K3A_20190305043408_27296_002235335_L1R_P")
                .date(LocalDateTime.of(2019,03,05,04,34,8))
                .build());

        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20200408T021609_TCI_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20200408T021609_B01_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20200408T021609_B02_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20200408T021609_B03_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20200408T021609_B04_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .layer("T525DH_20200408T021610_B02_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("LAN")
                .layer("T525DH_20200408T021610_TCI_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("KON")
                .layer("K3A_20200305043408_27296_002235335_L1R_B")
                .date(LocalDateTime.of(2020,3,5,4,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("KON")
                .layer("K3A_20200305043408_27296_002235335_L1R_G")
                .date(LocalDateTime.of(2020,3,5,4,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("KON")
                .layer("K3A_20200305043408_27296_002235335_L1R_P")
                .date(LocalDateTime.of(2020,3,5,4,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("KON")
                .layer("K3A_20210305043408_27296_002235335_L1R_P")
                .date(LocalDateTime.of(2021,3,5,4,34,8))
                .build());

        return new ResponseEntity(infos, HttpStatus.OK);
    }
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class SatelliteInfo {
    private String satellite;
    private String layer;
    private LocalDateTime date;

}