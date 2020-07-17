/*
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.fl.model;

import java.util.List;

public class FaceListResult {

    private List<FaceBean> face_list;


    public List<FaceBean> getFace_list() {
        return face_list;
    }

    public void setFace_list(List<FaceBean> face_list) {
        this.face_list = face_list;
    }

   public final class FaceBean {
        /**
         * face_token : 13947e7a47e60c1b8a87a551cf4b1946
         * ctime : 2019-10-28 18:14:57
         */

        private String face_token;
        private String ctime;

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }
    }
}
