package cn.shenyanchao.image.web;

import cn.shenyanchao.image.comparer.ImageComparer;
import cn.shenyanchao.image.entity.ImageCompareResult;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @date  14-1-15.
 *
 * @author shenyanchao
 */

@Controller
@SessionAttributes
public class ImageController {

    private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);
    private static final String DIFF_PATH = "upload";
    @Autowired
    private ImageComparer imageComparer;

    @RequestMapping(value = "/imagediff", method = RequestMethod.POST)
    public String imagediff(@RequestParam(required = true) MultipartFile sourceFile,
                            @RequestParam(required = true) MultipartFile candidateFile,
                            Model model, HttpServletRequest request
    )
            throws IOException {
        if (sourceFile.isEmpty() || candidateFile.isEmpty()) {
            //do nothing
        } else {
            long beginTime = System.currentTimeMillis();
            ImageCompareResult result = imagediff(sourceFile, candidateFile, request);
            long endTime = System.currentTimeMillis();
            model.addAttribute("result", result);
            model.addAttribute("durationTime", endTime - beginTime);
        }
        return "index";

    }

    @RequestMapping(value = "/api/imagediff", method = RequestMethod.POST)
    @ResponseBody
    public ImageCompareResult imagediff(@RequestParam(required = true) MultipartFile sourceFile,
                                        @RequestParam(required = true) MultipartFile candidateFile,
                                        HttpServletRequest request)
            throws IOException {
        String path = request.getSession().getServletContext()
                .getRealPath(DIFF_PATH);
        LOG.info(sourceFile.getContentType());
        LOG.info(candidateFile.getContentType());
        InputStream sourceInputFile = sourceFile.getInputStream();
        InputStream candidateInputFile = candidateFile.getInputStream();
        imageComparer.setSourceImage(ImageIO.read(sourceInputFile));
        imageComparer.setCandidateImage(ImageIO.read(candidateInputFile));
        ImageCompareResult result = imageComparer.compareWithBlock();
        return result;

    }

    @RequestMapping(value = "/api/diffimage/{id}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getDiffImage(@PathVariable String id, HttpServletRequest request) throws IOException {
        String path = request.getSession().getServletContext()
                .getRealPath(DIFF_PATH);
        File file = new File(path + id + ".png");
        byte[] body = FileUtils.readFileToByteArray(file);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);
        header.set("Content-Disposition", "attachment; filename=" + id + ".png");
        header.setContentLength(body.length);
        return new HttpEntity<byte[]>(body, header);
    }
}
