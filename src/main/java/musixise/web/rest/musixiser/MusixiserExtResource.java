package musixise.web.rest.musixiser;

import com.codahale.metrics.annotation.Timed;
import musixise.domain.Musixiser;
import musixise.domain.User;
import musixise.repository.MusixiserRepository;
import musixise.repository.UserRepository;
import musixise.repository.search.MusixiserSearchRepository;
import musixise.security.SecurityUtils;
import musixise.service.UserService;
import musixise.web.rest.MusixiserResource;
import musixise.web.rest.dto.ManagedUserDTO;
import musixise.web.rest.dto.MusixiseDTO;
import musixise.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by zhaowei on 16/5/15.
 */
@RestController
@RequestMapping("/api")
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

    @RequestMapping(value = "/musixisers/register",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> registerMusixiser(@RequestBody MusixiseDTO musixiseDTO, HttpServletRequest request) throws URISyntaxException {
        //log.debug("REST request to register Musixiser : {}", musixiseDTO);
        if (musixiseDTO.getId() != null || musixiseDTO.getUserId() != null || musixiseDTO.getIsMaster() !=null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("musixiser", "idexists", "A new musixiser cannot already have an ID")).body(null);
        }

        //注册账号
        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin(musixiseDTO.getNickname());
        managedUserDTO.setPassword(musixiseDTO.getPassword());
        managedUserDTO.setEmail(musixiseDTO.getEmail());

        if (userRepository.findOneByLogin(managedUserDTO.getLogin()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "username already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(musixiseDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(managedUserDTO);
            //保存个人信息
            Musixiser musixiser = new Musixiser();

            musixiser.setUserId(newUser.getId());
            musixiser.setRealname(musixiseDTO.getRealname());
            musixiser.setTel(musixiseDTO.getTel());
            musixiser.setEmail(musixiseDTO.getEmail());
            musixiser.setBirth(musixiseDTO.getBirth());
            musixiser.setGender(musixiseDTO.getGender());
            musixiser.setSmallAvatar(musixiseDTO.getSmallAvatar());
            musixiser.setLargeAvatar(musixiseDTO.getLargeAvatar());
            musixiser.setNation(musixiseDTO.getNation());

            Musixiser result = musixiserRepository.save(musixiser);

            //搜索索引
            musixiserSearchRepository.save(result);

            return ResponseEntity.created(new URI("/api/musixisers/" + newUser.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("musixiser", result.getId().toString()))
                .body(result);
        }
    }


    @RequestMapping(value = "/musixisers/getInfo",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Musixiser> getMusixiserInfo() {

        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                Musixiser musixiser = musixiserRepository.findOneByUserId(u.getId());
                return new ResponseEntity<>(musixiser, HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/musixisers/updateInfo",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
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
}
