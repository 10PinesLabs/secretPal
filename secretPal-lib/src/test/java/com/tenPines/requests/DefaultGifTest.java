package com.tenPines.requests;

import com.tenPines.application.service.DefaultGifService;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DefaultGifTest extends SpringBaseTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DefaultGifService defaultGifService;

    @LocalServerPort
    private String port;

    @Before
    public void setUp() {
        defaultGifService.set(defaultGifService.defaultDefaultGifURL());
    }

    @Test
    public void canSetANewDefaultGifAndReturnIt() {
        String url = "http://localhost:" + port + "/api/auth/defaultGif";
        String newUrl = "https://media.giphy.com/media/l2R0aKwejYr8ycKAg/giphy.gif";
        restTemplate.postForObject(url, newUrl, Void.class);
        assertThat(defaultGifService.get().getUrl(), is(newUrl));
    }

    @Test
    public void ifNoDefaultGifHasBeenSetADefaultDefaultOneIsReturned() {
        assertThat(defaultGifService.get().getUrl(), is(defaultGifService.defaultDefaultGifURL()));
    }
}
