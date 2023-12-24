package sandromoretti.ragnarokspringapi.JWTFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sandromoretti.ragnarokspringapi.config.GroupsConfig;
import sandromoretti.ragnarokspringapi.entity.User;
import sandromoretti.ragnarokspringapi.repository.UserRepository;
import sandromoretti.ragnarokspringapi.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    UserService userService;

    /*
    @Autowired
    PrivilegeRepository privilegeRepository;
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String jwt = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER_STRING);
        if(jwt != null && jwt.startsWith(SecurityConstants.JWT_TOKEN_PREFIX)){
            try {
                jwt = jwt.split(SecurityConstants.JWT_TOKEN_PREFIX)[1];

                String subject = JWT.require(Algorithm.HMAC256(SecurityConstants.JWT_SECRET)).build().verify(jwt).getSubject();
                User user = userService.getUserByJWTSubject(subject);
                if (user == null) {
                    // invalid user on token. this should not happen
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                authorities.add(new SimpleGrantedAuthority(GroupsConfig.ROLE_PREFIX + GroupsConfig.PLAYER));

                if(user.getGroup_id() >= GroupsConfig.VIP_GROUP_ID)
                    authorities.add(new SimpleGrantedAuthority(GroupsConfig.ROLE_PREFIX + GroupsConfig.VIP));

                if(user.getGroup_id() >= GroupsConfig.SUPPORT_GROUP_ID)
                    authorities.add(new SimpleGrantedAuthority(GroupsConfig.ROLE_PREFIX + GroupsConfig.SUPPORT));

                if(user.getGroup_id() >= GroupsConfig.GAME_MASTER_GROUP_ID)
                    authorities.add(new SimpleGrantedAuthority(GroupsConfig.ROLE_PREFIX + GroupsConfig.GAME_MASTER));

                if(user.getGroup_id() >= GroupsConfig.ADMIN_GROUP_ID)
                    authorities.add(new SimpleGrantedAuthority(GroupsConfig.ROLE_PREFIX + GroupsConfig.ADMIN));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch(Exception ex){
                // invalid token
                //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                //return;
            }
        }
        filterChain.doFilter(request, response);
    }
}