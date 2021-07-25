package com.eitech1.chartv.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eitech1.chartv.Entity.ResetToken;
import com.eitech1.chartv.Entity.User;

@Repository
public interface  ResetTokenRepository extends JpaRepository<ResetToken, Integer>{

			ResetToken findByToken(String token);
			boolean existsByToken(String token);
			ResetToken deleteByUserUsername(String username);
			ResetToken deleteByUser(User user);
			ResetToken deleteByToken(String token);
}
