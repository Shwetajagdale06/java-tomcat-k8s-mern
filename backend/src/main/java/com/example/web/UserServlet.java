package com.example.web;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.*;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private Connection conn;

    public void init() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://postgres:5432/usersdb", "user", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            out.print("[");
            while (rs.next()) {
                out.print("{\"id\":" + rs.getInt("id") + ",\"name\":\"" + rs.getString("name") + "\"},");
            }
            out.print("]");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String name = req.getParameter("name");
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users(name) VALUES (?)");
            ps.setString(1, name);
            ps.executeUpdate();
            res.getWriter().print("Inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
