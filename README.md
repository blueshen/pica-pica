
[![Build Status](https://travis-ci.org/blueshen/pica-pica.png?branch=master)](https://travis-ci.org/blueshen/pica-pica)
###介绍
用于对图片进行对比，并标识出不同区域的应用。

###Usage

    mvn tomcat7:run

open <http://localhost:8080/pica-pica/>



###Thanks:

<http://www.lac.inpe.br/JIPCookbook/>

<http://blog.csdn.net/jia20003/article/details/7771651>


###开放的API接口

- `/api/image/compare`    
       **http请求方式**：POST[multipart/form-data]

       **form参数：**   
       sourceFile  源图片   
       candidateFile 要比较的图片   
       col 图片切分为几列    
       row 图片切分为几行
       similarityThreshold 阈值，低于这个相似度认为图片是不同的
       
       返回JSON：
       
       {"match":true,"diffImageId":"038d245a-ae22-460d-b123-66dd11fcdaf5","links":[]}
       
####如何调用?
1.使用Spring RestTemplate
     
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
2.使用httpclient
    
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
        
-  `/api/image/${id}`
    **http请求方式**：GET
    参数：id 上面返回的diffImageId值
    
####如何调用？
    
        InputStream is =
                new URL("http://localhost:8080/pica-pica/api/image/d38719d1-3246-4e96-b444-6d7310156fad").openStream();
        FileOutputStream out = new FileOutputStream(new File("/home/shenyanchao/change.png"));
        IOUtils.copy(is, out);
        out.close();
        is.close();
  
  或者可以直接在浏览器查看<http://localhost:8080/pica-pica/upload/d38719d1-3246-4e96-b444-6d7310156fad.png>