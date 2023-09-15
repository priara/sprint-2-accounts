package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
    @Configuration
    class WebAuthorization  {

        @Bean
        public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

            http.authorizeRequests()

                    .antMatchers("/web/index.html", "/web/index.js","/web/styleindex.css","/web/images/**").permitAll()

                    .antMatchers(HttpMethod.POST,"/api/login", "/api/logout", "/api/clients").permitAll()

                    .antMatchers(HttpMethod.GET,"/api/clients/current", "/api/loans").hasAuthority("CLIENT")

                    .antMatchers(HttpMethod.POST,"/api/clients/current/cards","/api/transactions", "/api/loans").hasAuthority("CLIENT")

                    .antMatchers(HttpMethod.GET, "/api/clients","/rest/**").hasAuthority("ADMIN")

                    .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards/{id}", "/api/clients/current/accounts/{id}").hasAuthority("CLIENT")

                    .antMatchers("/manager.html", "/manager.js").hasAuthority("ADMIN")

                    .antMatchers("/web/account.html","/web/cards.html","/web/cards.js","/web/style.css","/web/accounts.html", "/web/create-cards.html","/web/create-cards.js","/web/transfers.html", "/web/transfers.js").hasAuthority("CLIENT")

                    /*.anyRequest().denyAll()*/
            ;


            http.formLogin()

                    .usernameParameter("email")

                    .passwordParameter("password")

                    .loginPage("/api/login");


            http.logout().logoutUrl("/api/logout");

            // turn off checking for CSRF tokens

            http.csrf().disable();

            //disabling frameOptions so h2-console can be accessed

            http.headers().frameOptions().disable();
            /*se utiliza para deshabilitar la protección de seguridad de marco*/
            /*permite cargar páginas web en un <iframe> (marco) en tu aplicación sin restricciones de seguridad adicionales.*/

            // if user is not authenticated, just send an authentication failure response

            http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
            /*se utiliza para personalizar la forma en que se manejan
            las excepciones relacionadas con la autenticación en tu aplicación web.*/

            /*req: una "request" (solicitud) se refiere a una petición que un cliente
            hace a un servidor para obtener algún recurso o realizar alguna acción en la aplicación web*/

            /*res: Representa una respuesta HTTP. Esta variable generalmente se utiliza para construir
            y enviar una respuesta al cliente, como configurar encabezados de respuesta,  etc.*/

            /*exc: Representa una excepción, se utiliza para capturar excepciones relacionadas
            con la autenticación, la autorización u otras cuestiones de seguridad.*/

            // if login is successful, just clear the flags asking for authentication

            http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

            // if login fails, just send an authentication failure response

            http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

            // if logout is successful, just send a success response

            http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

            return http.build();
        }

        private void clearAuthenticationAttributes(HttpServletRequest request) {

            HttpSession session = request.getSession(false);

            if (session != null) {

                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

            }

        }


    }

