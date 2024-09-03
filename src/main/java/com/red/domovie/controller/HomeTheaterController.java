package com.red.domovie.controller;

import com.red.domovie.domain.dto.hometheater.*;
import com.red.domovie.domain.entity.hometheater.Category;
import com.red.domovie.service.hometheater.HomeTheaterService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/hometheater")
@RequiredArgsConstructor
public class HomeTheaterController {

    private final HomeTheaterService homeTheaterService;

    @GetMapping
    public String homeTheaterMain(Model model) {
        List<Category> categories = homeTheaterService.getAllCategories();
        model.addAttribute("categories", categories);
        return "views/hometheater/hometheater"; // 홈시어터 메인 페이지
    }

    @GetMapping("/list")
    public String listPosts(Model model) {
        List<HomeTheaterListDTO> posts = homeTheaterService.getAllPosts();
        List<Category> categories = homeTheaterService.getAllCategories();
        model.addAttribute("posts", posts);
        model.addAttribute("categories", categories);
        return "views/hometheater/hometheater_list"; // 게시물 목록 페이지
    }

    @GetMapping("/hometheater_create")
    public String createPostForm(Model model) {
        model.addAttribute("homeTheaterSaveDTO", new HomeTheaterSaveDTO());
        model.addAttribute("categories", homeTheaterService.getAllCategories());
        return "views/hometheater/hometheater_create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute HomeTheaterSaveDTO homeTheaterSaveDTO,
                             @RequestParam("file") MultipartFile file) {
        homeTheaterService.createPost(homeTheaterSaveDTO, file); // 파일 포함
        return "redirect:/hometheater/list"; // 목록으로 리다이렉트
    }

    @PutMapping("/{id}")
    public String editPostForm(@PathVariable("id") Long id, @ModelAttribute HomeTheaterUpdateDTO updateDTO) {
        homeTheaterService.updatePost(id,updateDTO);
        return "redirect:/hometheater/{id}"; // 수정 페이지
    }



    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {
        HomeTheaterDetailDTO post = homeTheaterService.getPostById(id);
        List<Category> categories = homeTheaterService.getAllCategories();
        model.addAttribute("post", post);
        model.addAttribute("categories", categories);
        model.addAttribute("commentForm", new CommentSaveDTO());
        return "views/hometheater/hometheater_detail"; // 상세 페이지
    }
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable("id") Long id, @ModelAttribute CommentSaveDTO commentForm) {
        homeTheaterService.addComment(id,commentForm);
        return "redirect:/hometheater/" + id;
    }
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> updatePostAsync(@PathVariable("id") Long id, @RequestBody HomeTheaterUpdateDTO updateDTO) {
        try {
            homeTheaterService.updatePost(id, updateDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update post: " + e.getMessage());
        }
    }

}