package com.formcrafter.auth.security.filters;

import com.formcrafter.auth.jwt.JwtService;
import com.formcrafter.auth.jwt.TokenType;
import com.formcrafter.auth.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String TOKEN = "jwt-access-token";

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class DoFilterInternal {

        @Test
        void whenAlreadyAuthenticated_skipsJwtProcessing() throws Exception {
            CustomUserDetails principal = samplePrincipal();
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
            );
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("accessToken", TOKEN));
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verifyNoInteractions(jwtService, userDetailsService);
            assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .isSameAs(principal);
        }

        @Test
        void whenNoAccessTokenCookie_continuesWithoutAuthentication() throws Exception {
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verifyNoInteractions(jwtService, userDetailsService);
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        }

        @Test
        void whenValidAccessToken_setsSecurityContext() throws Exception {
            CustomUserDetails principal = samplePrincipal();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("accessToken", TOKEN));
            MockHttpServletResponse response = new MockHttpServletResponse();

            when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
            when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(principal);
            when(jwtService.isTokenValid(TOKEN, EMAIL, TokenType.ACCESS)).thenReturn(true);

            filter.doFilterInternal(request, response, filterChain);

            assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .isSameAs(principal);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void whenTokenInvalid_doesNotAuthenticateButContinues() throws Exception {
            CustomUserDetails principal = samplePrincipal();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("accessToken", TOKEN));
            MockHttpServletResponse response = new MockHttpServletResponse();

            when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
            when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(principal);
            when(jwtService.isTokenValid(TOKEN, EMAIL, TokenType.ACCESS)).thenReturn(false);

            filter.doFilterInternal(request, response, filterChain);

            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void whenJwtProcessingThrows_clearsContextAndContinues() throws Exception {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users/me");
            request.setCookies(new Cookie("accessToken", TOKEN));
            MockHttpServletResponse response = new MockHttpServletResponse();

            when(jwtService.extractUsername(TOKEN)).thenThrow(new RuntimeException("bad token"));

            filter.doFilterInternal(request, response, filterChain);

            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            verify(filterChain).doFilter(request, response);
            verify(userDetailsService, never()).loadUserByUsername(any());
        }

        @Test
        void whenOnlyAnonymousAuth_processesJwt() throws Exception {
            SecurityContextHolder.getContext().setAuthentication(
                    new AnonymousAuthenticationToken(
                            "key",
                            "anonymousUser",
                            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
                    )
            );
            CustomUserDetails principal = samplePrincipal();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("accessToken", TOKEN));
            MockHttpServletResponse response = new MockHttpServletResponse();

            when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
            when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(principal);
            when(jwtService.isTokenValid(eq(TOKEN), eq(EMAIL), eq(TokenType.ACCESS))).thenReturn(true);

            filter.doFilterInternal(request, response, filterChain);

            assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .isSameAs(principal);
        }

        @Test
        void whenExistingAuthNotAuthenticated_processesJwt() throws Exception {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(EMAIL, "password")
            );
            CustomUserDetails principal = samplePrincipal();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("accessToken", TOKEN));
            MockHttpServletResponse response = new MockHttpServletResponse();

            when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
            when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(principal);
            when(jwtService.isTokenValid(TOKEN, EMAIL, TokenType.ACCESS)).thenReturn(true);

            filter.doFilterInternal(request, response, filterChain);

            assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .isSameAs(principal);
        }

        @Test
        void whenUsernameMissingFromToken_continuesWithoutAuthentication() throws Exception {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("accessToken", TOKEN));
            MockHttpServletResponse response = new MockHttpServletResponse();

            when(jwtService.extractUsername(TOKEN)).thenReturn(null);

            filter.doFilterInternal(request, response, filterChain);

            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            verify(userDetailsService, never()).loadUserByUsername(any());
            verify(filterChain).doFilter(request, response);
        }

        @Test
        void whenCookiesPresentWithoutAccessToken_continuesWithoutAuthentication() throws Exception {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setCookies(new Cookie("refreshToken", "refresh"));
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verifyNoInteractions(jwtService, userDetailsService);
        }
    }

    private static CustomUserDetails samplePrincipal() {
        return new CustomUserDetails(USER_ID, EMAIL, "secret");
    }
}
