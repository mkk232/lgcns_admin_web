package com.lgcns.admin.common.utils;

public class ParamUtils {
    private ParamUtils() {}

    public static int getFrom(int pageNo, int pageSize) {
        return (pageNo - 1) * pageSize;
    }

    public static String[] getSearchType(String searchType) {
        String[] searchFields = null;
        if(searchType == null || searchType.isBlank()) {
            searchType = "all";
        }

        if("all".equals(searchType)) {
            searchFields = new String[] {"subject", "em_body", "attach_body", "attach_name", "sender"};
        } else if("subject".equals(searchType)) {
            searchFields = new String[] {"subject"};
        } else if("body".equals(searchType)) {
            searchFields = new String[] {"em_body"};
        } else if("attachName".equals(searchType)) {
            searchFields = new String[] {"attach_name"};
        } else if("attachBody".equals(searchType)) {
            searchFields = new String[] {"attach_body"};
        } else if("sender".equals(searchType)) {
            searchFields = new String[] {"sender"};
        } else {
            throw new IllegalArgumentException("Invalid search type: " + searchType);
        }

        return searchFields;


    }
}
