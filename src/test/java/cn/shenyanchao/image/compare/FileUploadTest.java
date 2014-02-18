package cn.shenyanchao.image.compare;

import cn.shenyanchao.image.entity.ImageForm;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
import java.nio.charset.Charset;

/**
 * @author shenyanchao
 * @date 14-1-15.
 */
public class FileUploadTest {

    private String uuid = "d38719d1-3246-4e96-b444-6d7310156fad";

    @Test
    public void compareImageTest() throws IOException {
        long beginTime = System.currentTimeMillis();
        RestTemplate template = new RestTemplate();
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
        form.add("sourceFile", new FileSystemResource("/home/shenyanchao/Pictures/ubuntu-011.jpg"));
        form.add("candidateFile", new FileSystemResource("/home/shenyanchao/Pictures/ubuntu-011.jpg"));
        form.add("col", 20);
        form.add("row", 20);
        form.add("similarityThreshold", 0.8);
        ResponseEntity<String> responseEntity = template.postForEntity("http://localhost:8080/pica-pica/api/image/compare", form, String.class);
        String result = responseEntity.getBody();
        long endTime = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("耗时(ms):" + (endTime - beginTime));
    }

    @Test
    public void testCompareWithHttpClient() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost:8080/pica-pica/api/image/compare");
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("sourceFile", new FileBody(new File("/home/shenyanchao/Pictures/ubuntu-011.jpg")));
        entity.addPart("candidateFile", new FileBody(new File("/home/shenyanchao/Pictures/ubuntu-011.jpg")));
        entity.addPart("col", new StringBody("20"));
        entity.addPart("row", new StringBody("20"));
        entity.addPart("similarityThreshold", new StringBody("0.8"));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        String returnStr = EntityUtils.toString(response.getEntity());
        client.getConnectionManager().shutdown();
        System.out.println(returnStr);

    }

    @Test
    public void getDiffImageTest() throws IOException {

        long beginTime = System.currentTimeMillis();
        InputStream is =
                new URL("http://localhost:8080/pica-pica/api/image/d38719d1-3246-4e96-b444-6d7310156fad").openStream();
        FileOutputStream out = new FileOutputStream(new File("/home/shenyanchao/change.png"));
        IOUtils.copy(is, out);
        out.close();
        is.close();
        long endTime = System.currentTimeMillis();
        System.out.println("耗时(ms):" + (endTime - beginTime));
    }

}
