package com.tenPines.builder;

import com.tenPines.application.MailProperties;
import com.tenPines.application.service.DefaultGifService;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.Message;
import com.tenPines.model.Worker;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HappyBithdayMessageBuilderTest extends SpringBaseTest {

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private DefaultGifService defaultGifService;

    private Worker birthdayWorker;
    private HappyBithdayMessageBuilder happyBithdayMessageBuilder;

    @Before
    public void setUp() {
        birthdayWorker = new WorkerBuilder().withNickname("Iñakí")
                .withBirthDayDate(LocalDate.of(1950, Month.DECEMBER, 10)).build();

        happyBithdayMessageBuilder = new HappyBithdayMessageBuilder(mailProperties, defaultGifService);
    }

    @Test
    public void theEmailSubjectIncludesTheBirthdayWorkerNameInUppercaseAndBetweenOneAndThreeExclamationSigns() {
        Message message = happyBithdayMessageBuilder.buildMessage(birthdayWorker);

        assertThat(message.getSubject(), containsString("IÑAKÍ"));
        assertThat(StringUtils.countMatches(message.getSubject(), "!"), is(greaterThanOrEqualTo(1)));
        assertThat(StringUtils.countMatches(message.getSubject(), "!"), is(lessThanOrEqualTo(3)));
    }
}
