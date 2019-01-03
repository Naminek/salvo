package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository,
                                      SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {
            Player user1 = new Player("j.bauer@ctu.gov", "24");
            Player user2 = new Player("c.obrian@ctu.gov", "42");
            Player user3 = new Player("kim_bauer@gmail.com", "kb");
            Player user4 = new Player("t.almeida@ctu.gov", "mole");


            Date date1 = new Date();
            Date date2 = date1.from(date1.toInstant().plusSeconds(3600));
            Date date3 = date1.from(date1.toInstant().plusSeconds(7200));

            Game game1 = new Game(date1);
            Game game2 = new Game(date2);
            Game game3 = new Game(date3);

            GamePlayer gamePlayer1 = new GamePlayer(date1);
            GamePlayer gamePlayer2 = new GamePlayer(date1);
            user1.addGamePlayer(gamePlayer1);
            game1.addGamePlayer(gamePlayer1);
            user2.addGamePlayer(gamePlayer2);
            game1.addGamePlayer(gamePlayer2);
            GamePlayer gamePlayer3 = new GamePlayer(date2);
            user1.addGamePlayer(gamePlayer3);
            game2.addGamePlayer(gamePlayer3);
            GamePlayer gamePlayer4 = new GamePlayer(date2);
            user2.addGamePlayer(gamePlayer4);
            game2.addGamePlayer(gamePlayer4);


            List<String> locations1 = Arrays.asList("H2", "H3", "H4");
            List<String> locations2 = Arrays.asList("E1", "F1", "G1");
            List<String> locations3 = Arrays.asList("B4", "B5");
            List<String> locations4 = Arrays.asList("B5", "C5", "D5");
            List<String> locations5 = Arrays.asList("F1", "F2");
            List<String> locations6 = Arrays.asList("C6", "C7");
            List<String> locations7 = Arrays.asList("A2", "A3", "A4");
            List<String> locations8 = Arrays.asList("G6", "H6");

            Ship ship1 = new Ship("Destroyer", locations1);
            Ship ship2 = new Ship("Submarine", locations2);
            Ship ship3 = new Ship("Patrol Boat", locations3);
            Ship ship4 = new Ship("Destroyer", locations4);
            Ship ship5 = new Ship("Patrol Boat", locations5);
            Ship ship6 = new Ship("Patrol Boat", locations6);
            Ship ship7 = new Ship("Submarine", locations7);
            Ship ship8 = new Ship("Patrol Boat", locations8);
            Ship ship9 = new Ship("Destroyer", locations4);

            gamePlayer1.addShip(ship1);
            gamePlayer1.addShip(ship2);
            gamePlayer1.addShip(ship3);
            gamePlayer2.addShip(ship4);
            gamePlayer2.addShip(ship5);
            gamePlayer3.addShip(ship9);
            gamePlayer3.addShip(ship6);
            gamePlayer4.addShip(ship7);
            gamePlayer4.addShip(ship8);


            List<String> salvoLocations1 = Arrays.asList("B5", "C5", "F1");
            List<String> salvoLocations2 = Arrays.asList("B4", "B5", "B6");
            List<String> salvoLocations3 = Arrays.asList("F2", "D5");
            List<String> salvoLocations4 = Arrays.asList("E1", "H3", "A2");
            List<String> salvoLocations5 = Arrays.asList("A2", "A4", "G6");
            List<String> salvoLocations6 = Arrays.asList("B5", "D5", "C7");
            List<String> salvoLocations7 = Arrays.asList("A3", "H6");
            List<String> salvoLocations8 = Arrays.asList("C5", "C6");


            Salvo salvo1 = new Salvo(1, salvoLocations1);
            Salvo salvo2 = new Salvo(1, salvoLocations2);
            Salvo salvo3 = new Salvo(2, salvoLocations3);
            Salvo salvo4 = new Salvo(2, salvoLocations4);
            Salvo salvo5 = new Salvo(1, salvoLocations5);
            Salvo salvo6 = new Salvo(1, salvoLocations6);
            Salvo salvo7 = new Salvo(2, salvoLocations7);
            Salvo salvo8 = new Salvo(2, salvoLocations8);

            gamePlayer1.addSalvo(salvo1);
            gamePlayer1.addSalvo(salvo3);
            gamePlayer3.addSalvo(salvo5);
            gamePlayer3.addSalvo(salvo7);
            gamePlayer2.addSalvo(salvo2);
            gamePlayer2.addSalvo(salvo4);
            gamePlayer4.addSalvo(salvo6);
            gamePlayer4.addSalvo(salvo8);

            Date finishingDate1 = date1.from(date1.toInstant().plusSeconds(1800));
            Date finishingDate2 = date2.from(date2.toInstant().plusSeconds(1800));
//            Date finishingDate3 = date3.from(date2.toInstant().plusSeconds(1800));

            Score score1 = new Score(0.5, finishingDate1);
            Score score2 = new Score(0.5, finishingDate1);
            Score score3 = new Score(1.0, finishingDate2);
            Score score4 = new Score(0.0, finishingDate2);

            game1.addScore(score1);
            game1.addScore(score2);
            game2.addScore(score3);
            game2.addScore(score4);

            user1.addScore(score1);
            user2.addScore(score2);
            user1.addScore(score3);
            user2.addScore(score4);


            playerRepository.save(user1);
            playerRepository.save(user2);
            playerRepository.save(user3);
            playerRepository.save(user4);

            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);

            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);
            gamePlayerRepository.save(gamePlayer4);

            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            shipRepository.save(ship4);
            shipRepository.save(ship5);
            shipRepository.save(ship6);
            shipRepository.save(ship7);
            shipRepository.save(ship8);

            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);
            salvoRepository.save(salvo5);
            salvoRepository.save(salvo6);
            salvoRepository.save(salvo7);
            salvoRepository.save(salvo8);

            scoreRepository.save(score1);
            scoreRepository.save(score2);
            scoreRepository.save(score3);
            scoreRepository.save(score4);
        };
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {
            Player player = playerRepository.findByUserName(inputName);
            if (player != null) {
                return new User(player.getEmail(), player.getPassword(), AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Player player = playerRepository.findByUserName(email);
//        if (player != null) {
//            return new User(player.getEmail(), player.getPassword(), AuthorityUtils.createAuthorityList("USER"));
//        } else {
//            throw new UsernameNotFoundException("Unknown user: " + email);
//        }
//    }

}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/web/games.html").permitAll()
                .antMatchers("/web/games.css").permitAll()
                .antMatchers("/web/games.js").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/api/games_view/*").hasAnyAuthority("USER")
                .antMatchers("/rest/*").denyAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();

        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint((request, response, authException)
                -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.formLogin().successHandler((request, response, authentication)
                -> clearAuthenticationAttributes(request));
        http.formLogin().failureHandler((request, response, exception)
                -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if(httpSession != null) {
            httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("email")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}


