package com.example.snsproject.controller;

import com.example.snsproject.controller.request.PostCommentRequest;
import com.example.snsproject.controller.request.PostCreateRequest;
import com.example.snsproject.controller.response.CommentResponse;
import com.example.snsproject.controller.response.PostResponse;
import com.example.snsproject.controller.response.Response;
import com.example.snsproject.model.Post;
import com.example.snsproject.model.User;
import com.example.snsproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable, @AuthenticationPrincipal User user) {
        return Response.success(postService.my(user.getId(), pageable).map(PostResponse::fromPost));
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Long postId, @RequestBody PostCreateRequest request, @AuthenticationPrincipal User user) {
        Post post = postService.modify(postId, request.getTitle(), request.getBody(), user.getId());
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        postService.delete(user.getId(), postId);
        return Response.success();
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Long postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Integer> likeCount(@PathVariable Long postId) {
        return Response.success(postService.likeCount(postId));
    }

    @PostMapping("/{postId}/comments")
    public Response<Void> comment(@PathVariable Long postId, @RequestBody PostCommentRequest request, Authentication authentication) {
        postService.comment(postId, authentication.getName(), request.getComment());
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> getComments(@PathVariable Long postId, Pageable pageable) {
        return Response.success(postService.getComments(postId, pageable).map(CommentResponse::fromComment));
    }
}
