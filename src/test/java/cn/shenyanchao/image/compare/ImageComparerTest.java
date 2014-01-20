package cn.shenyanchao.image.compare;

import cn.shenyanchao.image.comparer.ImageComparer;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by shenyanchao on 14-1-15.
 */
public class ImageComparerTest {

    private ImageComparer imageComparer;
    static
    {
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
    }
    @Test
    public void compareTest() throws IOException {
        long beginTime = System.currentTimeMillis();
        imageComparer = new ImageComparer("/home/shenyanchao/git/image-similarity/src/test/resources/basecompare/com.baidu.dan.uitest.test.AccountTest/createAccountTest/6/step0_com.baidu.dan.uitest.page.CreateAccountPage.getLoginUser.png"
                , "/home/shenyanchao/git/image-similarity/src/test/resources/tocompare/com.baidu.dan.uitest.test.AccountTest/createAccountTest/6/step0_com.baidu.dan.uitest.page.CreateAccountPage.getLoginUser.png");
        imageComparer.compareWithBlock();
        long endTime = System.currentTimeMillis();
        System.out.println("耗时(ms):" + (endTime - beginTime));
    }
}
