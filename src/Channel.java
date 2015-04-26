import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class Channel {

    private String name;
    private HashSet<String> userIds;

    public Channel(String name) {
        this.name = name;
        this.userIds = new HashSet<String>();
    }

    public void addUserId(String userId) {
        userIds.add(userId);
    }

    public List<String> getUserIds() {
        return new ArrayList<String>(userIds);
    }

}
