package com.codegym.webmusic.controllers;


import com.codegym.webmusic.model.Album;
import com.codegym.webmusic.model.Singer;
import com.codegym.webmusic.service.album.AlbumService;
import com.codegym.webmusic.service.singer.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    AlbumService albumService;

    @Value("${upload.path}")
    private String fileUpload;

    @GetMapping("/create")
    public ModelAndView showCreateProduct() {
        ModelAndView modelAndView = new ModelAndView("/album/create");
        modelAndView.addObject("album", new Album());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveProduct(@ModelAttribute("album") Album album) {
        MultipartFile multipartFile = album.getImage();
        String fileName = multipartFile.getOriginalFilename();
//        String fileUpload = env.getProperty("file_upload");
        try {
            FileCopyUtils.copy(album.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        album.setImgSrc(fileName);
        albumService.save(album);
        ModelAndView modelAndView = new ModelAndView("/album/create");
        modelAndView.addObject("album", new Album());
        modelAndView.addObject("message", "New Album Created Successfully");
        return modelAndView;
    }

    @GetMapping
    public ModelAndView listAlbums(@RequestParam("searchName") Optional<String> name, @PageableDefault(value = 3) Pageable pageable) {
        Page<Album> albums; // Tạo đối tượng lưu Page singers;
        if (name.isPresent()) {
            // Kiểm tra xem nếu Parameter search được truyền vào thì gọi service có 2 tham số
            albums= albumService.findAllByNameContains(name.get(), pageable);
        } else {
            // Nếu không có search thì gọi service có 1 tham số
            albums = albumService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("album/list");
        modelAndView.addObject("albums", albums);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Album album= albumService.findById(id).get();
        model.addAttribute("album", album);
        return "album/view";
    }

    @GetMapping("/edit-album/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<Album> album = albumService.findById(id);
        if (album.isPresent()) {
            model.addAttribute("album", album.get());
            return "album/edit";
        } else {
            redirect.addFlashAttribute("message", "Album not found!");
            return "redirect:/albums";
        }
    }

    @PostMapping("/edit-album")
    public ModelAndView updateBlog(@ModelAttribute("album") Album album) {
        albumService.save(album);

        ModelAndView modelAndView = new ModelAndView("album/edit");
        modelAndView.addObject("album", album);
        modelAndView.addObject("message", "Album updated sucessfully");
        return modelAndView;
    }

    @GetMapping("/delete-album/{id}")
    public String showDeleteForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
        Optional<Album> album = albumService.findById(id);
        if (album.isPresent()) {
            model.addAttribute("album", album.get());
            return "album/delete";
        } else {
            redirect.addFlashAttribute("message", "Album not found");
            return "redirect:/albums";
        }
    }

    @PostMapping("delete-album")
    public String deleteBlog(@ModelAttribute("album") Album album, RedirectAttributes redirect) {
        albumService.remove(album.getId());
        redirect.addFlashAttribute("message", "Deleted successfully.");
        return "redirect:/albums";
    }
}
