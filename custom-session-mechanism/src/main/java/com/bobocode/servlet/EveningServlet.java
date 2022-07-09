package com.bobocode.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static com.bobocode.servlet.HttpSessionUtils.SESSION_ID;

@WebServlet("/evening")
public class EveningServlet extends HttpServlet {

    private static final String PARAMETER = "name";
    private static final String DEFAULT_NAME = "Buddy";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (final PrintWriter writer = resp.getWriter()) {
            var requestParam = Optional.ofNullable(req.getParameter(PARAMETER));
            var sessionId = HttpSessionUtils.getSessionId(req.getCookies());
            var sessionAttribute = HttpSessionUtils.getAttribute(sessionId, PARAMETER);

            String name = requestParam.or(() -> sessionAttribute).orElse(DEFAULT_NAME);

            if (requestParam.isPresent()) {
                HttpSessionUtils.setAttribute(sessionId, PARAMETER, name);
                resp.addCookie(new Cookie(SESSION_ID, sessionId));
            }

            writer.printf("Good evening, %s\n", name);
            writer.flush();
        }
    }
}