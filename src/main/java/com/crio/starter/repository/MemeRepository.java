package com.crio.starter.repository;

import com.crio.starter.data.Meme;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemeRepository extends MongoRepository<Meme, String> {

  List<Meme> findByNameAndUrlAndCaption(String name, String url, String caption);

  List<Meme> findAllByOrderByIdDesc();
}
