package com.tenPines.builder;

import com.tenPines.application.MailProperties;
import com.tenPines.application.service.DefaultGifService;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.FriendRelation;
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
    private Worker workerGiver;
    private FriendRelation friendRelation;
    private HappyBithdayMessageBuilder happyBithdayMessageBuilder;

    @Before
    public void setUp() {
        birthdayWorker = new WorkerBuilder().withFullName("Cacho Castania")
                .withBirthDayDate(LocalDate.of(1950, Month.DECEMBER, 10)).build();
        workerGiver = new WorkerBuilder().build();
        friendRelation = new FriendRelation(workerGiver, birthdayWorker);

        happyBithdayMessageBuilder = new HappyBithdayMessageBuilder(mailProperties, defaultGifService);
    }

    @Test
    public void theEmailSubjectIncludesTheBirthdayWorkerNameInUppercaseAndBetweenOneAndThreeExclamationSigns() {
        Message message = happyBithdayMessageBuilder.buildMessage(birthdayWorker);

        String expectedName = friendRelation.getGiftReceiver().getName();

        assertThat(message.getSubject(), containsString(expectedName.toUpperCase()));
        assertThat(StringUtils.countMatches(message.getSubject(), "!"), is(greaterThanOrEqualTo(1)));
        assertThat(StringUtils.countMatches(message.getSubject(), "!"), is(lessThanOrEqualTo(3)));
    }
}
