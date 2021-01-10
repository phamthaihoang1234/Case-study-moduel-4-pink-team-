package com.codegym.webmusic.service.album;


import com.codegym.webmusic.model.Album;
import com.codegym.webmusic.service.IServiceAl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlbumService extends IServiceAl<Album> {
    Page<Album> findAllByNameContains(String name, Pageable pageable);

    Page<Album> findAll(Pageable pageable);
}
