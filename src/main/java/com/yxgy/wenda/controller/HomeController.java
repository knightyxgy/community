package com.yxgy.wenda.controller;

import com.yxgy.wenda.entity.DiscussPost;
import com.yxgy.wenda.entity.Page;
import com.yxgy.wenda.service.DiscussPostService;
import com.yxgy.wenda.service.LikeService;
import com.yxgy.wenda.service.UserService;
import com.yxgy.wenda.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page) {
        //方法调用前，springmvc会自动实例化model和page，并将page注入model
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            list.stream().forEach((post) -> {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                map.put("user", userService.findUserById(post.getUserId()));
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
                discussPosts.add(map);
            });
        }
        model.addAttribute("discussPosts", discussPosts);

        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "error/500";
    }
}
