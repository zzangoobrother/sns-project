package com.example.snsproject.controller;

import com.example.snsproject.controller.request.PostCreateRequest;
import com.example.snsproject.controller.response.PostResponse;
import com.example.snsproject.controller.response.Response;
import com.example.snsproject.model.Post;
import com.example.snsproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Long postId, @RequestBody PostCreateRequest request, Authentication authentication) {
        Post post = postService.modify(postId, request.getTitle(), request.getBody(), authentication.getName());
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();
    }
}
