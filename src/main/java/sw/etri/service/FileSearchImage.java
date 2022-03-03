package sw.etri.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sw.etri.dto.ImageSearchInfo;
import sw.etri.dto.SatelliteInfo;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class FileSearchImage implements SearchImage {

    @Value("${data.tile.root}")
    private Path dataRoot;

    @Override
    public List<SatelliteInfo> search(ImageSearchInfo info) {

        List<File> dirs = getBetweenDateDir(info.getSdate(), info.getEdate());
        List<String> satFilterList = info.getSatelliteList();
        List<SatelliteInfo> results = new ArrayList<>();
        for(File file : dirs) {

            Arrays.stream(file.listFiles(f-> f.isDirectory())).filter(
                    f-> satFilterList.contains(f.getName())).forEach(x ->
            {
                File[] tiffFiles = x.listFiles(xx -> xx.getName().toLowerCase().endsWith(".zip"));
                for(File tfile : tiffFiles) {

                    SatelliteInfo satInfo = new SatelliteInfo();
                    satInfo.setSatDate(file.getName());
                    satInfo.setSatLayername(tfile.getName().replace(".zip", "").replace(".ZIP", ""));
                    satInfo.setSatType(tfile.getParentFile().getName());

                    results.add(satInfo);
                }
            });
        }

        return results;
    }

    private List<File> getBetweenDateDir(Date sdate, Date edate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String strSDate = sdf.format(sdate);
        String strEDate = sdf.format(edate);

        File[] files = dataRoot.toFile().listFiles(f -> strSDate.compareTo(f.getName()) <= 0 &&
                                                        strEDate.compareTo(f.getName()) >= 0);

        return Arrays.asList(files);
    }
}
