package com.eitech1.chartv.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eitech1.chartv.Entity.SheetEx;
import com.eitech1.chartv.Entity.SheetMeta;

@Repository
public interface SheetExRepository extends JpaRepository<SheetEx, Integer> {

	@Query("SELECT s FROM SheetEx s where s.sheetMeta=?1")
	List<SheetEx> findBySheetMeta(SheetMeta meta);

}
