package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class LoginServlet extends HttpServlet {

    public LoginServlet() {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonData = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        HashMap<String, String> jsonMap = new ObjectMapper().readValue(jsonData, HashMap.class);
        String login = jsonMap.get("login");
        String password = jsonMap.get("password");
        if (login.equals("log") && password.equals("pass")) {
            Map<String, String> responseJsonMap = new HashMap<>();
            responseJsonMap.put("JWT", JWT.createJWT("1", "issue", login, 1000000));
            String responseJson = new ObjectMapper().writeValueAsString(responseJsonMap);
            resp.getWriter().println(responseJson);
            resp.setStatus(200);
        } else {
            resp.setStatus(401);
        }

    }

}
