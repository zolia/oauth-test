
package lt.tendo.mm.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
public class OAuth2ServerConfiguration {

	private static final String RESOURCE_ID = "restservices";

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


        @Autowired
        private TokenStore tokenStore;


		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			resources
                    .tokenStore(tokenStore)
                    .resourceId(RESOURCE_ID);
		}

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Autowired
		OAuth2ClientContext oauth2ClientContext;

		@Override
		public void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();

			// @formatter:off
			http
					.csrf().disable()
					.authorizeRequests()
					.antMatchers("/login*","/login*", "/logout*", "/signin/**", "/signup/**",
							"/user/registration*", "/registrationConfirm*", "/expiredAccount*",
							"/badUser*", "/user/resendRegistrationToken*" ,"/forgetPassword*", "/user/resetPassword*",
							"/user/changePassword*", "/emailError*", "/resources/**","/old/user/registration*","/successRegister*","/qrcode*").permitAll();

			http.authorizeRequests().anyRequest().authenticated();
		}
	}


	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private DataSource dataSource;

		@Bean
		public TokenStore tokenStore() {
			// TODO: uncomment me
			// return new InMemoryTokenStore();
			return new JdbcTokenStore(dataSource);
		}

        @Bean
        protected AuthorizationCodeServices authorizationCodeServices() {
            return new JdbcAuthorizationCodeServices(dataSource);
        }

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Autowired
		private CustomUserDetailsService userDetailsService;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints)
				throws Exception {
            endpoints
                    .userDetailsService(userDetailsService)
                    .authorizationCodeServices(authorizationCodeServices())
                    .authenticationManager(this.authenticationManager)
                    .tokenStore(tokenStore())
                    .approvalStoreDisabled();
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients
					//.jdbc(dataSource)
                    .inMemory()
					.withClient("clientapp")
                    .accessTokenValiditySeconds(-1)
						.authorizedGrantTypes("password", "refresh_token")
						.authorities("USER")
						.scopes("read", "write")
						.resourceIds(RESOURCE_ID)
						.secret("123456");
		}
	}
}
