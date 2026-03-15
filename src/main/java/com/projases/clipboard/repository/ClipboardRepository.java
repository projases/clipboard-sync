package com.projases.clipboard.repository;

import com.projases.clipboard.model.ClipboardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClipboardRepository extends JpaRepository<ClipboardItem, Long> {
    /**
     *  Finds all clipboard items ordered by timestamp in descending order (so the most recent items come first). 
     */
    List<ClipboardItem> findAllByOrderByTimestampDesc();

    /**
     * Finds the most recent clipboard item by ordering the items by timestamp in descending order and returning the first one. 
     */
    ClipboardItem findFirstByOrderByTimestampDesc();
}

