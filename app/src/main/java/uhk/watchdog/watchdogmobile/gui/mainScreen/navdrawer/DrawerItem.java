package uhk.watchdog.watchdogmobile.gui.mainScreen.navdrawer;

/**
 * Created by Tobous on 5. 2. 2015.
 */
public class DrawerItem {

    /**
     *
     */
    private int icon;

    /**
     *
     */
    private int type;

    /**
     *
     */
    private String title;

    /**
     *
     * @param icon
     * @param type
     * @param title
     */
    public DrawerItem(int icon, int type, String title) {
        super();
        this.icon = icon;
        this.type = type;
        this.title = title;
    }

    /**
     *
     * @return
     */
    public int getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }
}
