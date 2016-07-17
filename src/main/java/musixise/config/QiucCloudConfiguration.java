package musixise.config;

import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by zhaowei on 16/7/16.
 */
@Configuration
public class QiucCloudConfiguration {

    private final Logger log = LoggerFactory.getLogger(CloudDatabaseConfiguration.class);

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "ojwu8Dif9IIu2dh_9BAyr_VA1aG7kIxCFkoH6n-r";
    String SECRET_KEY = "xdV7nuNU-M7Bw8d7qorRaxK3yENNS9Z7obvKGlIg";
    //要上传的空间

    //密钥配置

    //创建上传对象

    @Bean
    public UploadManager uploadManager() {
        log.info("Configuring qiucloud provider");
        return   new UploadManager();
    }

    @Bean
    public Auth auth() {
        return Auth.create(ACCESS_KEY, SECRET_KEY);
    }
}
