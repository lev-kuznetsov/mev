/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.web.configuration.security;

import static org.springframework.security.crypto.encrypt.Encryptors.noOpText;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author levk
 * 
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder () {
    return new BCryptPasswordEncoder ();
  }

  @Bean
  public TextEncryptor textEncryptor () {
    return noOpText ();
  }

  /* (non-Javadoc)
   * @see org.springframework.security.config.annotation.web.configuration.
   * WebSecurityConfigurerAdapter
   * #configure(org.springframework.security.config.annotation
   * .web.builders.WebSecurity) */
  @Override
  public void configure (WebSecurity web) throws Exception {
    web.ignoring ().antMatchers ("/container/**");
  }

  /* (non-Javadoc)
   * @see org.springframework.security.config.annotation.web.configuration.
   * WebSecurityConfigurerAdapter
   * #configure(org.springframework.security.config.annotation
   * .web.builders.HttpSecurity) */
  @Override
  protected void configure (HttpSecurity http) throws Exception {
    http.logout ().deleteCookies ("JSESSIONID").invalidateHttpSession (true)
        .and ().rememberMe ();
  }
}
