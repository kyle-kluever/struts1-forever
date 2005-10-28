/*
 * Copyright 2003,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.struts.chain.commands.servlet;


import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.chain.commands.AbstractSelectLocale;
import org.apache.struts.chain.contexts.ActionContext;
import org.apache.struts.chain.contexts.ServletActionContext;


/**
 * <p>Select the <code>Locale</code> to be used for this request.</p>
 *
 * @version $Rev$ $Date$
 */

public class SelectLocale extends AbstractSelectLocale {


    private static final Log log = LogFactory.getLog(SelectLocale.class);

    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Return the <code>Locale</code> to be used for this request.</p>
     *
     * @param context The <code>Context</code> for this request
     */
    protected Locale getLocale(ActionContext context) {

        ServletActionContext saContext = (ServletActionContext) context;

        // Has a Locale already been selected?
        HttpSession session = saContext.getRequest().getSession();
        Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
        if (locale != null) {
            return (locale);
        }

        // Select and cache the Locale to be used
        locale = saContext.getRequest().getLocale();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        session.setAttribute(Globals.LOCALE_KEY, locale);
        return (locale);

    }


}