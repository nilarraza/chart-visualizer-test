package com.eitech1.chartv.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eitech1.chartv.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String userName);

	User findByuserid(int userId);

	List<User> findByRoles(String string);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	boolean existsByUseridAndToken(Integer id,String token);
	
	
	
	@Query("SELECT u FROM User u WHERE u.isActive=1")
	List<User> findByActive();
}
