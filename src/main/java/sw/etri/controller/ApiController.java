package sw.etri.controller;

import lombok.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/modal")
    public ResponseEntity modal(ModalDto dto) throws URISyntaxException {
        URI serviceUrl = new URI("http://192.168.1.77:8081/modal");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
        Map<String, Object> map = new HashMap<>();
        map.put("date", dto.getDate());
        map.put("layer", dto.getLayer());
        map.put("sat", dto.getSat());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.setReadTimeout(Duration.ofMinutes(5));

        RestTemplate restTemplate = builder.build();;
        ResponseEntity<Map> response = restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, Map.class);
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity search(SearchDto dto) throws URISyntaxException {
        URI serviceUrl = new URI("http://192.168.1.77:8081/search");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
        Map<String, Object> map = new HashMap<>();
        map.put("sdate", dto.getStartDate());
        map.put("edate", dto.getEndDate());
        map.put("satelliteList", dto.getSatellite());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.setReadTimeout(Duration.ofMinutes(5));

        RestTemplate restTemplate = builder.build();;
        ResponseEntity<List> response = restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, List.class);
        return new ResponseEntity(response.getBody(), HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity test(){
        List<SatelliteInfo> infos = new ArrayList<>();
        infos.add(SatelliteInfo.builder()
                .satellite("K3A")
                .satelliteFullName("Kompsat3a")
                .layer("K3_20190405042527_36717_09411281_L1O")
                .date(LocalDateTime.of(2019,04,5,12,56,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20190408T021609_B01_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20190408T021609_B02_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20190408T021609_B03_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20190408T021609_B04_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20190408T021610_B02_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,10))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("LAN")
                .satelliteFullName("Landsat8")
                .layer("T525DH_20190408T021610_TCI_20m")
                .date(LocalDateTime.of(2019,04,8,02,16,10))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("K3A")
                .satelliteFullName("Kompsat3a")
                .layer("K3A_20190305043408_27296_002235335_L1R_B")
                .date(LocalDateTime.of(2019,03,05,04,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("K3A")
                .satelliteFullName("Kompsat3a")
                .layer("K3A_20190305043408_27296_002235335_L1R_G")
                .date(LocalDateTime.of(2019,03,05,04,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("K3A")
                .satelliteFullName("Kompsat3a")
                .layer("K3A_20190305043408_27296_002235335_L1R_P")
                .date(LocalDateTime.of(2019,03,05,04,34,8))
                .build());

        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20200408T021609_TCI_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20200408T021609_B01_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20200408T021609_B02_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20200408T021609_B03_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20200408T021609_B04_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("SEN")
                .satelliteFullName("Sentinel")
                .layer("T525DH_20200408T021610_B02_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("LAN")
                .satelliteFullName("Landsat8")
                .layer("T525DH_20200408T021610_TCI_20m")
                .date(LocalDateTime.of(2020,04,8,02,16,9))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("K3A")
                .satelliteFullName("Kompsat3a")
                .layer("K3A_20200305043408_27296_002235335_L1R_B")
                .date(LocalDateTime.of(2020,3,5,4,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("K3A")
                .satelliteFullName("Kompsat3a")
                .layer("K3A_20200305043408_27296_002235335_L1R_G")
                .date(LocalDateTime.of(2020,3,5,4,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("K3A")
                .satelliteFullName("Kompsat3a")
                .layer("K3A_20200305043408_27296_002235335_L1R_P")
                .date(LocalDateTime.of(2020,3,5,4,34,8))
                .build());
        infos.add(SatelliteInfo.builder()
                .satellite("K3A")
                .satelliteFullName("Kompsat3a")
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
    private String satelliteFullName;
    private String layer;
    private LocalDateTime date;

}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SearchDto {
    private String startDate;
    private String endDate;
    private List<String> satellite;
}
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ModalDto {
    private String layer;
    private String sat;
    private String date;
}
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ResponseDto {
    private String satType;
    private String satDate;
    private String satLayername;
}


