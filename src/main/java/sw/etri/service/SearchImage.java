package sw.etri.service;

import sw.etri.dto.ImageSearchInfo;
import sw.etri.dto.SatelliteInfo;

import java.util.List;

public interface SearchImage {

    public List<SatelliteInfo> search(ImageSearchInfo info);
}
