# What is this?

Testing Login Handler for the Shibboleth IdP. Tested with Shibboleth IdP 2.4.0.

This will require the user to enter a single preset "password."
Password is embedded in this implementation,
so this won't be usable for the production use but for some sort of development.
You should not use this in production, of course.

# Is this useful?

For most of Shibboleth users, no.

For certain developers, maybe yes.
You may see this as a very simple example of how to implement LoginHandler,
which is even easier than UsernamePasswordLoginHandler.

# How to install?

* Build this package with Apache Maven
* Place resultant library file into lib/ of Shibboleth IdP installation package.
* Place files in examples/ into src/main/webapp/ of Shibboleth IdP installation
package.
* Configure web.xml appropriately (wow, how kind :-P)
* Install IdP again (without overwriting existing configurations)
* All done.

# Why this is implemented.

I wanted another simple LoginHandler other than UsernamePassword that is
distinguished from the other handlers.

# Liscence?

Apache 2.0. This is derivative work of several other Apache 2.0 projects.

* Shibboleth IdP 2.4.0
* yubico multifactor login handler
    * https://github.com/Yubico/yubico-shibboleth-idp-multifactor-login-handler
