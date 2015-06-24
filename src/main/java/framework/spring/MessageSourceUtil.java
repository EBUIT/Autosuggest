package framework.spring;

import org.springframework.context.MessageSource;

/**
 * Allow to access to message source via static method (framework.bean messageSourceUtil)
 * Created by laurent on 26.05.2015.
 */
public class MessageSourceUtil {

    private static MessageSource messageSourceUtil;

    /**
     * @return framework.bean messageSourceUtil
     */
    public static MessageSource getMessageSource() {
        if (messageSourceUtil == null) {
            messageSourceUtil = ApplicationContextJfxUtils.getApplicationContext().getBean("messageSourceUtil", MessageSource.class);
        }
        return messageSourceUtil;
    }

    public static String getMessage(String code, Object... args) {
        return getMessageSource().getMessage(code, args, null);
    }

}
