package com.ilya40umov.badge.dto;

/**
 * Jackson JSON views that allow to control which fields are meant to be serialized.
 *
 * @author isorokoumov
 */
public class Views {

    /**
     * View that is safe to send to anyone.
     */
    public static class Public {
    }

    /**
     * View that contains data which should only be provided to its owner or admin.
     */
    public static class Private extends Public {
    }

}
