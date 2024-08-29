package com.red.domovie.controller;

import com.red.domovie.domain.dto.hometheater.*;
import com.red.domovie.service.hometheater.HomeTheaterService;

import lombok.RequiredArgsConstructor;
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
    public String homeTheaterMain() {
        return "views/hometheater/hometheater"; // 홈시어터 메인 페이지
    }

    @GetMapping("/list")
    public String listPosts(Model model) {
        List<HomeTheaterListDTO> posts = homeTheaterService.getAllPosts();
        model.addAttribute("posts", posts);
        return "views/hometheater/hometheater_list"; // 게시물 목록 페이지
    }

    @GetMapping("/hometheater_create")
    public String createPostForm(Model model) {
        model.addAttribute("homeTheaterSaveDTO", new HomeTheaterSaveDTO());
        return "views/hometheater/hometheater_create"; // 글 작성 페이지
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute HomeTheaterSaveDTO homeTheaterSaveDTO,
                             @RequestParam("file") MultipartFile file) {
        homeTheaterService.createPost(homeTheaterSaveDTO, file); // 파일 포함
        return "redirect:/hometheater/list"; // 목록으로 리다이렉트
    }

    @GetMapping("/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        HomeTheaterUpdateDTO post = homeTheaterService.getPostForUpdate(id);
        model.addAttribute("postForm", post);
        return "views/hometheater/hometheater_edit"; // 수정 페이지
    }

    @PostMapping("/{id}/edit")
    public String editPost(@PathVariable Long id,
                           @ModelAttribute HomeTheaterUpdateDTO postForm,
                           @RequestParam("file") MultipartFile file) {
        homeTheaterService.updatePost(id, postForm, file); // 파일 포함
        return "redirect:/hometheater/" + id; // 상세 페이지로 리다이렉트
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        HomeTheaterDetailDTO post = homeTheaterService.getPostById(id);
        model.addAttribute("post", post);
        model.addAttribute("commentForm", new CommentSaveDTO());  // 이 줄을 추가합니다
        return "views/hometheater/hometheater_detail"; // 상세 페이지
    }
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, @ModelAttribute CommentSaveDTO commentForm) {
        homeTheaterService.addComment(id, commentForm);
        return "redirect:/hometheater/" + id;
    }
}