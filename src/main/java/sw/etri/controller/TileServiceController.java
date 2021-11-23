package sw.etri.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import sw.etri.dto.ImageSearchInfo;
import sw.etri.dto.SatelliteInfo;
import sw.etri.service.SearchImage;
import sw.etri.service.data.ZipFileCacheKey;
import sw.etri.service.pic.PngMaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
public class TileServiceController {

    @Value("${data.tile.root}")
    private Path dataRoot;

    @Autowired
    private SearchImage fileSearchImage;
    private byte[] noDataImage;

    LoadingCache<String, ZipFile> zipFileCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(new CacheLoader<String, ZipFile>() {
                @Override
                public ZipFile load(String key) throws Exception {
                    log.debug("Loading ZipFile {}", key);
                    return getZipFile(key);
                }
            });


    LoadingCache<ZipFileCacheKey, Optional<FileHeader>> zipSingleFileHeaderCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.HOURS)
            .build(new CacheLoader<ZipFileCacheKey,Optional<FileHeader>>(){
                public Optional<FileHeader> load(ZipFileCacheKey key) throws ZipException, ExecutionException { // no checked exception
                    log.debug("Loading FileHeader {}", key);

                    ZipFile zipFile = zipFileCache.get(key.toFilePath());
                    if(zipFile == null) {
                        throw new ZipException("LayerNotFound " + key.getLayerName());
                    }

                    String fileName = key.getLevel() + "/" + key.getCol() + "/" + key.getRow() + ".png";
                    FileHeader header = zipFile.getFileHeader(fileName);
                    if(header == null) {
                        return Optional.empty();
                    }
                    return Optional.of(header);
                }
            });

    private void makeNodataTile() {

        byte[][] noImage = new byte[3][256*256];
        byte[] noData = {(byte) 255,(byte) 255,(byte) 255};

        Arrays.fill(noImage[0], (byte)255);
        Arrays.fill(noImage[1], (byte)255);
        Arrays.fill(noImage[2], (byte)255);


        noDataImage = PngMaker.makeRGBImage(noImage, 256, 256, noData);
//		Encoder encoder = PictureEncoderType.PNG_RGBA.getEncoder(0);
//		TileWriter writer = StreamWriterType.Zip64.getWriter(ofname, props);
//		ImageDataType type = ImageDataType.BYTE_RGB;
//
//		byte[] pngArrays;
//
//		pngArrays = encoder.doEncode(tgtChunkImage, imageWidth, imageHeight);
//		FileOutputStream fos = new FileOutputStream("D:/work/DATA/KARI/"+startLevel+"-"+row+"-"+col+".png");
//		fos.write(pngArrays);
//		fos.flush();
//		fos.close();

    }
    private ZipFile getZipFile(String layerName) throws FileNotFoundException {

        String zipName = layerName + ".zip";
        Path layerPath = dataRoot.resolve(zipName);
//		logger.debug("layerPath {}", layerPath);
//		String dir = "D:/work/DATA/KARI/";
//		String fname = dir + layerName + ".zip";
//		File file = new File(fname);
        File file = layerPath.toFile();
        if(file.exists()) {

            return new ZipFile(file);
        } else {

            throw new FileNotFoundException("file not exist : " + layerPath);
        }
    }

//    @PostConstruct
//    public void postConstruct() {
//        try {
//
////            loadBigMosaicImages();
////            makeNodataTile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }





    @GetMapping("/{yyyymmdd}/{type}/{layerName}/{level}/{col}/{row}.png")
    @ResponseBody
    public ResponseEntity<Resource> getTileImage(@PathVariable("yyyymmdd") String yyyymmdd,
                                                 @PathVariable("type") String type,
                                                 @PathVariable("layerName") String layername,
                                                 @PathVariable("level") int level,
                                                 @PathVariable("col") int col,
                                                 @PathVariable("row") int row) {
        StopWatch stopWatch = new StopWatch();
        log.info("getTileImage {}/{}/{}/{}/{}", yyyymmdd, type, layername, level, col, row);

        try {
            String typeLayer = type + "/" + layername;
            ZipFileCacheKey cacheKey = ZipFileCacheKey.of(yyyymmdd, type, layername, level, col, row);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");
            stopWatch.start("Loading Header");
            ZipFile zipFile = zipFileCache.get(cacheKey.toFilePath());
            Optional<FileHeader> header = zipSingleFileHeaderCache.get(cacheKey);
            stopWatch.stop();

            byte[] imageData = null;
            stopWatch.start("Loading Image");
            if (header.isPresent()) {
                imageData = StreamUtils.copyToByteArray(zipFile.getInputStream(header.get()));
                ByteArrayResource resource = new ByteArrayResource(imageData);
                stopWatch.stop();
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
            }

        } catch (Exception ex) {

            return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
        } finally {
            log.info("getTileImage {}", stopWatch.prettyPrint());
        }
    }

}
