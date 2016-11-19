package musixise.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.security.jwt.JWTConfigurer;
import musixise.service.UserService;
import musixise.web.rest.dto.user.LoginDTO;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Api(value = "user-jwt", description = "用户登录", position = 1)
public class UserJWTController {

    @Inject
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "用户认证并获取秘钥,后续接口调用都依赖此秘钥", position = 2)
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        try {
            String jwt = userService.auth(authenticationToken);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
