package org.wittydev.config;
import java.io.File;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

    public class ConfigPropertiesFile{
        String content;
        String path;
        String rootContextPath;
        long refFileTime;

        public ConfigPropertiesFile(
                                    String rootContextPath,
                                    String path,
                                    String content,
                                    long refFileTime){
            this.rootContextPath = rootContextPath;
            this.path = path;
            this.content= content;
            this.refFileTime = refFileTime;
        }

        public String getPath(){
            return path;
        }
        public String getRootContextPath(){
            return rootContextPath;
        }
        public String getContent(){
            return content;
        }
        public long getRefFileTime(){
            return refFileTime;
        }

    }
