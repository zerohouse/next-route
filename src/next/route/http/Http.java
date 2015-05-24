package next.route.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import next.route.setting.Setting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Http {

	private final static Logger logger = LoggerFactory.getLogger(Http.class);

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private Map<String, String> uriValueMap;
	private Queue<String> uriValueQue;

	public String getParameter(String name) {
		return req.getParameter(name);
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public HttpServletResponse getResp() {
		return resp;
	}

	public <T> T getJsonObject(Class<T> cLass, String name) {
		Gson gson = Setting.getGson();
		try {
			return gson.fromJson(req.getParameter(name), cLass);
		} catch (JsonSyntaxException e) {
			logger.warn(e.getMessage());
			return null;
		}
	}

	// [TODO]중복코드 수정
	public <T> T getJsonObject(Class<T> cLass) {
		Gson gson = Setting.getGson();
		try {
			return gson.fromJson(gson.toJson(req.getParameterMap()), cLass);
		} catch (JsonSyntaxException e) {
			logger.warn(e.getMessage());
			return null;
		}
	}

	public Http(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
	}

	public void forword(String path) {
		if (path.equals(""))
			sendError(508);
		RequestDispatcher rd = req.getRequestDispatcher(path);
		try {
			rd.forward(req, resp);
		} catch (ServletException e) {
			logger.warn(e.getMessage());
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	public void setContentType(String type) {
		resp.setContentType(type);
	}

	public void write(String string) {
		try {
			resp.getWriter().write(string);
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	public void putUriValue(String key, String uriVariable) {
		if (uriValueMap == null)
			uriValueMap = new HashMap<String, String>();
		uriValueMap.put(key, uriVariable);
		if (uriValueQue == null)
			uriValueQue = new LinkedList<String>();
		uriValueQue.add(uriVariable);
	}

	public String getUriValue(String key) {
		if (uriValueMap == null)
			return null;
		return uriValueMap.get(key);
	}
	
	public String getUriValue() {
		if (uriValueQue == null)
			return null;
		return uriValueQue.poll();
	}

	public void setCharacterEncoding(String encording) {
		try {
			req.setCharacterEncoding(encording);
			resp.setCharacterEncoding(encording);
		} catch (UnsupportedEncodingException e) {
			logger.warn(e.getMessage());
		}
	}

	public void sendNotFound() {
		try {
			resp.sendError(404, req.getRequestURI());
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	public void setSessionAttribute(String name, Object value) {
		req.getSession().setAttribute(name, value);
	}

	public void removeSessionAttribute(String name) {
		req.getSession().removeAttribute(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSessionAttribute(Class<T> cLass, String name) {
		return (T) req.getSession().getAttribute(name);
	}

	public Object getSessionAttribute(String name) {
		return req.getSession().getAttribute(name);
	}

	public void sendRedirect(String location) {
		if (location.equals(""))
			sendError(508);
		try {
			resp.sendRedirect(location);
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	public void sendError(int errorNo) {
		try {
			resp.sendError(errorNo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendError(int errorNo, String errorMesage) {
		try {
			resp.sendError(errorNo, errorMesage);
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	public void setAttribute(String key, Object value) {
		req.setAttribute(key, value);
	}

	public Object getAttribute(String key) {
		return req.getAttribute(key);
	}

	public int getUriValueSize() {
		if (uriValueMap == null)
			return 0;
		return uriValueMap.size();
	}

	public Collection<Part> getParts() {
		try {
			return req.getParts();
		} catch (IOException | ServletException e) {
			logger.warn(e.getMessage());
		}
		return null;
	}

	public Part getPart(String name) {
		try {
			return req.getPart(name);
		} catch (IOException | ServletException e) {
			logger.warn(e.getMessage());
		}
		return null;
	}

	public <T> T getObjectFromParameterMap(Class<T> type) {
		ParameterMapToObject parser = new ParameterMapToObject();
		return parser.makeFromParameter(req.getParameterMap(), type);
	}

}
