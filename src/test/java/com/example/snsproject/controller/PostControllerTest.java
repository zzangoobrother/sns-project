package com.example.snsproject.controller;

import com.example.snsproject.controller.request.PostCommentRequest;
import com.example.snsproject.controller.request.PostCreateRequest;
import com.example.snsproject.controller.request.PostModifyRequest;
import com.example.snsproject.exception.ErrorCode;
import com.example.snsproject.exception.SnsApplicationException;
import com.example.snsproject.fixture.PostEntityFixture;
import com.example.snsproject.model.Post;
import com.example.snsproject.model.User;
import com.example.snsproject.model.entity.UserEntity;
import com.example.snsproject.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.of("", "");
        userEntity.setId(1L);
    }

    @Test
    @WithMockUser
    void ???????????????() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void ??????????????????_???????????????????????????() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ???????????????() throws Exception {
        String title = "title";
        String body = "body";

        when(postService.modify(any(), eq(title), eq(body), any())).thenReturn(Post.fromEntity(PostEntityFixture.get("userName", 1L, 1L)));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(User.fromEntity(userEntity)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void ??????????????????_???????????????????????????() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ??????????????????_?????????_?????????_??????_????????????_????????????() throws Exception {
        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(1L), eq(title), eq(body), anyLong());

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(User.fromEntity(userEntity)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ??????????????????_???????????????_??????_????????????_????????????() throws Exception {
        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(1L), eq(title), eq(body), any());

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(User.fromEntity(userEntity)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void ???????????????() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(User.fromEntity(userEntity)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void ??????????????????_???????????????_????????????() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ??????????????????_????????????_??????????????????_????????????() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(User.fromEntity(userEntity)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ??????????????????_???????????????_????????????_????????????_??????_??????() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(User.fromEntity(userEntity)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void ???????????????() throws Exception {
        when(postService.list(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void ????????????????????????_???????????????_????????????() throws Exception {
        when(postService.list(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ??????????????????() throws Exception {
        when(postService.my(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                        .with(SecurityMockMvcRequestPostProcessors.user(User.fromEntity(userEntity)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void ???????????????????????????_???????????????_????????????() throws Exception {
        when(postService.my(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ???????????????() throws Exception {
        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void ????????????????????????_???????????????_????????????() throws Exception {
        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ????????????????????????_????????????_????????????() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).like(any(), any());

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void ????????????() throws Exception {
        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void ???????????????_???????????????_????????????() throws Exception {
        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void ???????????????_????????????_????????????() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).comment(any(), any(), any());

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment"))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
