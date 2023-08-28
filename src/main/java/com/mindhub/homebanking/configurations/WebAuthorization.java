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

@EnableWebSecurity  /*estoy indicando a Spring Security que active sus características de seguridad web a la clase WebAuthorization*/
    @Configuration /*indicamos a Spring que una clase contiene información de configuración de beans*/
    class WebAuthorization  {

        @Bean
        public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

            http.authorizeRequests()
            /*Cuando utilizas http.authorizeRequests(),
            estás configurando reglas de autorización para diferentes rutas o URLs en tu aplicación*/

                    .antMatchers("/web/index.html", "/web/index.js","/web/styleindex.css","/web/images/**").permitAll()
                    /*.antMatchers: permite definir reglas de autorización específicas para diferentes rutas de tu aplicación web.*/

                    .antMatchers(HttpMethod.POST,"/api/login", "/api/clients", "/api/logout").permitAll()

                    .antMatchers(HttpMethod.GET,"/api/clients/current").hasAuthority("CLIENT")

                    .antMatchers(HttpMethod.POST,"/api/clients/current/cards").hasAuthority("CLIENT")

                    .antMatchers("/manager.html", "/manager.js").hasAuthority("ADMIN")

                    .antMatchers("/web/account.html","/web/cards.html","/web/cards.js","/web/style.css","/web/accounts.html", "/web/create-cards.html","/web/create-cards.js").hasAuthority("CLIENT")

                    /*.anyRequest().denyAll()*/
            ;


            http.formLogin()
                /*se utiliza comúnmente cuando deseas proporcionar a los usuarios
                un formulario de inicio de sesión personalizado*/

                    .usernameParameter("email")

                    .passwordParameter("password")

                    .loginPage("/api/login");


            http.logout().logoutUrl("/api/logout");
            /*esta configuración establece la URL de cierre de sesión a /api/logout.*/

            // turn off checking for CSRF tokens

            http.csrf().disable();
            /*se utiliza en Spring Security para deshabilitar la protección CSRF (Cross-Site Request Forgery) en tu aplicación web.*/
            /*CSRF es un tipo de ataque en el que un atacante engaña a un usuario
            para que realice una acción no deseada en una aplicación en la que el usuario está autenticado.*/

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

