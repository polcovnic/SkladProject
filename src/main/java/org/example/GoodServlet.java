package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GoodServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(201);
        String jsonData = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        HashMap<String, String> jsonMap = new ObjectMapper().readValue(jsonData, HashMap.class);
        String name = jsonMap.get("name");
        String description = jsonMap.get("description");
        String producer = jsonMap.get("producer");
        String amount = jsonMap.get("amount");
        if (Integer.parseInt(amount) < 0) {
            resp.setStatus(409);
            return;
        }
        int id = Table.insert(name, description, producer, Integer.parseInt(amount));
        Map<String, Integer> responseJsonMap = new HashMap<>();
        responseJsonMap.put("id", id);
        String responseJson = new ObjectMapper().writeValueAsString(responseJsonMap);
        resp.getWriter().println(responseJson);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        String idStr = req.getPathInfo().substring(1);
        int id = Integer.parseInt(idStr);
        ResultSet rs = Table.selectById(id);
        try {
            rs.getString("name");
        } catch (SQLException | NullPointerException e) {
            resp.setStatus(404);
            return;
        }

        Map<String, String> responseJsonMap = new HashMap<>();
        try {
            responseJsonMap.put("name", rs.getString("name"));
            responseJsonMap.put("description", rs.getString("description"));
            responseJsonMap.put("producer", rs.getString("producer"));
            responseJsonMap.put("amount", String.valueOf(rs.getInt("amount")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String responseJson = new ObjectMapper().writeValueAsString(responseJsonMap);
        resp.getWriter().println(responseJson);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(204);
        String idStr = req.getPathInfo().substring(1);
        int id = Integer.parseInt(idStr);
        String jsonData = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        HashMap<String, String> jsonMap = new ObjectMapper().readValue(jsonData, HashMap.class);
        String name = jsonMap.get("name");
        String description = jsonMap.get("description");
        String producer = jsonMap.get("producer");
        String amount = jsonMap.get("amount");
        if (Integer.parseInt(amount) < 0) {
            resp.setStatus(409);
            return;
        }
        ResultSet rs = Table.selectById(id);
        try {
            rs.getString("name");
        } catch (SQLException | NullPointerException e) {
            resp.setStatus(404);
            return;
        }
        Table.update(id, name, description, producer, Integer.parseInt(amount));
        Map<String, String> responseJsonMap = new HashMap<>();
        responseJsonMap.put("message", "User has been successfully changed");
        String responseJson = new ObjectMapper().writeValueAsString(responseJsonMap);
        resp.getWriter().println(responseJson);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(204);

        String idStr = req.getPathInfo().substring(1);
        int id = Integer.parseInt(idStr);
        ResultSet rs = Table.selectById(id);
        if (rs == null) {
            resp.setStatus(404);
            return;
        }
        Table.delete(id);
        Map<String, String> responseJsonMap = new HashMap<>();
        responseJsonMap.put("message", "User has been successfully changed");
        String responseJson = new ObjectMapper().writeValueAsString(responseJsonMap);
        resp.getWriter().println(responseJson);
    }
}
