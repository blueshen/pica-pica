package cn.shenyanchao.image.web;

import cn.shenyanchao.image.comparer.ImageComparer;
import cn.shenyanchao.image.entity.ImageCompareResult;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shenyanchao on 14-1-15.
 *
 * @author shenyanchao
 */

@Controller
public class ImageController {

    private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    @RequestMapping(value = "/{jsp}", method = RequestMethod.GET)
    public String autonavi(@PathVariable String jsp) {
        return jsp;
    }

    @RequestMapping(value = "/api/imagediff", method = RequestMethod.POST)
    @ResponseBody
    public ImageCompareResult imagediff(@RequestParam(required = true) MultipartFile sourceFile,
                                        @RequestParam(required = true) MultipartFile candidateFile)
            throws IOException {
//        String path = request.getSession().getServletContext()
//                .getRealPath("upload");
        LOG.info(sourceFile.getContentType());
        LOG.info(candidateFile.getContentType());
        InputStream sourceInputFile = sourceFile.getInputStream();
        InputStream candidateInputFile = candidateFile.getInputStream();
        ImageComparer imageComparer = new ImageComparer(ImageIO.read(sourceInputFile),
                ImageIO.read(candidateInputFile));
        ImageCompareResult result = imageComparer.compareWithBlock();
        return result;

    }

    @RequestMapping(value = "/imagediff", method = RequestMethod.POST)
    public String imagediff(@RequestParam(required = true) MultipartFile sourceFile,
                            @RequestParam(required = true) MultipartFile candidateFile, Model model)
            throws IOException {
        if (sourceFile.isEmpty() || candidateFile.isEmpty()) {
            //do nothing
        } else {
            long beginTime = System.currentTimeMillis();
            ImageCompareResult result = imagediff(sourceFile, candidateFile);
            long endTime = System.currentTimeMillis();
            model.addAttribute("result", result);
            model.addAttribute("durationTime",endTime-beginTime);
        }
        return "index";

    }

    @RequestMapping(value = "/diffimage/{id}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getDiffImage(@PathVariable String id) throws IOException {
        File file = new File("/home/shenyanchao/git/pica-pica/src/test/resources/" + id + ".png");
        byte[] body = FileUtils.readFileToByteArray(file);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);
        header.set("Content-Disposition", "attachment; filename=" + id + ".png");
        header.setContentLength(body.length);
        return new HttpEntity<byte[]>(body, header);
    }
}
