package sw.etri.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ImageSearchInfo {

    @JsonFormat(pattern = "yyyyMMdd")
    protected Date sdate;

    @JsonFormat(pattern = "yyyyMMdd")
    protected Date edate;

    protected List<String> satelliteList;

    protected List<SatelliteInfo>   results;
}
