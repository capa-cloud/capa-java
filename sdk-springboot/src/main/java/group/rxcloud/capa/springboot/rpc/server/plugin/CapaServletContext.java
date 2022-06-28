package group.rxcloud.capa.springboot.rpc.server.plugin;

import javax.servlet.http.HttpServletRequest;

/**
 * The Capa servlet context plugin.
 */
public interface CapaServletContext {

    /**
     * Gets servlet request.
     *
     * @return the servlet request
     */
    HttpServletRequest getServletRequest();
}
