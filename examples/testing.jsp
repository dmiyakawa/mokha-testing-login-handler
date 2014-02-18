<!DOCTYPE html>
<%@ taglib uri="urn:mace:shibboleth:2.0:idp:ui" prefix="idpui" %>
<html>
  <head>
    <meta charset="utf-8" />
    <title>Testing Auth Page</title>
    <!-- Use same css as embedded login.jsp -->
    <link rel="stylesheet"
          type="text/css"
          href="<%= request.getContextPath()%>/login.css"/>
  </head>

  <body>
    <div class="wrapper">
      <div class="container">
        <header>
          <a class="logo" href="../images/testing.png">
            <img src="<%= request.getContextPath()%>/images/testing.png"
                 alt="Example image"/>
          </a>
        </header>
    
        <div class="content">
          <div class="column one">

            <% if(request.getAttribute("actionUrl") != null){ %>
            <form id="login"
                  action="<%=request.getAttribute("actionUrl")%>"
                  method="post">
            <% }else{ %>
            <form id="login" action="j_security_check" method="post">
            <% } %>

              <% if ("true".equals(request.getAttribute("loginFailed"))) { %>
                <section>
                  <p class="form-element form-error">
                    Login has failed. Double-check your password.
                  </p>
                </section>
              <% } %>

              <section>
                <label for="username">Username (for dummy principal name)</label>
                <input class="form-element form-field"
                       name="j_username"
                       type="text" value="test001" />
              </section>

              <section>
                <label for="password">Don't use your real password here.</label>
                <input class="form-element form-field"
                       name="j_password"
                       type="password"
                       placeHolder="Don't use your real password here."
                       value="" />
              </section>

              <section>
                <button class="form-element form-button" type="submit">
                  Login
                </button>
              </section>

            </form>
          </div>

          <div class="column two">
          </div>
        </div>
      </div>

      <footer>
        <div class="container container-footer">
          <p class="footer-text">
            This is testing page. DO NOT USE your real password here.
          </p>
        </div>
      </footer>
    </div>
  </body>
</html>
