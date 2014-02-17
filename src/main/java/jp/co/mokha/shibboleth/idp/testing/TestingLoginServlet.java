/*
 * Copyright 2014 mokha Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.mokha.shibboleth.idp.testing;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.internet2.middleware.shibboleth.idp.authn.AuthenticationEngine;
import edu.internet2.middleware.shibboleth.idp.authn.AuthenticationException;
import edu.internet2.middleware.shibboleth.idp.authn.LoginHandler;
import edu.internet2.middleware.shibboleth.idp.authn.UsernamePrincipal;
import edu.internet2.middleware.shibboleth.idp.authn.provider.UsernamePasswordCredential;

/**
 * This Servlet authenticates a user using a single, fixed password.
 */
public class TestingLoginServlet extends HttpServlet {
    private static final long serialVersionUID = -169133028626285156L;

    private final Logger log = LoggerFactory.getLogger(TestingLoginServlet.class);

    private static final String DEFAULT_USERNAME = "test001";
    private static final String PASSWORD = "eh74waEP";
    private final String LOGIN_PAGE_INIT_PARAM = "loginPage";
    private final String FAILURE_PARAM = "loginFailed";

    private String authenticationMethod;
    private String loginPage = "testing.jsp";
    private final String usernameAttribute = "j_username";
    private final String passwordAttribute = "j_password";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (getInitParameter(LOGIN_PAGE_INIT_PARAM) != null) {
            loginPage = getInitParameter(LOGIN_PAGE_INIT_PARAM);
        }
        if (!loginPage.startsWith("/")) {
            loginPage = "/" + loginPage;
        }
        
        final String method =
                DatatypeHelper.safeTrimOrNullString(
                        config.getInitParameter(LoginHandler.AUTHENTICATION_METHOD_KEY));
        if (method != null) {
            authenticationMethod = method;
        } else {
            authenticationMethod = "urn:oasis:names:tc:SAML:2.0:ac:classes:Token";
        }
        log.debug("Using authnMethod \"{}\"", authenticationMethod);
    }

    /** {@inheritDoc} */
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String password = request.getParameter(passwordAttribute);
        if (password == null) {
            redirectToLoginPage(request, response);
            return;
        }

        String username = request.getParameter(usernameAttribute);
        if (username == null || username.isEmpty()) {
            log.debug("using username \"{}\"", DEFAULT_USERNAME);
            username = DEFAULT_USERNAME;
        }
        if (PASSWORD.equals(password)) {
            request.setAttribute(LoginHandler.PRINCIPAL_NAME_KEY, username);
            request.setAttribute(LoginHandler.AUTHENTICATION_METHOD_KEY, authenticationMethod);
            AuthenticationEngine.returnToAuthenticationEngine(request, response);
        } else {
            log.debug("Login failed.");
            request.setAttribute(FAILURE_PARAM, "true");
            LoginException e = new LoginException("Wow, you entered a wrong password!");
            request.setAttribute(LoginHandler.AUTHENTICATION_EXCEPTION_KEY,
                    new AuthenticationException(e));
            redirectToLoginPage(request, response);
        }
    }

    protected void redirectToLoginPage(HttpServletRequest request, HttpServletResponse response) {
        final StringBuilder actionUrlBuilder = new StringBuilder();
        if(!"".equals(request.getContextPath())){
            actionUrlBuilder.append(request.getContextPath());
        }
        actionUrlBuilder.append(request.getServletPath());

        request.setAttribute("actionUrl", actionUrlBuilder.toString());

        try {
            request.getRequestDispatcher(loginPage).forward(request, response);
            log.debug("Redirecting to login TestingAuth page {}", loginPage);
        } catch (IOException ex) {
            log.error("Unable to redirect to login page.", ex);
        } catch (ServletException ex) {
            log.error("Unable to redirect to login page.", ex);
        }
    }
}