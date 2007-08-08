/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.jvnet.mimepull;

import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Represents an attachment part in a MIME message. MIME message parsing is done
 * lazily using a pull parser, so the part may not have all the data. {@link #read}
 * and {@link #readOnce} may trigger the actual parsing the message. In fact,
 * parsing of an attachment part may be triggered by calling {@link #read} methods
 * on some other attachemnt parts. All this happens behind the scenes so the
 * application developer need not worry about these details.
 *
 * @author Jitendra Kotamraju
 */
public class MIMEPart {

    private InternetHeaders headers;
    private String contentId;
    private volatile boolean parsed;
    private final MIMEConfig config;

    // only one is not null ByteArrayList or RandomAccessFile
    private ByteArrayBufferList buf;
    private RandomAccessFile file;

    MIMEPart(MIMEConfig config) {
        this.config = config;
    }

    MIMEPart(MIMEConfig config, String contentId) {
        this(config);
        this.contentId = contentId;
    }

    /**
     * Can get the attachment part's content multiple times. That means
     * the full content needs to be there in memory or on the file system.
     *
     * TODO: can it called multiple times concurrently
     * @return data for the part's content
     */
    public InputStream read() {
        return null;
    }

    /**
     * Can get the attachment part's content only once. The content
     * will be lost after the method. Content data is not be stored
     * on the file system or is not kept in the memory for the
     * following case:
     *   - Attachement parts contents are accessed sequentially
     *
     * In general, take advantage of this when the data is used only
     * once.
     *
     * @return data for the part's content
     */
    public InputStream readOnce() {
        return null;
    }

    /**
     * Returns Content-ID MIME header for this attachment part
     *
     * @return Content-ID of the part
     */
    public String getContentId() {
        return contentId;
    }

    void setHeaders(InternetHeaders headers) {
        this.headers = headers;
    }

    void addBody(ByteArrayBuffer buf) {
    }
    
    void doneParsing() {
        parsed = true;
    }

    public void setContentId(String cid) {
        this.contentId = cid;
    }

    private class MyInputStream extends InputStream {
        boolean onetime = true;

        MyInputStream() {

        }

        public int read() throws IOException {
            /*
            if (parsed) {
                // Parsing is done
            } else {
                if (onetime) {
                    onetime = false;
                    parser.readMinimum();

                }
            }
            */

            return 0;
        }
    }

    @Override
    public String toString() {
        return "Part="+contentId;
    }

}