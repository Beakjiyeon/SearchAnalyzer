package kr.co.tbase.searchad.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        if (authentication.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인이 완료 되었습니다 .'); location.href='/admin/users';</script>");
            out.flush();
//            response.sendRedirect("/admin/users");
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인이 완료 되었습니다 .'); location.href='/search';</script>");
            out.flush();
//            response.sendRedirect("/search");
        }
    }

}
