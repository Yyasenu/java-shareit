package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(long ownerId);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE %:text% OR LOWER(i.description) LIKE %:text%")
    Collection<Item> searchByText(String text);
}