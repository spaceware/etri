package sw.etri.service.pic;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineByte;
import ar.com.hjg.pngj.PngWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PngMaker {

    private  static List<ImageLineByte> createEmptyImageLines(ImageInfo imgInfo) {
        List<ImageLineByte> imgLines = new ArrayList<ImageLineByte>(imgInfo.rows);
        for (int h = 0; h < imgInfo.rows; ++h) {
            imgLines.add(new ImageLineByte(imgInfo));
        }
        return imgLines;
    }

	public static byte[] makeRGBImage(byte[][] rgbData, int width, int height, byte[] noData) {
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 100);) {
			
			/**
			 * Full constructor
			 * 
			 * @param cols      Width in pixels
			 * @param rows      Height in pixels
			 * @param bitdepth  Bits per sample, in the buffer : 8-16 for RGB true color and
			 *                  greyscale
			 * @param alpha     Flag: has an alpha channel (RGBA or GA)
			 * @param grayscale Flag: is gray scale (any bitdepth, with or without alpha)
			 * @param indexed   Flag: has palette 
			 * public ImageInfo (int cols, int rows, int bitdepth, boolean alpha, boolean grayscale, boolean indexed);
			 */
			ImageInfo imgInfo = new ImageInfo(width, height, 8, true, false, false);
			PngWriter pngw = new PngWriter(baos, imgInfo);
			pngw.setCompLevel(5);
			List<ImageLineByte> imageLines = createEmptyImageLines(imgInfo);

			int srcIdx, tgtIdx;
			
			byte red, green, blue;
			for (int h = 0; h < height; ++h) {
				ImageLineByte line = imageLines.get(h);
				byte[] imgData = line.getScanline();
				for (int w = 0; w < width; ++w) {
				
					tgtIdx = w*4;
//					tgtIdx = w*3;
					srcIdx = h*width + w;
					
					red = (byte)rgbData[0][srcIdx];
					green = (byte)rgbData[1][srcIdx];
					blue = (byte)rgbData[2][srcIdx];
					
					imgData[tgtIdx+0] = red;//h*width + w];
					imgData[tgtIdx+1] = green;//h*width + w];
					imgData[tgtIdx+2] = blue;//h*width + w];
					imgData[tgtIdx+3] = (byte)255;
//					rgbVal = (red)
					if(red == noData[0] && green == noData[1] && blue == noData[2] ) {
						
						imgData[tgtIdx+3] = (byte)0;
					} else {
//						System.out.println(h + ", " + w + " : " + red + "-"+green+"-"+blue);
					}
				}
				
				pngw.writeRow(line);
			}
			
			pngw.end();
			
			return baos.toByteArray();
		} catch (IOException e) {
            throw new RuntimeException("makeImageBytes Error");
        }
	}
	
	public static byte[] makeGrayImage(byte[] data, int width, int height, byte noData) {
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 100);) {
			
			/**
			 * Full constructor
			 * 
			 * @param cols      Width in pixels
			 * @param rows      Height in pixels
			 * @param bitdepth  Bits per sample, in the buffer : 8-16 for RGB true color and
			 *                  greyscale
			 * @param alpha     Flag: has an alpha channel (RGBA or GA)
			 * @param grayscale Flag: is gray scale (any bitdepth, with or without alpha)
			 * @param indexed   Flag: has palette 
			 * public ImageInfo (int cols, int rows, int bitdepth, boolean alpha, boolean grayscale, boolean indexed);
			 */
			ImageInfo imgInfo = new ImageInfo(width, height, 8, false);
			PngWriter pngw = new PngWriter(baos, imgInfo);
			pngw.setCompLevel(5);
			List<ImageLineByte> imageLines = createEmptyImageLines(imgInfo);

			int srcIdx, tgtIdx;
			
			byte gray;
			for (int h = 0; h < height; ++h) {
				ImageLineByte line = imageLines.get(h);
				byte[] imgData = line.getScanline();
				for (int w = 0; w < width; ++w) {
				
//					tgtIdx = w*4;
					tgtIdx = w*3;
					srcIdx = h*width + w;
					
					gray = (byte)data[srcIdx];
					
					imgData[tgtIdx+0] = gray;//h*width + w];
					imgData[tgtIdx+1] = gray;//h*width + w];
					imgData[tgtIdx+2] = gray;//h*width + w];
				}
				
				pngw.writeRow(line);
			}
			
			pngw.end();
			
			return baos.toByteArray();
		} catch (IOException e) {
            throw new RuntimeException("makeImageBytes Error");
        }
	}
}
