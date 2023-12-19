package sandromoretti.ragnarokspringapi.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sandromoretti.ragnarokspringapi.JWTFilter.JWTAuthenticationFilter;

@Configuration
@EnableMethodSecurity(
        prePostEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfiguration {
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.csrf().disable();
        //http.cors().and().addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // add jwt authentication
        //http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // use jwt authentication in all pages
        /*http.authorizeHttpRequests()
                .requestMatchers("/users/sign-in").permitAll()
                .anyRequest().authenticated();*/

        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        //.anyRequest().permitAll() // allow everything
                        //.requestMatchers("*").permitAll()
                        //.requestMatchers("/users").hasAuthority("MANAGER_USERSZ")
                        .requestMatchers(HttpMethod.POST, "/users/sign-in").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/donations").authenticated()
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}