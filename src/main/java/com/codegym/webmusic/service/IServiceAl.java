package com.codegym.webmusic.service;

import com.codegym.webmusic.model.Album;
import com.codegym.webmusic.model.Singer;

import java.util.Optional;

public interface IServiceAl<E> {
    Iterable<E> findAll();
    //    E findById(Long id);
    Optional<Album> findById(Long id);

    E save(E e);
    void remove(Long id);
}
