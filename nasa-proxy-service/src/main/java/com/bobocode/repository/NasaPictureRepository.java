package com.bobocode.repository;

import com.bobocode.entity.Picture;
import org.springframework.data.repository.CrudRepository;

public interface NasaPictureRepository extends CrudRepository<Picture, String> {
}
