/*
 * $Header: /home/cvs/jakarta-struts/contrib/struts-chain/src/java/org/apache/struts/chain/AbstractSelectForward.java,v 1.2 2004/02/29 13:12:19 germuska Exp $
 * $Revision: 1.2 $
 * $Date: 2004/02/29 13:12:19 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Struts", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.struts.chain;


import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.struts.chain.Constants;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Select and cache the <code>ActionForward</code> for this
 * <code>ActionConfig</code> if specified.</p>
 *
 * @author Don Brown
 * @version $Revision: 1.2 $ $Date: 2004/02/29 13:12:19 $
 */

public abstract class AbstractSelectForward implements Command {


    // ------------------------------------------------------ Instance Variables


    private String actionConfigKey = Constants.ACTION_CONFIG_KEY;
    private String forwardConfigKey = Constants.FORWARD_CONFIG_KEY;
    private String validKey = Constants.VALID_KEY;

    private static final Log log =
        LogFactory.getLog(AbstractSelectForward.class);


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the context attribute key under which the
     * <code>ActionConfig</code> for the currently selected application
     * action is stored.</p>
     */
    public String getActionConfigKey() {

        return (this.actionConfigKey);

    }


    /**
     * <p>Set the context attribute key under which the
     * <code>ActionConfig</code> for the currently selected application
     * action is stored.</p>
     *
     * @param actionConfigKey The new context attribute key
     */
    public void setActionConfigKey(String actionConfigKey) {

        this.actionConfigKey = actionConfigKey;

    }


    /**
     * <p>Return the context attribute key under which the
     * <code>ForwardConfig</code> for the currently selected application
     * action is stored.</p>
     */
    public String getForwardConfigKey() {

        return (this.forwardConfigKey);

    }


    /**
     * <p>Set the context attribute key under which the
     * <code>ForwardConfig</code> for the currently selected application
     * action is stored.</p>
     *
     * @param forwardConfigKey The new context attribute key
     */
    public void setForwardConfigKey(String forwardConfigKey) {

        this.forwardConfigKey = forwardConfigKey;

    }


    /**
     * <p>Return the context attribute key under which the
     * validity flag for this request is stored.</p>
     */
    public String getValidKey() {

        return (this.validKey);

    }


    /**
     * <p>Set the context attribute key under which the
     * validity flag for this request is stored.</p>
     *
     * @param validKey The new context attribute key
     */
    public void setValidKey(String validKey) {

        this.validKey = validKey;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Select and cache the <code>ActionForward</code> for this
     * <code>ActionConfig</code> if specified.</p>
     *
     * @param context The <code>Context</code> for the current request
     *
     * @return <code>false</code> so that processing continues
     */
    public boolean execute(Context context) throws Exception {

        // Skip processing if the current request is not valid
        Boolean valid = (Boolean) context.get(getValidKey());
        if ((valid == null) || !valid.booleanValue()) {
            return (false);
        }

        // Acquire configuration objects that we need
        ActionConfig actionConfig = (ActionConfig)
                                    context.get(getActionConfigKey());
        ModuleConfig moduleConfig = actionConfig.getModuleConfig();

        ForwardConfig forwardConfig = null;
        String forward = actionConfig.getForward();
        if (forward != null) {
            forwardConfig = forward(context, moduleConfig, forward);
            if (log.isDebugEnabled()) {
                log.debug("Forwarding to " + forwardConfig);
            }
            context.put(getForwardConfigKey(), forwardConfig);
        }
        return (false);

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Create and return a <code>ForwardConfig</code> representing the
     * specified module-relative destination.</p>
     *
     * @param context The context for this request
     * @param moduleConfig The <code>ModuleConfig</code> for this request
     * @param uri The module-relative URI to be the destination
     */
    protected abstract ForwardConfig forward(Context context,
                                             ModuleConfig moduleConfig,
                                             String uri);



}
