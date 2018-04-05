package com.tenPines.integration;

import com.tenPines.mailer.InMemoryPostMan;
import com.tenPines.persistence.*;
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
    private WishListRepository wishListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RealAdminRepository adminRepository;

    public void resetDB(){
        friendRelationRepository.deleteAll();
        wishListRepository.deleteAll();
        adminRepository.deleteAll();
        userRepository.deleteAll();
        workerRepository.deleteAll();
        postMan.messages.clear();
    }

    @Before
    public void restart(){
        resetDB();
    }
}
