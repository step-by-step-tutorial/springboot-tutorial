package com.tutorial.springboot.securityoauth2client.api.impl;

import com.tutorial.springboot.securityoauth2client.repository.JdbcClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static com.tutorial.springboot.securityoauth2client.util.SecurityUtils.getCurrentOAuth2Authentication;
import static java.util.stream.Collectors.toMap;
import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

@Controller
public class LoginController {

    private final JdbcClientRegistrationRepository clientRegistrationRepository;

    public LoginController(JdbcClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/login")
    public String login(Model model) {
        var clients = StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(clientRegistrationRepository.iterator(), Spliterator.ORDERED), false)
                .collect(toMap(client -> client.getClientName(), client -> DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/" + client.getRegistrationId()));
        model.addAttribute("clientMap", clients);

        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("dto", getCurrentOAuth2Authentication());
        return "index";
    }
}
