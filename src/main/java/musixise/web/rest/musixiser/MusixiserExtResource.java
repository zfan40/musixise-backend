package musixise.web.rest.musixiser;

import com.codahale.metrics.annotation.Timed;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.swagger.annotations.*;
import musixise.domain.Musixiser;
import musixise.domain.Stages;
import musixise.domain.User;
import musixise.domain.WorkList;
import musixise.repository.MusixiserRepository;
import musixise.repository.StagesRepository;
import musixise.repository.UserRepository;
import musixise.repository.WorkListRepository;
import musixise.repository.search.MusixiserSearchRepository;
import musixise.repository.search.StagesSearchRepository;
import musixise.repository.search.WorkListSearchRepository;
import musixise.security.SecurityUtils;
import musixise.service.UserService;
import musixise.web.rest.dto.ManagedUserDTO;
import musixise.web.rest.dto.MusixiserDTO;
import musixise.web.rest.dto.OutputDTO;
import musixise.web.rest.dto.user.RegisterDTO;
import musixise.web.rest.util.HeaderUtil;
import org.apache.catalina.Context;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.tools.java.Environment;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by zhaowei on 16/5/15.
 */
@Api(value = "musixisers", description = "音乐人相关接口", position = 1)
@RestController
@RequestMapping("/api/musixisers")
public class MusixiserExtResource {

    private final Logger log = LoggerFactory.getLogger(MusixiserExtResource.class);

    @Inject
    private MusixiserRepository musixiserRepository;

    @Inject
    private MusixiserSearchRepository musixiserSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private StagesRepository stagesRepository;

    @Inject
    private StagesSearchRepository stagesSearchRepository;

    @Inject
    private WorkListRepository workListRepository;

    @Inject
    private WorkListSearchRepository workListSearchRepository;

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private Auth auth;

    @RequestMapping(value = "/register",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiOperation(value = "音乐人注册", notes = "返回用户实体对象", response = Musixiser.class, position = 2)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "注册成功", response = Musixiser.class),
        @ApiResponse(code = 404, message = "找不到页面"),
        @ApiResponse(code = 500, message = "内部报错")}
    )
    @Timed
    public ResponseEntity<?> registerMusixiser(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) throws URISyntaxException {

        //注册账号
        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin(registerDTO.getUsername());
        managedUserDTO.setPassword(registerDTO.getPassword());
        managedUserDTO.setEmail(registerDTO.getEmail());

        if (userRepository.findOneByLogin(managedUserDTO.getLogin()).isPresent()) {
            return ResponseEntity.ok(new OutputDTO<>(20000, "用户名已存在"));

        } else if (userRepository.findOneByEmail(registerDTO.getEmail()).isPresent()) {
            return ResponseEntity.ok(new OutputDTO<>(20000, "邮箱已存在"));
        } else {

            //  User newUser = userService.createUser(managedUserDTO);
            User newUser = userService.createUserBySite(
                managedUserDTO.getLogin(),
                managedUserDTO.getPassword(),
                managedUserDTO.getFirstName(),
                managedUserDTO.getLastName(),
                managedUserDTO.getEmail()
            );
            //保存个人信息
            Musixiser musixiser = new Musixiser();

            musixiser.setUserId(newUser.getId());
            musixiser.setRealname(registerDTO.getRealname());
            musixiser.setTel(registerDTO.getTel());
            musixiser.setEmail(registerDTO.getEmail());
            musixiser.setBirth(registerDTO.getBirth());
            musixiser.setGender(registerDTO.getGender());
            musixiser.setSmallAvatar(registerDTO.getSmallAvatar());
            musixiser.setLargeAvatar(registerDTO.getLargeAvatar());
            musixiser.setNation(registerDTO.getNation());

            Musixiser result = musixiserRepository.save(musixiser);

            //搜索索引
            musixiserSearchRepository.save(result);

            return ResponseEntity.ok(new OutputDTO<>(0, "success", result));
        }
    }


    @RequestMapping(value = "/getInfo",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取当前用户信息", notes = "返回当前用户信息", response = Musixiser.class, position = 2)
    @Timed
    public ResponseEntity<?> getMusixiserInfo() {

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                Musixiser musixiser = musixiserRepository.findOneByUserId(u.getId());
                MusixiserDTO musixiserDTO = new MusixiserDTO();
                //musixiserDTO.setId(musixiser.getId());
                musixiserDTO.setUserId(musixiser.getUserId());
                musixiserDTO.setUsername(SecurityUtils.getCurrentUserLogin());
                musixiserDTO.setEmail(musixiser.getEmail());
                musixiserDTO.setLargeAvatar(musixiser.getLargeAvatar());
                musixiserDTO.setSmallAvatar(musixiser.getSmallAvatar());
                musixiserDTO.setNation(musixiser.getNation());
                musixiserDTO.setBirth(musixiser.getBirth());
                musixiserDTO.setTel(musixiser.getTel());

                return ResponseEntity.ok(new OutputDTO<>(0, "success", musixiserDTO));
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/updateInfo",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改当前用户信息", notes = "修改当前用户信息", response = Musixiser.class, position = 2)
    @Timed
    public ResponseEntity<Musixiser> updateMusixiserEx(@Valid @RequestBody Musixiser musixiser) {
        log.debug("REST request to update MusixiserEx : {}", musixiser);

        //获取当前用户信息
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                Musixiser musixiserCmp = musixiserRepository.findOneByUserId(u.getId());
                musixiser.setId(musixiserCmp.getId());
                musixiser.setUserId(u.getId());
                Musixiser result = musixiserRepository.save(musixiser);
                musixiserSearchRepository.save(result);

                return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("musixiser", musixiser.getId().toString()))
                    .body(result);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/onStages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "音乐人开始演出", notes = "音乐人开始演出", response = Stages.class, position = 2)
    @Timed
    public ResponseEntity<?> onStages(@Valid @RequestBody Stages stages) throws URISyntaxException {
        log.debug("REST request to on Stages : {}", stages);
        if (stages.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stages", "idexists", "A new stages cannot already have an ID")).body(null);
        }

        //获取当前用户信息
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {

                stages.setUserId(u.getId());
                Stages result = stagesRepository.save(stages);
                stagesSearchRepository.save(result);
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityCreationAlert("stages", result.getId().toString()))
                    .body(result);

            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));


    }


    @RequestMapping(value = "/offStages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "音乐人中止演出", notes = "音乐人中止演出", response = Stages.class, position = 2)
    @Timed
    public ResponseEntity<Void> offStages(@ApiParam(value = "ID", required = true) @PathVariable Long id) {
        log.debug("REST request to off Stages : {}", id);

        //获取当前用户信息
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                WorkList workList = workListRepository.findOne(id);
                //判断是当前用户操作则执行.
                if (workList.getUserId() == u.getId()) {
                    stagesRepository.delete(id);
                    stagesSearchRepository.delete(id);
                }
                return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stages", id.toString())).build();

            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(value = "/saveWork",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存作品信息", notes = "储存音乐人表演的作品信息", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<WorkList> saveWork(@Valid @RequestBody WorkList workList) throws URISyntaxException {
        log.debug("REST request to save WorkList  : {}", workList);
        if (workList.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workList", "idexists", "A new workList cannot already have an ID")).body(null);
        }

        //获取当前用户信息
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                workList.setUserId(u.getId());
                WorkList result = workListRepository.save(workList);
                workListSearchRepository.save(result);
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityCreationAlert("workList", result.getId().toString()))
                    .body(result);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));


    }


    @RequestMapping(value = "/pastWorksList",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取当前音乐人作品列表", notes = "获取音乐人过去的演出作品列表（midi或其他形式返回）", response = WorkList.class, position = 2)
    @Timed
    public List<WorkList> getPassWorksList(Pageable pageable) {
        log.debug("REST request to get all WorkLists");
        List<WorkList> workLists = workListRepository.findAll();
        return workLists;
    }

    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "上传图片", notes = "上传图片返回图片链接,使用云存储.", response = OutputDTO.class, position = 2)
    @Timed
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, Context context) {

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


        return ResponseEntity.ok(new OutputDTO<>(0, "success", file.getOriginalFilename()));
    }

    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        String filePath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
        File tmpFile = new File(filePath, multipart.getOriginalFilename());
        tmpFile.createNewFile();
        multipart.transferTo(tmpFile);
        return tmpFile;
    }

}
