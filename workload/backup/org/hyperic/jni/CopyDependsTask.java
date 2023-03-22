// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.jni;

import org.apache.tools.ant.BuildException;
import java.io.File;
import org.apache.tools.ant.taskdefs.Copy;

public class CopyDependsTask extends Copy
{
    private File depends;
    
    protected void validateAttributes() throws BuildException {
        super.validateAttributes();
        if (this.depends == null) {
            throw new BuildException("missing depends attribute");
        }
    }
    
    public void execute() throws BuildException {
        if (this.destFile.exists()) {
            String state;
            if (this.depends.lastModified() > this.destFile.lastModified()) {
                this.setOverwrite(true);
                state = "out of date";
            }
            else {
                state = "up to date";
            }
            this.log(this.destFile + " " + state + " with " + this.depends);
        }
        super.execute();
    }
    
    public File getDepends() {
        return this.depends;
    }
    
    public void setDepends(final String depends) {
        this.depends = new File(depends);
    }
}
