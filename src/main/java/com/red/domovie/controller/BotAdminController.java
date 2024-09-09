package com.red.domovie.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.red.domovie.domain.dto.bot.FAQDTO;
import com.red.domovie.domain.dto.chat.ChatRoomDTO;
import com.red.domovie.domain.dto.chat.ChattingDTO;
import com.red.domovie.domain.dto.chat.ChattingRoomDTO;
import com.red.domovie.domain.dto.chat.PageDTO;
import com.red.domovie.domain.entity.FAQEntity;
import com.red.domovie.service.ChatService;
import com.red.domovie.service.FAQService;


@Controller
@RequiredArgsConstructor
public class BotAdminController {
	
	private final FAQService faqService;
	private final ChatService chatService;
	
	@GetMapping("/admin")
	public String mainPage() {
		return "views/admin/admin-main";
	}
	
	@GetMapping("/admin/faqs")
	public String faqs(Model model) {
		List<FAQDTO> faqList = faqService.getAllFAQs();
		System.out.println(faqList);
        model.addAttribute("faqList", faqList);
		return "views/admin/faq";
	}
	
	@PostMapping("/admin/faqs/first")
    public String addFirstFAQ(@RequestParam(name = "name") String name) {
		faqService.addFirstFAQ(name);
        return "redirect:/admin/faqs";
    }
	
	@DeleteMapping("/admin/faqs/{id}")
    @ResponseBody
    public void deleteFAQ(@PathVariable(name="id") Long id) {
		
        faqService.deleteFAQ(id);
    }
	
	//////////////////////////////
    @PutMapping("/admin/faqs/{parentId}")
    @ResponseBody
    public FAQDTO updateFAQ(@PathVariable Long parentId, @RequestBody FAQDTO faqDTO) {
        return faqService.updateFAQ(parentId, faqDTO);
    }
	//////////////////////////////
	
    @GetMapping("/admin/chat/list")
    public String chatList(Model model, 
                           @RequestParam(name = "page", defaultValue = "1") int page, 
                           @RequestParam(name = "size", defaultValue = "8") int size) {
        PageDTO pageDTO = chatService.findAllChattingRoom(page, size);
        model.addAttribute("pageDTO", pageDTO);
        return "views/admin/chat-list";
    }
	
	@GetMapping("/admin/chat/{roomId}")
	public String chatting(@PathVariable("roomId") String roomId, Model model) {
		ChatRoomDTO roomInfo = chatService.findRoomInfo(roomId);
		model.addAttribute("roomInfo", roomInfo);
		List<ChattingDTO> chattingDTO = chatService.findChatByRoomId(roomId);
		model.addAttribute("chattingDTO", chattingDTO);
		return "views/admin/chat";
	}
	

}
