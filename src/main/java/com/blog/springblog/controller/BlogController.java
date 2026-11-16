package com.blog.springblog.controller;

import com.blog.springblog.dto.CommentForm;
import com.blog.springblog.dto.PostForm;
import com.blog.springblog.entity.Comment;
import com.blog.springblog.entity.Post;
import com.blog.springblog.entity.User;
import com.blog.springblog.repository.CommentRepository;
import com.blog.springblog.repository.PostRepository;
import com.blog.springblog.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
public class BlogController {

    private static final int POSTS_PER_PAGE = 5;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public BlogController(PostRepository postRepository,
            UserRepository userRepository,
            CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // ---------- Listado paginado ----------
    @GetMapping("/blog")
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0)
            page = 0;
        Page<Post> posts = postRepository.findAllByOrderByPublishedAtDesc(
                PageRequest.of(page, POSTS_PER_PAGE));
        model.addAttribute("posts", posts);
        return "blog/list";
    }

    // ---------- Una entrada con sus comentarios ----------
    @GetMapping("/blog/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = findPost(id);
        model.addAttribute("commentForm", new CommentForm());
        fillDetail(post, model);
        return "blog/detail";
    }

    // ---------- Comentar (usuario identificado) ----------
    @PostMapping("/blog/{id}/comments")
    public String addComment(@PathVariable Long id,
            @Valid @ModelAttribute("commentForm") CommentForm form,
            BindingResult result,
            Principal principal,
            Model model) {
        Post post = findPost(id);

        if (result.hasErrors()) {
            fillDetail(post, model);
            return "blog/detail";
        }

        User author = userRepository.findByEmail(principal.getName()).orElseThrow();

        Comment comment = new Comment();
        comment.setContent(form.getContent());
        comment.setPost(post);
        comment.setAuthor(author);
        commentRepository.save(comment);

        return "redirect:/blog/" + id + "#comentarios";
    }

    // ---------- Borrar comentario (solo admin) ----------
    @PostMapping("/blog/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
        return "redirect:/blog/" + postId + "#comentarios";
    }

    // ---------- Crear entrada (solo admin) ----------
    @GetMapping("/blog/new")
    public String newForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "blog/new_post";
    }

    @PostMapping("/blog/new")
    public String create(@Valid @ModelAttribute("postForm") PostForm form,
            BindingResult result,
            Principal principal) {
        if (result.hasErrors()) {
            return "blog/new_post";
        }

        User author = userRepository.findByEmail(principal.getName()).orElseThrow();

        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setAuthor(author);
        postRepository.save(post);

        return "redirect:/blog/" + post.getId();
    }

    // ---------- Auxiliares ----------
    private Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void fillDetail(Post post, Model model) {
        model.addAttribute("post", post);
        model.addAttribute("comments", commentRepository.findByPostIdOrderByCreatedAtAsc(post.getId()));
    }
}