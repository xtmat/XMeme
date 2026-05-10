package com.crio.starter.controller;

import com.crio.starter.data.Meme;
import com.crio.starter.repository.MemeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memes")
public class MemeController {

  @Autowired
  private MemeRepository memeRepository;

  @GetMapping
  public List<Meme> getAllMemes() {
    List<Meme> memes = memeRepository.findAllByOrderByIdDesc();
    if (memes.size() > 100) {
      return memes.subList(0, 100);
    }
    return memes;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Meme> getMemeById(@PathVariable String id) {
    Optional<Meme> meme = memeRepository.findById(id);
    return meme.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> createMeme(@RequestBody Meme meme) {
    if (meme.getName() == null || meme.getUrl() == null || meme.getCaption() == null
        || meme.getName().trim().isEmpty() || meme.getUrl().trim().isEmpty()
        || meme.getCaption().trim().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    List<Meme> existingMemes = memeRepository.findByNameAndUrlAndCaption(
        meme.getName(), meme.getUrl(), meme.getCaption());
    if (existingMemes != null && !existingMemes.isEmpty()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    Meme savedMeme = memeRepository.save(meme);
    Map<String, String> response = new HashMap<>();
    response.put("id", savedMeme.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
