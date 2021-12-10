package com.booksystem.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.booksystem.service.MemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//인증서가 유효할시 컨트롤러로 진입
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MemberDetailsService mDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String headerToken = request.getHeader("token");
            String token = null;
            String memberid = null;

            if (headerToken != null && headerToken.startsWith("")) {
                token = headerToken.substring(0);
                memberid = jwtUtil.extractUsername(token);
            }
            // 토큰이 전달되었고 로그인이 되어야 하는 것만
            if (memberid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 시큐리티에 로그인 처리루틴
                UserDetails userDetails = mDetailsService.loadUserByUsername(memberid);

                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(upat);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(578, "토큰오류");
        }
    }
}
