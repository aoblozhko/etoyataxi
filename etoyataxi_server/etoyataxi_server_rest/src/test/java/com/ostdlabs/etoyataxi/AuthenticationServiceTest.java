package com.ostdlabs.etoyataxi;

import org.hamcrest.MatcherAssert;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/*
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
*/

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:serviceContext-test.xml")
public class AuthenticationServiceTest {

/*    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationTokenProcessingService authenticationTokenProcessingService;

    @Autowired
    private TokenUtils tokenUtils;

        @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void testTokenOfUser() throws Exception {
        User result = new User();
        result.setCreationDate(DateTime.now());
        result.setLastName("MADER");
        result.setFirstName("RAINER");
        result.setBirthDate(new DateTime(1966, 12, 6, 0, 0));
        result.setZipCode("06116");
        result.setCity("HALLE");
        result.setStreet("REIDEBURGER STR");
        result.setHouse("41");
        result.setEmail("test@ostdlabs.com");
        result.setGender(Gender.FEMALE);
        result.setPassword(passwordEncoder.encode("test"));
        result = userRepository.save(result);

        String token = tokenUtils.createToken(result);
        User rez = authenticationTokenProcessingService.getUserByAuthToken(token);
        MatcherAssert.assertThat(rez.getId(), equalTo(result.getId()));

        String expires = String.valueOf(new DateTime().minusMinutes(1).getMillis());
        token = tokenUtils.createToken(result,expires);
        MatcherAssert.assertThat(tokenUtils.validateToken(token,result), is(false));
    }*/

}