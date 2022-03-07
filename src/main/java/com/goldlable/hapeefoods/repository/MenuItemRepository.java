package com.goldlable.hapeefoods.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goldlable.hapeefoods.model.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

}
