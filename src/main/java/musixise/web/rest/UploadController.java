package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.web.rest.dto.OutputDTO;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhaowei on 16/11/19.
 */

@Api(value = "picture", description = "图片接口", position = 1)
@RestController
@RequestMapping("/api/picture")
public class UploadController {

    @Inject private UploadManager uploadManager;

    @Inject private Auth auth;

    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "上传图片", notes = "上传图片返回图片链接,使用云存储.", response = OutputDTO.class, position = 2)
    @Timed
    public ResponseEntity<?> upload(@RequestParam("files") MultipartFile file) {

        //上传到七牛后保存的文件名
        String key = file.getOriginalFilename();
        String bucketname = "muixise-img";
        //上传文件的路径
        //密钥配置

        String fileName = String.format("%s_%s", RandomStringUtils.randomAlphanumeric(8), key);

        try {
            Response res = uploadManager.put(multipartToFile(file), fileName, auth.uploadToken(bucketname));
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());

            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return ResponseEntity.ok(new OutputDTO<>(0, "success", fileName));
    }

    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        String filePath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
        File tmpFile = new File(filePath, multipart.getOriginalFilename());
        tmpFile.createNewFile();
        multipart.transferTo(tmpFile);
        return tmpFile;
    }
}
