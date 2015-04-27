import java.io.ObjectOutputStream;
import java.util.UUID;

public class UserInfo {
    String id;
    long lastTimestampSeconds;
    boolean isAway;
    boolean isInvisible;
    boolean isOperator;

    String username;
    String realName;

    ObjectOutputStream outStream;

    public UserInfo(ObjectOutputStream outStream) {
        this.outStream = outStream;

        this.id = UUID.randomUUID().toString();
        this.lastTimestampSeconds = System.currentTimeMillis() / 1000;
        this.isAway = false;
        this.isInvisible = false;
        this.isOperator = false;
    }
}
