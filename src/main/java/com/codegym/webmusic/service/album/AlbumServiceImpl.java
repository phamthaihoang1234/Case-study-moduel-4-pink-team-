package com.codegym.webmusic.service.album;


import com.codegym.webmusic.model.Album;
import com.codegym.webmusic.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    AlbumRepository albumRepository;

    @Override
    public Iterable<Album> findAll() {
        return albumRepository.findAll();
    }

    @Override
    public Optional<Album> findById(Long id) {
        return albumRepository.findById(id);
    }

    @Override
    public Album save(Album product) {
        return albumRepository.save(product);
    }

    @Override
    public void remove(Long id) {
        albumRepository.deleteById(id);
    }

    @Override
    public Page<Album> findAllByNameContains(String name, Pageable pageable) {
        return albumRepository.findAllByNameContains(name,pageable);
    }

    @Override
    public Page<Album> findAll(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }
}
