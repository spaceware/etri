package sw.etri.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sw.etri.dto.ImageSearchInfo;
import sw.etri.dto.ModalDto;
import sw.etri.dto.SatelliteInfo;
import sw.etri.dto.SearchDto;
import sw.etri.service.SearchImage;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    @Value("${data.tile.root}")
    private Path dataRoot;
    @Value("${basemap.key}")
    private String key;

    private final SearchImage fileSearchImage;

    @GetMapping("/getKey")
    public ResponseEntity getKey(){
        return new ResponseEntity(key, HttpStatus.OK);
    }

    @GetMapping("/satelliteInfo")
    public ResponseEntity modal(ModalDto dto) throws URISyntaxException {

        ResponseEntity response = getSatelliteInfo(dto.getDate(), dto.getSat(), dto.getLayer());
        return new ResponseEntity(response.getBody(), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity search(SearchDto dto) throws URISyntaxException {

        ImageSearchInfo info = new ImageSearchInfo();
        info.setSdate(Date.valueOf(LocalDate.parse(dto.getStartDate())));
        info.setEdate(Date.valueOf(LocalDate.parse(dto.getEndDate())));
        info.setSatelliteList(dto.getSatellite());
        try {
            List<SatelliteInfo> results = fileSearchImage.search(info);
            log.info(results.get(0).getSatLayername());
            return new ResponseEntity(results, HttpStatus.OK);
        } catch (Exception ex) {

            log.error(ex.getMessage());
            ResponseEntity infoResponseEntity = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(dataRoot.toString());
            return infoResponseEntity;
        }
    }

    public ResponseEntity<String> getSatelliteInfo(String yyyymmdd, String type, String layername) {

        try {
            String fname = String.format("%s/%s/%s.JSON", yyyymmdd, type, layername);
            Path jsonPath = dataRoot.resolve(fname);
            File jsonFile = jsonPath.toFile();
            if (!jsonFile.exists()) {
                log.error("No such file : " + jsonFile.getName());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(jsonPath.toFile(), Object.class);
            String result = mapper.writeValueAsString(json);

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception ex) {

            log.error(ex.getMessage());
            ResponseEntity<String> infoResponseEntity = new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            return infoResponseEntity;
        }
    }

}


