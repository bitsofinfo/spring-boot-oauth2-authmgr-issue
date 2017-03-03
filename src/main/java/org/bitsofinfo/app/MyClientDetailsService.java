package org.bitsofinfo.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;


public class MyClientDetailsService implements ClientDetailsService {

	public MyClientDetailsService() {}


	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		
		if (clientId.equals("testuser")) {
			
			return new ClientDetails() {
				
				private static final long serialVersionUID = -7697022059904216596L;
				
				private int TOKEN_EXPIRY_SECONDS = 1500;
				private String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
				private String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
				
				private String secret = null;

				@Override
				public String getClientId() {
					return "testuser";
				}

				@Override
				public Set<String> getResourceIds() {
					Set<String> resourceids = new HashSet<String>();
					
					resourceids.add("test-resource-id");
					
					return resourceids;
				}

				@Override
				public boolean isSecretRequired() {
					return true;
				}

				@Override
				public String getClientSecret() {
					return this.secret;
				}

				@Override
				public boolean isScoped() {
					return false;
				}

				@Override
				public Set<String> getScope() {
					return null;
				}

				@Override
				public Set<String> getAuthorizedGrantTypes() {
					HashSet<String> grantTypes = new HashSet<String>();
					grantTypes.add(GRANT_TYPE_CLIENT_CREDENTIALS);
					grantTypes.add(GRANT_TYPE_REFRESH_TOKEN);
					return grantTypes;
				}

				@Override
				public Set<String> getRegisteredRedirectUri() {
					return null;
				}

				@Override
				public Collection<GrantedAuthority> getAuthorities() {
					List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
					
					authorities.add(new GrantedAuthority() {

						private static final long serialVersionUID = 1850051549310629007L;

						@Override
						public String getAuthority() {
							return "test-role";
						}
						
					});
					
					return authorities;
				}

				@Override
				public Integer getAccessTokenValiditySeconds() {
					return TOKEN_EXPIRY_SECONDS;
				}

				@Override
				public Integer getRefreshTokenValiditySeconds() {
					return TOKEN_EXPIRY_SECONDS;
				}

				@Override
				public boolean isAutoApprove(String scope) {
					return false;
				}

				@Override
				public Map<String, Object> getAdditionalInformation() {
					return null;
				}
				
			};
			
		} else {
			throw new NoSuchClientException("No clientId exists: " + clientId);
		}
	}
	

}
