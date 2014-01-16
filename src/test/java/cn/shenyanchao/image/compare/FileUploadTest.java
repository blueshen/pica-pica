package cn.shenyanchao.image.compare;

import cn.shenyanchao.image.entity.ImageCompareResult;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author shenyanchao
 * @date 14-1-15.
 */
public class FileUploadTest {

    private String uuid = "d38719d1-3246-4e96-b444-6d7310156fad";

    @Test
    public void uploadTest() throws IOException {
        long beginTime = System.currentTimeMillis();
        RestTemplate template = new RestTemplate();
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("sourceName", "a.png");
        parts.add("sourceFile", new FileSystemResource("/home/shenyanchao/git/image-similarity/src/test/resources/basecompare/com.baidu.dan.uitest.test.AccountTest/createAccountTest/6/step0_com.baidu.dan.uitest.page.CreateAccountPage.getLoginUser.png"));
        parts.add("candidateName", "b.png");
        parts.add("candidateFile", new FileSystemResource("/home/shenyanchao/git/image-similarity/src/test/resources/tocompare/com.baidu.dan.uitest.test.AccountTest/createAccountTest/6/step0_com.baidu.dan.uitest.page.CreateAccountPage.getLoginUser.png"));
        ResponseEntity<ImageCompareResult> responseEntity = template.postForEntity("http://localhost:8080/pica-pica/api/imagediff", parts, ImageCompareResult.class);
        ImageCompareResult result = responseEntity.getBody();
        long endTime = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("耗时(ms):" + (endTime - beginTime));
    }

    @Test
    public void getDiffImageTest() throws IOException {

        long beginTime = System.currentTimeMillis();
        InputStream is = new URL("http://localhost:8080/pica-pica/diffimage/d38719d1-3246-4e96-b444-6d7310156fad").openStream();

        FileOutputStream out = new FileOutputStream(new File("/home/shenyanchao/change.png"));
        IOUtils.copy(is, out);
        out.close();
        is.close();
        long endTime = System.currentTimeMillis();
        System.out.println("耗时(ms):" + (endTime - beginTime));
    }

}
