package com.cgj.accountbook.event;

/**
 * @author onono
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class MessageEvent {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageEvent(String message) {
        this.message = message;
    }

    private String message ;


}
