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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.internet2.middleware.shibboleth.idp.authn.provider.AbstractLoginHandler;
import edu.internet2.middleware.shibboleth.idp.util.HttpServletHelper;

public class TestingLoginHandler extends AbstractLoginHandler {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(TestingLoginHandler.class);

    /** The context-relative path of the servlet used to perform authentication. */
    private String authenticationServletPath;

    public TestingLoginHandler(String servletPath) {
        super();
        setSupportsPassive(false);
        setSupportsForceAuthentication(true);
        authenticationServletPath = servletPath;
    }

    public void login(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) {
        // forward control to the servlet.
        try {
            final String authnServletUrl =
                    HttpServletHelper.getContextRelativeUrl(httpRequest, authenticationServletPath)
                    .buildURL();
            log.debug("Redirecting to {}", authnServletUrl);
            httpResponse.sendRedirect(authnServletUrl);
            return;
        } catch (IOException ex) {
            log.error("Unable to redirect to authentication servlet.", ex);
        }

    }
}