package com.carspot.app.service.impl;

import com.carspot.app.entity.Post;
import com.carspot.app.entity.User;
import com.carspot.app.entity.WatchlistPost;
import com.carspot.app.exception.exceptions.PostNotFoundException;
import com.carspot.app.repository.PostRepository;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.repository.WatchlistPostRepository;
import com.carspot.app.service.PostService;
import com.carspot.app.service.WatchlistPostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WatchlistPostServiceImpl implements WatchlistPostService {

    private final WatchlistPostRepository watchlistPostRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostService postService;


    @Override
    public Page<Post> findUserWatchListPosts(String emailAddress, Pageable pageable) {

        User user = userRepository.findUserByEmailAddress(emailAddress);

        List<WatchlistPost> postIdList = watchlistPostRepository.findAllByUserId(user.getId());

        Page<Post> watchlistPostsPage = postRepository.findAllByIdIn(postIdList.stream().map(post -> post.getPostId()).collect(Collectors.toList()), pageable);
        if (watchlistPostsPage.isEmpty()) {
            throw new PostNotFoundException("You do not have any Watchlisted Posts.");
        }


        return watchlistPostsPage;
    }


    @Override
    public Integer findUserWatchlistPostsCount(String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);
        if (user == null) {
            throw new UsernameNotFoundException("User was not found");
        }
        return watchlistPostRepository.countAllByUserId(user.getId());
    }

    @Override
    public WatchlistPost addWatchlistPost(Long postId, String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);
        Post post = postService.findPostById(postId);

        WatchlistPost watchlistPost = new WatchlistPost();
        watchlistPost.setPostId(post.getId());
        watchlistPost.setUserId(user.getId());

        if (emailAddress.equals(post.getPostCreatorEmail())) {
            throw new PostNotFoundException("You cannot Watchlist your own post");
        }

        watchlistPostRepository.save(watchlistPost);

        return watchlistPost;

    }


    @Override
    public void deleteMultipleWatchlistPosts(List<Long> watchlistPostIdList, String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);
        List<WatchlistPost> posts = new ArrayList<>();

        for(int i = 0; i < watchlistPostIdList.size(); i++) {
            WatchlistPost watchlistPost = watchlistPostRepository.findFirstByPostIdAndUserId(watchlistPostIdList.get(i), user.getId());
            if (watchlistPost == null) {
                throw new PostNotFoundException("Watchlist Post with id " + watchlistPostIdList.get(i) + " was not found");
            }
            posts.add(watchlistPost);
        }

        watchlistPostRepository.deleteAll(posts);

    }
}
