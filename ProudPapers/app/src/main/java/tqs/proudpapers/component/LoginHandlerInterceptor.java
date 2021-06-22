package tqs.proudpapers.component;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wy
 * @date 2021/6/14 19:45
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("client");
        if (user == null){
            request.setAttribute("Message","Please login");
            request.getRequestDispatcher("/login").forward(request,response);
            return false;
        }else {
            return true;
        }
    }
}
