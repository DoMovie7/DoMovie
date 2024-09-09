package com.red.domovie.controller;

import com.red.domovie.domain.dto.hometheater.*;
import com.red.domovie.domain.entity.hometheater.Category;

import com.red.domovie.service.hometheater.HomeTheaterServicePrecess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hometheater")
@RequiredArgsConstructor
public class HomeTheaterController {

    private final HomeTheaterServicePrecess homeTheaterServicePrecess;

    @GetMapping
    public String homeTheaterMain(Model model) {
        List<Category> categories = homeTheaterServicePrecess.getAllCategories();
        model.addAttribute("categories", categories);
        return "views/hometheater/hometheater"; // 홈시어터 메인 페이지
    }

    @GetMapping("/list")
    public String listPosts(Model model) {
        List<HomeTheaterListDTO> posts = homeTheaterServicePrecess.getAllPosts();
        List<Category> categories = homeTheaterServicePrecess.getAllCategories();
        model.addAttribute("posts", posts);
        model.addAttribute("categories", categories);
        return "views/hometheater/hometheater_list"; // 게시물 목록 페이지
    }

    @GetMapping("/hometheater_create")
    @PreAuthorize("isAuthenticated()")
    public String createPostForm(Model model) {
        model.addAttribute("homeTheaterSaveDTO", new HomeTheaterSaveDTO());
        model.addAttribute("categories", homeTheaterServicePrecess.getAllCategories());
        return "views/hometheater/hometheater_create";
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public String createPost(@ModelAttribute HomeTheaterSaveDTO homeTheaterSaveDTO,
                             ItemImageSaveDTO itemImageSaveDTO,
                             @AuthenticationPrincipal UserDetails userDetails) {
        homeTheaterServicePrecess.createPost(homeTheaterSaveDTO, itemImageSaveDTO, userDetails.getUsername());
        return "redirect:/hometheater/list";
    }


    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {
        HomeTheaterDetailDTO post = homeTheaterServicePrecess.getPostById(id);
        List<Category> categories = homeTheaterServicePrecess.getAllCategories();
        model.addAttribute("post", post);
        model.addAttribute("categories", categories);
        model.addAttribute("commentForm", new CommentSaveDTO());
        return "views/hometheater/hometheater_detail"; // 상세 페이지
    }
    @PostMapping("/{id}/comment")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@PathVariable("id") Long id, @ModelAttribute CommentSaveDTO commentForm,
                             @AuthenticationPrincipal UserDetails userDetails) {
        commentForm.setAuthor(userDetails.getUsername());  // 이 줄을 추가하세요
        homeTheaterServicePrecess.addComment(id,commentForm);
        return "redirect:/hometheater/" + id;
    }
    @PutMapping("/api/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePostAsync(@PathVariable("id") Long id,
                                             @RequestBody HomeTheaterUpdateDTO updateDTO,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            HomeTheaterDetailDTO updatedPost = homeTheaterServicePrecess.updatePost(id, updateDTO, userDetails);
            if (updatedPost != null) {
                // ResponseEntity.ok() 대신 직접 ResponseEntity 객체 생성
                return new ResponseEntity<>(updatedPost, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You don't have permission to update this post.", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update post: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/api/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            boolean isDeleted = homeTheaterServicePrecess.deletePost(id, userDetails.getUsername());
            if (isDeleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this post.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete post: " + e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/temp-upload")
    public Map<String,String> tempUpload(@RequestParam(name = "itemImage") MultipartFile itemImage){
        return homeTheaterServicePrecess.tempUploadProcess(itemImage);
    }


}