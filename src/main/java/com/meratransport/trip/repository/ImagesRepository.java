package com.meratransport.trip.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meratransport.trip.entity.Images;

public interface ImagesRepository extends JpaRepository<Images, UUID> {

	List<Images> findByReferenceIdAndType(String id, String type);

	void deleteByReferenceIdAndTypeAndIsDamaged(String id, String string, boolean b);

	List<Images> findByReferenceIdInAndType(List<String> ids, String tag);

	void deleteByReferenceIdInAndType(List<String> deviceIds, String tag);

}
