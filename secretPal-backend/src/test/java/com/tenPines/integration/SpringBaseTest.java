package com.tenPines.integration;

import com.tenPines.mailer.InMemoryPostMan;
import com.tenPines.persistence.FriendRelationRepository;
import com.tenPines.persistence.WishlistRepository;
import com.tenPines.persistence.WorkerRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public abstract class SpringBaseTest {
    @Autowired
    private FriendRelationRepository friendRelationRepository;
    @Autowired
    private InMemoryPostMan postMan;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private WishlistRepository wishlistRepository;

    public void resetDB(){
        friendRelationRepository.deleteAll();
        wishlistRepository.deleteAll();
        workerRepository.deleteAll();
        postMan.messages.clear();
    }


    @Before
    public void restart(){
        resetDB();
    }
}
