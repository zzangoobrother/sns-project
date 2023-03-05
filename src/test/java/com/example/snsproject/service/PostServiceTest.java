package com.example.snsproject.service;

import com.example.snsproject.exception.ErrorCode;
import com.example.snsproject.exception.SnsApplicationException;
import com.example.snsproject.fixture.PostEntityFixture;
import com.example.snsproject.fixture.UserEntityFixture;
import com.example.snsproject.model.entity.PostEntity;
import com.example.snsproject.model.entity.UserEntity;
import com.example.snsproject.repository.PostEntityRepository;
import com.example.snsproject.repository.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;
    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void 게시글작성이_성공한경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        assertDoesNotThrow(() -> postService.create(title, body, userName));
    }

    @Test
    void 게시글작성시_요청한유저가_존재하지않은경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 게시글수정이_성공한경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1L);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        assertDoesNotThrow(() -> postService.modify(postId, title, body, userName));
    }

    @Test
    void 게시글수정시_게시글이_존재하지않는_경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1L);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () -> postService.modify(postId, title, body, userName));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 게시글수정시_권한이_없는_경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1L);
        UserEntity writer = UserEntityFixture.get("userName1", "password", 2L);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () -> postService.modify(postId, title, body, userName));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void 게시글삭제가_성공한경우() {
        String userName = "userName";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1L);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));


        assertDoesNotThrow(() -> postService.delete(userName, postId));
    }

    @Test
    void 게시글삭제시_게시글이_존재하지않는_경우() {
        String userName = "userName";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1L);
        UserEntity userEntity = postEntity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () -> postService.delete(userName, postId));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 게시글삭제시_권한이_없는_경우() {
        String userName = "userName";
        Long postId = 1L;

        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1L);
        UserEntity writer = UserEntityFixture.get("userName1", "password", 2L);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException exception = assertThrows(SnsApplicationException.class, () -> postService.delete(userName, postId));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void 게시글목록조회_성공한경우() {
        Pageable pageable = mock(Pageable.class);
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    void 내게시글목록조회_성공한경우() {
        Pageable pageable = mock(Pageable.class);
        when(postEntityRepository.findAllByUser(any(), pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> postService.my("", pageable));
    }
}
