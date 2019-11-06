package com.crrs.util;

import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class WebUtil {
    /**
     * 返回结果输出
     * @param result
     * @param error
     * @param response
     */
    public static void packResponse(JSONObject result, int code, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type");
        PrintWriter out = null;
        try {
            result.put("code", code);
            out = response.getWriter();
            out.write(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 返回结果输出
     * @param result
     * @param error
     * @param response
     */
    public static void packResponse(String result, int code, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type");
        PrintWriter out = null;
        try {
            //result.put("code", code);
            out = response.getWriter();
            out.write(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    public static void packJsonPResponse(JSONObject result, int code, HttpServletRequest request,
                                         HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type");
        PrintWriter out = null;
        try {
            result.put("code", Integer.valueOf(code));
            out = response.getWriter();
            String callback = request.getParameter("callback");
            boolean hascallback = StringUtils.hasText(request.getParameter("callback"));
            if (hascallback) {
                out.write(callback+"(");
            }
            out.write(result.toString());
            if (hascallback) {
                out.write(")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                out.close();
        }
    }

    /**
     * 返回结果输出 android专用
     * @param result
     * @param error
     * @param response
     */
    public static void packResponseAndroid(JSONObject result, int code, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type");
        PrintWriter out = null;
        try {
            result.put("code", code);
            out = response.getWriter();
            out.write("["+result.toString()+"]");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
