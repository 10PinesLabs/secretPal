package com.tenPines.persistence;

import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.Wish;
import com.tenPines.model.Worker;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;

public class DatabaseWishlistTest extends SpringBaseTest {

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private WorkerService workerService;

    private Worker worker;
    private Worker creator;

    @Before
    public void setUP() {
        worker = new WorkerBuilder().build();
        creator = new WorkerBuilder().build();
        workerService.save(worker);
        workerService.save(creator);
    }

    @Test
    public void When_A_Wishlist_is_created_it_should_be_empty() {
        assertThat(wishlistRepository.findAll(), empty());
    }

    @Test
    public void When_I_Add_A_Wish_To_A_Wishlist_It_Should_Get_Stored() throws Exception {
        Wish aWish = Wish.create(creator, worker, "Un pony");

        wishlistRepository.save(aWish);

        assertThat(wishlistRepository.findAll(), hasSize(1));
        assertThat(wishlistRepository.findAll(), hasItem(hasProperty("gift", is("Un pony"))));
    }

    @Test
    public void When_I_Remove_A_Wish_From_A_Wishlist_It_Should_Be_No_More() throws Exception {
        Wish aWish = Wish.create(creator, worker, "Un pony");

        wishlistRepository.save(aWish);
        wishlistRepository.delete(aWish);

        assertThat(wishlistRepository.findAll(), hasSize(0));
        assertThat(wishlistRepository.findAll(), not(hasItem(aWish)));
    }

    @Test
    public void When_I_Edit_A_Wish_From_A_Wishlist_It_Should_Be_Changed() throws Exception {
        Wish aWish = Wish.create(creator, worker, "Un pony");
        wishlistRepository.save(aWish);

        aWish.setGift("Dos ponys!");
        wishlistRepository.save(aWish);

        assertThat(wishlistRepository.findAll(), hasSize(1));
        assertThat(wishlistRepository.findAll(), hasItem(hasProperty("gift", is("Dos ponys!"))));
        assertThat(worker.getId(), not(nullValue()));
    }

    @Test
    public void One_creator_can_have_multiple_wishes_for_himself() throws Exception {
        Wish aWish = Wish.create(creator, creator, "Un pony");
        wishlistRepository.save(aWish);

        Wish otherWish = Wish.create(creator, creator, "Otro pony");
        wishlistRepository.save(otherWish);

        assertThat(wishlistRepository.findAll(), hasSize(2));
        assertThat(wishlistRepository.findAll(), hasItems(
                hasProperty("gift", is("Un pony")),
                hasProperty("gift", is("Otro pony"))
            ));
    }
}
