package com.example.snsproject.service;

import com.example.snsproject.exception.ErrorCode;
import com.example.snsproject.exception.SnsApplicationException;
import com.example.snsproject.model.AlarmArgs;
import com.example.snsproject.model.Comment;
import com.example.snsproject.model.Post;
import com.example.snsproject.model.entity.*;
import com.example.snsproject.model.event.AlarmEvent;
import com.example.snsproject.producer.AlarmProducer;
import com.example.snsproject.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final AlarmService alarmService;
    private final AlarmProducer alarmProducer;

    @Transactional
    public void create(String title, String body, String userName) {
        UserEntity userEntity = getUserOrException(userName);
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public Post modify(Long postId, String title, String body, Long userId) {
        PostEntity postEntity = getPostOrException(postId);

        if (!Objects.equals(postEntity.getUser().getId(), userId)) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with %s", userId, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntity);
    }

    @Transactional
    public void delete(Long userId, Long postId) {
        PostEntity postEntity = getPostOrException(postId);

        if (!Objects.equals(postEntity.getUser().getId(), userId)) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with %s", userId, postId));
        }

        likeEntityRepository.deleteAllByPost(postEntity);
        commentEntityRepository.deleteAllByPost(postEntity);

        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(Long userId, Pageable pageable) {
        return postEntityRepository.findAllByUserId(userId, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Long postId, String userName) {
        UserEntity userEntity = getUserOrException(userName);
        PostEntity postEntity = getPostOrException(postId);

        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like post %d", userName, postId));
        });

        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));

        alarmProducer.send(new AlarmEvent(postEntity.getUser().getId(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public int likeCount(Long postId) {
        PostEntity postEntity = getPostOrException(postId);
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Long postId, String userName, String comment) {
        UserEntity userEntity = getUserOrException(userName);
        PostEntity postEntity = getPostOrException(postId);

        commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comment));

        alarmProducer.send(new AlarmEvent(postEntity.getUser().getId(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public Page<Comment> getComments(Long postId, Pageable pageable) {
        PostEntity postEntity = getPostOrException(postId);
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    private PostEntity getPostOrException(Long postId) {
        return postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
    }

    private UserEntity getUserOrException(String userName) {
        return userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }
}
