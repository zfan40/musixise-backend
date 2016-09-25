package musixise.config.social;

import musixise.repository.SocialUserConnectionRepository;
import musixise.repository.CustomSocialUsersConnectionRepository;
import musixise.security.social.CustomSignInAdapter;

import net.gplatform.spring.social.qq.connect.QQConnectionFactory;
import net.gplatform.spring.social.weibo.connect.WeiboConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.social.wechat.connect.WeChatConnectionFactory;
// jhipster-needle-add-social-connection-factory-import-package

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic Spring Social configuration.
 *
 * <p>Creates the beans necessary to manage Connections to social services and
 * link accounts from those services to internal Users.</p>
 */
@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {
    private final Logger log = LoggerFactory.getLogger(SocialConfiguration.class);

    @Inject
    private SocialUserConnectionRepository socialUserConnectionRepository;

    private Map<String, OAuth2ConnectionFactory> oAuth2ConnectionFactoryMap = new HashMap<>();

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {

        // Google configuration
        String googleClientId = environment.getProperty("spring.social.google.clientId");
        String googleClientSecret = environment.getProperty("spring.social.google.clientSecret");
        if (googleClientId != null && googleClientSecret != null) {
            log.debug("Configuring GoogleConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new GoogleConnectionFactory(
                    googleClientId,
                    googleClientSecret
                )
            );
        } else {
            log.error("Cannot configure GoogleConnectionFactory id or secret null");
        }

        // Facebook configuration
        String facebookClientId = environment.getProperty("spring.social.facebook.clientId");
        String facebookClientSecret = environment.getProperty("spring.social.facebook.clientSecret");
        if (facebookClientId != null && facebookClientSecret != null) {
            log.debug("Configuring FacebookConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new FacebookConnectionFactory(
                    facebookClientId,
                    facebookClientSecret
                )
            );
        } else {
            log.error("Cannot configure FacebookConnectionFactory id or secret null");
        }

        // Twitter configuration
        String twitterClientId = environment.getProperty("spring.social.twitter.clientId");
        String twitterClientSecret = environment.getProperty("spring.social.twitter.clientSecret");
        if (twitterClientId != null && twitterClientSecret != null) {
            log.debug("Configuring TwitterConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new TwitterConnectionFactory(
                    twitterClientId,
                    twitterClientSecret
                )
            );
        } else {
            log.error("Cannot configure TwitterConnectionFactory id or secret null");
        }

        //Weibo configuretion
        String weiboClientId = environment.getProperty("spring.social.weibo.clientId");
        String weiboClientSecret = environment.getProperty("spring.social.weibo.clientSecret");
        if (weiboClientId != null && weiboClientSecret != null) {
            log.debug("Configuring WeiboConnectionFactory");
            ConnectionFactory weiBoConnectionFactory = new WeiboConnectionFactory(weiboClientId, weiboClientSecret);
            connectionFactoryConfigurer.addConnectionFactory( weiBoConnectionFactory );
            OAuth2ConnectionFactory weiboAuth2ConnectionFactory = new WeiboConnectionFactory(weiboClientId, weiboClientSecret);
            oAuth2ConnectionFactoryMap.put("weibo", weiboAuth2ConnectionFactory);
        }

        //wechat configuretion
        String wechatClientId = environment.getProperty("spring.social.wechat.clientId");
        String wechatClientSecret = environment.getProperty("spring.social.wechat.clientSecret");

        if (wechatClientId != null && wechatClientSecret != null) {
            log.debug("Configuring WechatConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                    new WeChatConnectionFactory(
                            wechatClientId,
                            wechatClientSecret
                    )
            );
            OAuth2ConnectionFactory weichatAuth2ConnectionFactory = new WeChatConnectionFactory(wechatClientId, wechatClientSecret);
            oAuth2ConnectionFactoryMap.put("wechat", weichatAuth2ConnectionFactory);
        }

        //qq codniguretion
        String qqClientId = environment.getProperty("spring.social.qq.clientId");
        String qqClientSecret = environment.getProperty("spring.social.qq.clientSecret");

        if (qqClientId != null && qqClientSecret != null) {
            log.debug("Configuring qqConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new QQConnectionFactory(
                    qqClientId,
                    qqClientSecret
                )
            );
            OAuth2ConnectionFactory qqAuth2ConnectionFactory = new QQConnectionFactory(weiboClientId, weiboClientSecret);
            oAuth2ConnectionFactoryMap.put("qq", qqAuth2ConnectionFactory);

        }

        // jhipster-needle-add-social-connection-factory
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new CustomSocialUsersConnectionRepository(socialUserConnectionRepository, connectionFactoryLocator);
    }

    @Bean
    public SignInAdapter signInAdapter() {
        return new CustomSignInAdapter();
    }

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) throws Exception {
        ProviderSignInController providerSignInController = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
        providerSignInController.setSignUpUrl("/social/signup");
        return providerSignInController;
    }

    @Bean
    public ProviderSignInUtils getProviderSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
        return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
    }

    public Map<String, OAuth2ConnectionFactory> getoAuth2ConnectionFactoryMap() {
        return oAuth2ConnectionFactoryMap;
    }
}
